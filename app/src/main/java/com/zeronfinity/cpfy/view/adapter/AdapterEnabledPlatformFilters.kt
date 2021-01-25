package com.zeronfinity.cpfy.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat.getColor
import androidx.recyclerview.widget.RecyclerView
import com.zeronfinity.core.entity.Platform
import com.zeronfinity.core.logger.logD
import com.zeronfinity.core.usecase.DisablePlatformUseCase
import com.zeronfinity.cpfy.R
import com.zeronfinity.cpfy.common.PLATFORM_FILTER_MAX_COUNT
import com.zeronfinity.cpfy.databinding.ItemPlatformFilterBinding
import javax.inject.Inject

class AdapterEnabledPlatformFilters @Inject constructor(
    private val disablePlatformUseCase: DisablePlatformUseCase
) : RecyclerView.Adapter<AdapterEnabledPlatformFilters.PlatformViewHolder>() {
    private var isMaxCountSet = true
    private var maxItemCount = PLATFORM_FILTER_MAX_COUNT

    private var platformList = ArrayList<Platform>()

    inner class PlatformViewHolder(
        private val binding: ItemPlatformFilterBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(platform: Platform) {
            binding.btnPlatformFilter.background.setTint(getColor(binding.btnPlatformFilter.context, R.color.secondaryColor))
            binding.btnPlatformFilter.isSelected = true
            binding.btnPlatformFilter.text = platform.shortName

            binding.btnPlatformFilter.setOnClickListener {
                logD("platform button clicked -> oldTag: [${it.tag}], platformName: [${platform.name}], platformId: [${platform.id}]")
                disablePlatformUseCase(platform.id)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdapterEnabledPlatformFilters.PlatformViewHolder {
        val binding: ItemPlatformFilterBinding =
            ItemPlatformFilterBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        return PlatformViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PlatformViewHolder, position: Int) {
        holder.bind(platformList[position])
    }

    override fun getItemCount(): Int {
        return minOf(maxItemCount, platformList.size)
    }

    fun refreshPlatformList(list: List<Platform>) {
        logD("refreshPlatformList() started")
        platformList.clear()
        platformList.addAll(list)

        maxItemCount = if (isMaxCountSet) {
            PLATFORM_FILTER_MAX_COUNT
        } else {
            platformList.size
        }

        notifyDataSetChanged()
    }

    fun setMaxItemCount() {
        isMaxCountSet = true
        maxItemCount = PLATFORM_FILTER_MAX_COUNT
        notifyDataSetChanged()
    }

    fun resetMaxItemCount() {
        isMaxCountSet = false
        maxItemCount = platformList.size
        notifyDataSetChanged()
    }
}
