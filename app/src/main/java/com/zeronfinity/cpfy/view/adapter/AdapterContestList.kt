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
import com.zeronfinity.cpfy.common.makeFullDurationText
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

            when {
                contest.endTime.time < Date().time -> {
                    binding.tvTimeLeft.text =
                        binding.tvTimeLeft.context.getString(R.string.contest_already_ended)
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
                        R.string.duration_colon_tv_label
                    )
                    binding.tvDuration.text =
                        makeFullDurationText(
                            contest.duration.toLong()
                        )
                }
                contest.startTime.time < Date().time -> {
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
                        makeFullDurationText(
                            contest.duration.toLong() + (contest.startTime.time - Date().time) / 1000
                        )
                }
                else -> {
                    binding.tvTimeLeft.text = binding.tvTimeLeft.context.getString(
                        R.string.time_left_to_start,
                        makeFullDurationText(
                            (contest.startTime.time - Date().time) / 1000
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
                        makeFullDurationText(
                            contest.duration.toLong()
                        )
                }
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
}
