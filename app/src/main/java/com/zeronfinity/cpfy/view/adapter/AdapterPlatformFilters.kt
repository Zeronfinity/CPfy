package com.zeronfinity.cpfy.view.adapter

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getColor
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.zeronfinity.core.entity.Contest
import com.zeronfinity.core.entity.Platform
import com.zeronfinity.cpfy.R
import com.zeronfinity.cpfy.databinding.ItemContestBinding
import com.zeronfinity.cpfy.databinding.ItemPlatformFilterBinding
import com.zeronfinity.cpfy.model.UseCases
import com.zeronfinity.cpfy.view.MainActivity
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class AdapterPlatformFilters @Inject constructor(val usecase: UseCases) :
    RecyclerView.Adapter<AdapterPlatformFilters.PlatformViewHolder>() {
    private val LOG_TAG = MainActivity::class.simpleName

    private val platformList = usecase.getPlatformListUseCase()

    inner class PlatformViewHolder(
        private val binding: ItemPlatformFilterBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(platform: Platform) {
            binding.btnPlatformFilter.tag = "enabled"
            binding.btnPlatformFilter.text = platform.shortName

            binding.btnPlatformFilter.isSelected = true

            binding.btnPlatformFilter.setOnClickListener {
                when (it.tag) {
                    "enabled" -> {
                        it.background.setTint(getColor(it.context, R.color.secondaryLightColor))
                        it.tag = "disabled"
                    }
                    "disabled" -> {
                        it.background.setTint(getColor(it.context, R.color.secondaryColor))
                        it.tag = "enabled"
                    }
                    else -> {
                        Log.e(LOG_TAG, "Invalid platform filter button tag: " + it.tag.toString())
                    }
                }
            }
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
