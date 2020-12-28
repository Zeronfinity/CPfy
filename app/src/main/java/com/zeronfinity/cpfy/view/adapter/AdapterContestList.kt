package com.zeronfinity.cpfy.view.adapter

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.zeronfinity.core.entity.Contest
import com.zeronfinity.core.usecase.GetFilteredContestListUseCase
import com.zeronfinity.core.usecase.GetPlatformUseCase
import com.zeronfinity.cpfy.R
import com.zeronfinity.cpfy.databinding.ItemContestBinding
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class AdapterContestList @Inject constructor(
    private val getFilteredContestListUseCase: GetFilteredContestListUseCase,
    private val getPlatformUseCase: GetPlatformUseCase
) : RecyclerView.Adapter<AdapterContestList.ContestViewHolder>() {
    private var filteredContestList = getFilteredContestListUseCase()

    inner class ContestViewHolder(
        private val binding: ItemContestBinding
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
                    R.string.started_on_colon_tv_label
                )
                binding.tvDurationLabel.text = binding.tvDurationLabel.context.getString(
                    R.string.time_left_colon_tv_label
                )
                binding.tvDuration.text =
                    parseSecondsToString(
                        contest.duration.toLong() + diffInMillis / 1000
                    )
            } else {
                binding.tvTimeLeft.text = binding.tvTimeLeft.context.getString(
                    R.string.time_left_to_start,
                    parseSecondsToString(
                        diffInMillis / 1000
                    )
                )
                binding.tvTimeLeft.setTextColor(
                    ContextCompat.getColor(
                        binding.tvTimeLeft.context,
                        R.color.primaryDarkColor
                    )
                )

                binding.tvStartsOnLabel.text = binding.tvStartsOnLabel.context.getString(
                    R.string.starts_on_colon_tv_label
                )
                binding.tvDurationLabel.text = binding.tvDurationLabel.context.getString(
                    R.string.duration_colon_tv_label
                )
                binding.tvDuration.text =
                    parseSecondsToString(
                        contest.duration.toLong()
                    )
            }

            val platform = getPlatformUseCase(contest.platformName)

            platform?.imageUrl?.let {
                Picasso.get()
                    .load(it)
                    .resize(50, 50)
                    .centerCrop()
                    .into(binding.ivContestPlatform)
            }

            binding.tvContestPlatform.text = platform?.shortName

            binding.ivLaunch.setOnClickListener {
                val launchUrlIntent = Intent(Intent.ACTION_VIEW, Uri.parse(contest.url))
                try {
                    it.context.startActivity(launchUrlIntent)
                } catch (ex: ActivityNotFoundException) {
                    Toast.makeText(it.context, "No external app found for opening url!", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContestViewHolder {
        val binding: ItemContestBinding =
            ItemContestBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        return ContestViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ContestViewHolder, position: Int) =
        holder.bind(filteredContestList[position])

    override fun getItemCount() = filteredContestList.size

    fun refreshContestList() {
        filteredContestList = getFilteredContestListUseCase()
        notifyDataSetChanged()
    }

    private fun parseSecondsToString(durationInSeconds: Long): String {
        val minutes = (durationInSeconds / 60) % 60
        val hours = (durationInSeconds / 60 / 60) % 24
        val days = durationInSeconds / 60 / 60 / 24
        var ret = ""
        if (days != 0L) {
            ret += "$days day"
            if (days > 1) ret += "s"
        }
        if (hours != 0L) {
            if (ret.isNotEmpty()) ret += " "
            ret += "$hours hour"
            if (hours > 1) ret += "s"
        }
        if (minutes != 0L) {
            if (ret.isNotEmpty()) ret += " "
            ret += "$minutes minute"
            if (minutes > 1) ret += "s"
        }
        return ret
    }
}
