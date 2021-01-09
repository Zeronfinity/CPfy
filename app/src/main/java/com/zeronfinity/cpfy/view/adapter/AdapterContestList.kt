package com.zeronfinity.cpfy.view.adapter

import android.app.Activity
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
import com.zeronfinity.core.logger.logD
import com.zeronfinity.core.usecase.GetPlatformUseCase
import com.zeronfinity.cpfy.R
import com.zeronfinity.cpfy.databinding.ItemContestBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class AdapterContestList @Inject constructor(
    private val activity: Activity,
    private val getPlatformUseCase: GetPlatformUseCase
) : RecyclerView.Adapter<AdapterContestList.ContestViewHolder>() {
    private val coroutineScope = CoroutineScope(Dispatchers.IO)

    private var contestList = ArrayList<Contest>()

    inner class ContestViewHolder(
        private val binding: ItemContestBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(contest: Contest) {
            binding.tvContestName.text = contest.name
            binding.tvStartTime.text = SimpleDateFormat(
                "dd-MMM-yy hh:mm a Z",
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
                    R.string.started_at_colon_tv_label
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
                    R.string.starts_at_colon_tv_label
                )
                binding.tvDurationLabel.text = binding.tvDurationLabel.context.getString(
                    R.string.duration_colon_tv_label
                )
                binding.tvDuration.text =
                    parseSecondsToString(
                        contest.duration.toLong()
                    )
            }

            coroutineScope.launch {
                val platform = getPlatformUseCase(contest.platformId)

                activity.runOnUiThread {
                    platform?.imageUrl.let {
                        Picasso.get()
                            .load(it)
                            .resize(48, 48)
                            .centerCrop()
                            .into(binding.ivContestPlatform)
                    }
                    binding.tvContestPlatform.text = platform?.shortName
                }
            }

            binding.ivLaunch.setOnClickListener {
                val launchUrlIntent = Intent(Intent.ACTION_VIEW, Uri.parse(contest.url))
                try {
                    it.context.startActivity(launchUrlIntent)
                } catch (ex: ActivityNotFoundException) {
                    Toast.makeText(
                        it.context,
                        "No external app found for opening url!",
                        Toast.LENGTH_SHORT
                    ).show()
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

    override fun onBindViewHolder(holder: ContestViewHolder, position: Int) {
        holder.bind(contestList[position])
    }

    override fun getItemCount(): Int {
        return contestList.size
    }

    fun refreshContestList(list: List<Contest>) {
        logD("refreshContestList() started")
        contestList.clear()
        contestList.addAll(list)
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
