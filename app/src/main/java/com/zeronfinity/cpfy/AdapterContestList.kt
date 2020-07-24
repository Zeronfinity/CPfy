package com.zeronfinity.cpfy

import android.content.Context
import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.zeronfinity.cpfy.databinding.RecyclerviewContestItemBinding
import java.text.SimpleDateFormat
import java.util.Date
import kotlin.collections.ArrayList

class AdapterContestList(private val dataset: ArrayList<MainActivity.ContestData>) :
        RecyclerView.Adapter<AdapterContestList.ContestViewHolder>() {

    class ContestViewHolder(private val binding: RecyclerviewContestItemBinding) :
            RecyclerView.ViewHolder(binding.root) {
        fun bind(contestData: MainActivity.ContestData) {
            binding.tvContestName.text = contestData.name
            binding.tvStartTime.text =  SimpleDateFormat("dd MMM yyyy hh:mm a z").format(contestData.startTime)

            val diffInMillis = contestData.startTime.getTime() - Date().getTime()
            if (diffInMillis < 0) {
                binding.tvTimeLeft.text = "Contest already started!"
                binding.tvTimeLeft.setTextColor(ContextCompat.getColor(binding.tvTimeLeft.context, R.color.primaryTextColor))

                binding.tvStartsOnLabel.text = "Started on: "
                binding.tvDurationLabel.text = "Time left: "
                binding.tvDuration.text = parseSecondsToString(contestData.duration + diffInMillis.toInt() / 1000)
            } else {
                binding.tvTimeLeft.text = parseSecondsToString((diffInMillis / 1000).toInt()) + " to start"
                binding.tvTimeLeft.setTextColor(ContextCompat.getColor(binding.tvTimeLeft.context, R.color.primaryDarkColor))

                binding.tvStartsOnLabel.text = "Starts on: "
                binding.tvDurationLabel.text = "Duration: "
                binding.tvDuration.text = parseSecondsToString(contestData.duration)
            }

            if (platformImages.containsKey(contestData.platformName)) {
                binding.ivContestPlatform.setImageBitmap(platformImages[contestData.platformName])
            }
            binding.tvContestPlatform.text = contestData.platformName
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): AdapterContestList.ContestViewHolder {
        val binding = RecyclerviewContestItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ContestViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AdapterContestList.ContestViewHolder, position: Int) {
        holder.bind(dataset[position])
    }

    override fun getItemCount() = dataset.size
}
