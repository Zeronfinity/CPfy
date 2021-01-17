package com.zeronfinity.cpfy.view

import android.os.Bundle
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.URLSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.wanghong.cromappwhitelist.AppWhitelist
import com.zeronfinity.core.logger.logD
import com.zeronfinity.cpfy.R
import com.zeronfinity.cpfy.databinding.FragmentNotificationBinding
import com.zeronfinity.cpfy.view.adapter.AdapterNotificationPriorities
import com.zeronfinity.cpfy.viewmodel.ContestListViewModel
import com.zeronfinity.cpfy.viewmodel.NotificationViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class NotificationFragment : BaseFragment(), AdapterView.OnItemSelectedListener {
    private var _binding: FragmentNotificationBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var adapterNotificationPriorities: AdapterNotificationPriorities

    private lateinit var contentListViewModel: ContestListViewModel
    private lateinit var notificationViewModel: NotificationViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        logD("onCreateView started")

        _binding = FragmentNotificationBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        logD("onViewCreated started")

        val dontKillMyAppUrl = getString(R.string.dontkillmyapp_url)
        val spannableString = SpannableStringBuilder()
            .append(getString(R.string.notification_header_label) + " ")
            .append(dontKillMyAppUrl, URLSpan(dontKillMyAppUrl), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

        binding.tvNotificationHeader.text = spannableString
        binding.btnAutoStart.setOnClickListener {
            logD("btnAutoStart clicked!")

            activity?.let {
                AppWhitelist.settingForAutoStart(it)
            }
        }

        binding.btnNotifications.setOnClickListener {
            logD("btnNotifications clicked!")

            activity?.let {
                AppWhitelist.settingForNotification(it)
            }
        }

        binding.btnBatterySaver.setOnClickListener {
            logD("btnBatterySaver clicked!")

            activity?.let {
                AppWhitelist.settingForBatterySaver(it)
            }
        }

        binding.rvNotificationPriorities.adapter = adapterNotificationPriorities
        binding.rvNotificationPriorities.layoutManager = LinearLayoutManager(activity)
        binding.rvNotificationPriorities.setHasFixedSize(true)

        binding.tvChangeAll.isSelected = true

        ArrayAdapter.createFromResource(
            binding.spinnerPriorityAll.context,
            R.array.notification_priority,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.spinnerPriorityAll.adapter = adapter
        }

        binding.spinnerPriorityAll.onItemSelectedListener = this

        contentListViewModel =
            ViewModelProvider(requireActivity()).get(ContestListViewModel::class.java)
        observeContestListViewModel()

        notificationViewModel = ViewModelProvider(this).get(NotificationViewModel::class.java)
    }

    private fun observeContestListViewModel() {
        contentListViewModel.platformListLiveData.observe(viewLifecycleOwner, {
            logD("platformListLiveData -> platformList[$it]")
            adapterNotificationPriorities.refreshPlatformList(it)
        })
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        val notificationPriorityList = resources.getStringArray(R.array.notification_priority).toList()
        notificationViewModel.setAllNotificationPriorityList(notificationPriorityList[position])
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
    }
}
