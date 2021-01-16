package com.zeronfinity.cpfy.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.wanghong.cromappwhitelist.AppWhitelist
import com.zeronfinity.core.logger.logD
import com.zeronfinity.cpfy.databinding.FragmentNotificationBinding
import com.zeronfinity.cpfy.view.adapter.AdapterNotificationPriorities
import com.zeronfinity.cpfy.viewmodel.ContestListViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class NotificationFragment : BaseFragment() {
    private var _binding: FragmentNotificationBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var adapterNotificationPriorities: AdapterNotificationPriorities

    private lateinit var contentListViewModel: ContestListViewModel

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

        binding.btnAutoStart.setOnClickListener {
            logD("btnAutoStart clicked!")

            activity?.let {
                AppWhitelist.settingForAutoStart(it);
            }
        }

        binding.btnNotifications.setOnClickListener {
            logD("btnNotifications clicked!")

            activity?.let {
                AppWhitelist.settingForNotification(it);
            }
        }

        binding.btnBatterySaver.setOnClickListener {
            logD("btnBatterySaver clicked!")

            activity?.let {
                AppWhitelist.settingForBatterySaver(it);
            }
        }

        binding.rvNotificationPriorities.adapter = adapterNotificationPriorities
        binding.rvNotificationPriorities.layoutManager = LinearLayoutManager(activity)
        binding.rvNotificationPriorities.setHasFixedSize(true)

        contentListViewModel =
            ViewModelProvider(requireActivity()).get(ContestListViewModel::class.java)
        observeContestListViewModel()
    }

    private fun observeContestListViewModel() {
        contentListViewModel.platformListLiveData.observe(viewLifecycleOwner, {
            logD("platformListLiveData -> platformList[$it]")
            adapterNotificationPriorities.refreshPlatformList(it)
        })
    }
}
