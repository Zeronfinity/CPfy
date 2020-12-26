package com.zeronfinity.cpfy.view.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import androidx.core.content.ContextCompat.getColor
import androidx.recyclerview.widget.RecyclerView
import com.zeronfinity.core.entity.Platform
import com.zeronfinity.core.usecase.DisablePlatformUseCase
import com.zeronfinity.core.usecase.EnablePlatformUseCase
import com.zeronfinity.core.usecase.GetPlatformListUseCase
import com.zeronfinity.core.usecase.IsPlatformEnabledUseCase
import com.zeronfinity.cpfy.R
import com.zeronfinity.cpfy.databinding.ItemPlatformFilterBinding
import com.zeronfinity.cpfy.view.MainActivity
import javax.inject.Inject

class AdapterPlatformFilters @Inject constructor(
    private val disablePlatformUseCase: DisablePlatformUseCase,
    private val enablePlatformUseCase: EnablePlatformUseCase,
    private val isPlatformEnabledUseCase: IsPlatformEnabledUseCase,
    private val getPlatformListUseCase: GetPlatformListUseCase
) : RecyclerView.Adapter<AdapterPlatformFilters.PlatformViewHolder>() {
    private val LOG_TAG = MainActivity::class.simpleName

    private val platformList = getPlatformListUseCase()

    inner class PlatformViewHolder(
        private val binding: ItemPlatformFilterBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(platform: Platform) {
            binding.btnPlatformFilter.isSelected = true
            binding.btnPlatformFilter.text = platform.shortName

            if (isPlatformEnabledUseCase(platform.name)) {
                enableButton(binding.btnPlatformFilter)
            } else {
                disableButton(binding.btnPlatformFilter)
            }

            binding.btnPlatformFilter.setOnClickListener {
                when (it.tag) {
                    "enabled" -> {
                        disableButton(it as Button)
                        disablePlatformUseCase(platform.name)
                    }
                    "disabled" -> {
                        enableButton(it as Button)
                        enablePlatformUseCase(platform.name)
                    }
                    else -> {
                        Log.e(LOG_TAG, "Invalid platform filter button tag: " + it.tag.toString())
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

    override fun onBindViewHolder(holder: PlatformViewHolder, position: Int) =
        holder.bind(platformList[position])

    override fun getItemCount() = platformList.size
}
