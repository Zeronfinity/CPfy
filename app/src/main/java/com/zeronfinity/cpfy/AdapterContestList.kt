package com.zeronfinity.cpfy

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.zeronfinity.cpfy.databinding.RecyclerviewContestItemBinding
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class AdapterContestList(private val dataset: ArrayList<MainActivity.ContestData>) :
        RecyclerView.Adapter<AdapterContestList.ContestViewHolder>() {

    class ContestViewHolder(private val binding: RecyclerviewContestItemBinding) :
            RecyclerView.ViewHolder(binding.root) {
        fun bind(contestData: MainActivity.ContestData) {
            binding.tvContestName.text = contestData.name
            binding.tvStartTime.text =  SimpleDateFormat("E dd-MMM-yy hh:mm a", Locale.getDefault()).format(contestData.startTime)

            val diffInMillis = contestData.startTime.time - Date().time
            if (diffInMillis < 0) {
                binding.tvTimeLeft.text = binding.tvTimeLeft.context.getString(R.string.contest_already_started)
                binding.tvTimeLeft.setTextColor(ContextCompat.getColor(binding.tvTimeLeft.context, R.color.primaryTextColor))

                binding.tvStartsOnLabel.text = binding.tvStartsOnLabel.context.getString(R.string.started_on_tv_label)
                binding.tvDurationLabel.text = binding.tvDurationLabel.context.getString(R.string.time_left_tv_label)
                binding.tvDuration.text = parseSecondsToString(contestData.duration + diffInMillis.toInt() / 1000)
            } else {
                binding.tvTimeLeft.text = binding.tvTimeLeft.context.getString(R.string.time_left_to_start, parseSecondsToString(diffInMillis.toInt() / 1000))
                binding.tvTimeLeft.setTextColor(ContextCompat.getColor(binding.tvTimeLeft.context, R.color.primaryDarkColor))

                binding.tvStartsOnLabel.text = binding.tvStartsOnLabel.context.getString(R.string.starts_on_tv_label)
                binding.tvDurationLabel.text = binding.tvDurationLabel.context.getString(R.string.duration_tv_label)
                binding.tvDuration.text = parseSecondsToString(contestData.duration)
            }

            if (platformImages.containsKey(contestData.platformName)) {
                binding.ivContestPlatform.setImageBitmap(platformImages[contestData.platformName])
            }
            binding.tvContestPlatform.text = contestData.platformName
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContestViewHolder {
        val binding: RecyclerviewContestItemBinding =
            RecyclerviewContestItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ContestViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ContestViewHolder, position: Int) {
        holder.bind(dataset[position])
    }

    override fun getItemCount() = dataset.size
}
