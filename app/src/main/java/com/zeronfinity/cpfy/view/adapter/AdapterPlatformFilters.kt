package com.zeronfinity.cpfy.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import androidx.core.content.ContextCompat.getColor
import androidx.recyclerview.widget.RecyclerView
import com.zeronfinity.core.entity.Platform
import com.zeronfinity.core.logger.logD
import com.zeronfinity.core.logger.logE
import com.zeronfinity.core.usecase.DisablePlatformUseCase
import com.zeronfinity.core.usecase.EnablePlatformUseCase
import com.zeronfinity.cpfy.R
import com.zeronfinity.cpfy.common.PLATFORM_FILTER_MAX_COUNT
import com.zeronfinity.cpfy.databinding.ItemPlatformFilterBinding
import javax.inject.Inject

class AdapterPlatformFilters @Inject constructor(
    private val disablePlatformUseCase: DisablePlatformUseCase,
    private val enablePlatformUseCase: EnablePlatformUseCase
) : RecyclerView.Adapter<AdapterPlatformFilters.PlatformViewHolder>() {
    private var maxItemCount = PLATFORM_FILTER_MAX_COUNT

    private var platformList = ArrayList<Platform>()

    inner class PlatformViewHolder(
        private val binding: ItemPlatformFilterBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(platform: Platform) {
            binding.btnPlatformFilter.isSelected = true
            binding.btnPlatformFilter.text = platform.shortName

            if (platform.isEnabled) {
                enableButton(binding.btnPlatformFilter)
            } else {
                disableButton(binding.btnPlatformFilter)
            }

            binding.btnPlatformFilter.setOnClickListener {
                logD("platform button clicked -> oldTag: [${it.tag}], platformName: [${platform.name}], platformId: [${platform.id}]")
                when (it.tag) {
                    "enabled" -> {
                        disableButton(it as Button)
                        disablePlatformUseCase(platform.id)
                    }
                    "disabled" -> {
                        enableButton(it as Button)
                        enablePlatformUseCase(platform.id)
                    }
                    else -> {
                        logE("Invalid platform filter button tag: " + it.tag.toString())
                    }
                }
            }
        }

        private fun disableButton(btnPlatform: Button) {
            btnPlatform.background.setTint(getColor(binding.btnPlatformFilter.context, R.color.secondaryLightColor))
            btnPlatform.tag = "disabled"
        }

        private fun enableButton(btnPlatform: Button) {
            btnPlatform.background.setTint(getColor(binding.btnPlatformFilter.context, R.color.secondaryColor))
            btnPlatform.tag = "enabled"
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlatformViewHolder {
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
        notifyDataSetChanged()
    }

    fun setMaxItemCount() {
        maxItemCount = PLATFORM_FILTER_MAX_COUNT
        notifyDataSetChanged()
    }

    fun resetMaxItemCount() {
        maxItemCount = platformList.size
        notifyDataSetChanged()
    }
}
