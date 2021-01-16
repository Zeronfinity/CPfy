package com.zeronfinity.cpfy.view.adapter

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.recyclerview.widget.RecyclerView
import com.zeronfinity.core.entity.Platform
import com.zeronfinity.core.logger.logD
import com.zeronfinity.core.usecase.SetPlatformNotificationPriorityUseCase
import com.zeronfinity.cpfy.R
import com.zeronfinity.cpfy.databinding.ItemNotificationPriorityBinding
import javax.inject.Inject

class AdapterNotificationPriorities @Inject constructor(
    private val activity: Activity,
    private val setPlatformNotificationPriorityUseCase: SetPlatformNotificationPriorityUseCase
) : RecyclerView.Adapter<AdapterNotificationPriorities.ViewHolder>() {

    private var platformList = ArrayList<Platform>()

    inner class ViewHolder(
        private val binding: ItemNotificationPriorityBinding
    ) : RecyclerView.ViewHolder(binding.root), AdapterView.OnItemSelectedListener {
        private val notificationPriorityList = activity.resources.getStringArray(R.array.notification_priority).toList()

        fun bind(platformPosition: Int) {
            val platform = platformList[platformPosition]

            binding.tvPlatformName.isSelected = true
            binding.tvPlatformName.text = platform.shortName

            ArrayAdapter.createFromResource(
                binding.spinnerPriority.context,
                R.array.notification_priority,
                android.R.layout.simple_spinner_item
            ).also { adapter ->
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                binding.spinnerPriority.adapter = adapter
            }

            binding.spinnerPriority.tag = platformPosition
            binding.spinnerPriority.onItemSelectedListener = this
            binding.spinnerPriority.setSelection(notificationPriorityList.indexOf(platform.notificationPriority))
        }

        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
            parent?.let {
                val platform = platformList[it.tag as Int]
                if (platform.notificationPriority != notificationPriorityList[position]) {
                    logD("onItemSelected() -> platform: [$platform], priorityPosition: [$position], notificationPriority: [${notificationPriorityList[position]}]")
                    setPlatformNotificationPriorityUseCase(platform.id, notificationPriorityList[position])
                }
            }
        }

        override fun onNothingSelected(parent: AdapterView<*>?) {
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: ItemNotificationPriorityBinding =
            ItemNotificationPriorityBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int {
        return platformList.size
    }

    fun refreshPlatformList(list: List<Platform>) {
        logD("refreshPlatformList() started")
        platformList.clear()
        platformList.addAll(list)
        notifyDataSetChanged()
    }
}
