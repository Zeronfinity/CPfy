package com.zeronfinity.cpfy.framework.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.zeronfinity.core.entity.Contest
import com.zeronfinity.cpfy.R
import com.zeronfinity.cpfy.databinding.RecyclerviewContestItemBinding
import com.zeronfinity.cpfy.model.UseCases
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class AdapterContestList @Inject constructor(val usecase: UseCases) :
    RecyclerView.Adapter<AdapterContestList.ContestViewHolder>() {

    inner class ContestViewHolder(
        private val binding: RecyclerviewContestItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(contest: Contest) {
            binding.tvContestName.text = contest.name
            binding.tvStartTime.text = SimpleDateFormat(
                "E dd-MMM-yy hh:mm a",
                Locale.getDefault()
            ).format(contest.startTime)

            val diffInMillis = contest.startTime.time - Date().time
            if (diffInMillis < 0) {
                binding.tvTimeLeft.text =
                    binding.tvTimeLeft.context.getString(R.string.contest_already_started)
                binding.tvTimeLeft.setTextColor(
                    ContextCompat.getColor(
                        binding.tvTimeLeft.context,
                        R.color.primaryTextColor
                    )
                )

                binding.tvStartsOnLabel.text = binding.tvStartsOnLabel.context.getString(
                    R.string.started_on_tv_label
                )
                binding.tvDurationLabel.text = binding.tvDurationLabel.context.getString(
                    R.string.time_left_tv_label
                )
                binding.tvDuration.text =
                    parseSecondsToString(
                        contest.duration + diffInMillis.toInt() / 1000
                    )
            } else {
                binding.tvTimeLeft.text = binding.tvTimeLeft.context.getString(
                    R.string.time_left_to_start,
                    parseSecondsToString(
                        diffInMillis.toInt() / 1000
                    )
                )
                binding.tvTimeLeft.setTextColor(
                    ContextCompat.getColor(
                        binding.tvTimeLeft.context,
                        R.color.primaryDarkColor
                    )
                )

                binding.tvStartsOnLabel.text = binding.tvStartsOnLabel.context.getString(
                    R.string.starts_on_tv_label
                )
                binding.tvDurationLabel.text = binding.tvDurationLabel.context.getString(
                    R.string.duration_tv_label
                )
                binding.tvDuration.text =
                    parseSecondsToString(
                        contest.duration
                    )
            }

            val platformImageUrl = usecase.getPlatformImageUrlUseCase(contest.platformName)

            if (platformImageUrl != null) {
                Picasso.get()
                    .load(platformImageUrl)
                    .resize(50, 50)
                    .centerCrop()
                    .into(binding.ivContestPlatform)
            }

            binding.tvContestPlatform.text = contest.platformName
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContestViewHolder {
        val binding: RecyclerviewContestItemBinding =
            RecyclerviewContestItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        return ContestViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ContestViewHolder, position: Int) =
        holder.bind(usecase.getContestUseCase(position))

    override fun getItemCount() = usecase.getContestCountUseCase()

    private fun parseSecondsToString(durationInSeconds: Int): String {
        val minutes = (durationInSeconds / 60) % 60
        val hours = (durationInSeconds / 60 / 60) % 24
        val days = durationInSeconds / 60 / 60 / 24
        var ret = ""
        if (days != 0) {
            ret += "$days day"
            if (days > 1) ret += "s"
        }
        if (hours != 0) {
            if (ret.isNotEmpty()) ret += " "
            ret += "$hours hour"
            if (hours > 1) ret += "s"
        }
        if (minutes != 0) {
            if (ret.isNotEmpty()) ret += " "
            ret += "$minutes minute"
            if (minutes > 1) ret += "s"
        }
        return ret
    }
}
