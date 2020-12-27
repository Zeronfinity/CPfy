package com.zeronfinity.cpfy.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.zeronfinity.cpfy.databinding.FragmentFilterBinding
import com.zeronfinity.cpfy.view.adapter.AdapterPlatformFilters
import com.zeronfinity.cpfy.view.adapter.AdapterPlatformFilters.PlatformFilterClickListener
import com.zeronfinity.cpfy.viewmodel.ContestListViewModel
import com.zeronfinity.cpfy.viewmodel.FilterDialogFragmentViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class FilterDialogFragment
    : DialogFragment(), PlatformFilterClickListener {
    companion object {
        const val LOG_TAG = "FilterDialogFragment"
        fun newInstance() = FilterDialogFragment()
    }

    private var _binding: FragmentFilterBinding? = null
    private val binding get() = _binding!!

    private lateinit var filterDialogViewModel: FilterDialogFragmentViewModel
    private lateinit var contentListViewModel: ContestListViewModel

    @Inject
    lateinit var adapterPlatformFilters: AdapterPlatformFilters

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFilterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rvPlatforms.adapter = adapterPlatformFilters
        binding.rvPlatforms.layoutManager = GridLayoutManager(activity, 3)
        binding.rvPlatforms.setHasFixedSize(true)

        adapterPlatformFilters.setPlatformFilterClickListener(this)

        contentListViewModel = ViewModelProvider(requireActivity()).get(ContestListViewModel::class.java)
        observeContestListViewModel()

        filterDialogViewModel = ViewModelProvider(this).get(FilterDialogFragmentViewModel::class.java)
        observeFilterDialogViewModel()
        filterDialogViewModel.loadTimeFilterButtonTexts()
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )
    }

    private fun observeContestListViewModel() {
        contentListViewModel.contestListUpdatedLiveData.observe(viewLifecycleOwner, Observer {
            adapterPlatformFilters.refreshPlatformList()
        })
    }

    private fun observeFilterDialogViewModel() {
        filterDialogViewModel.startTimeLowerBoundLiveData.observe(viewLifecycleOwner, Observer {
            binding.btnStartTimeLowerBound.text = it
        })

        filterDialogViewModel.startTimeUpperBoundLiveData.observe(viewLifecycleOwner, Observer {
            binding.btnStartTimeUpperBound.text = it
        })

        filterDialogViewModel.endTimeLowerBoundLiveData.observe(viewLifecycleOwner, Observer {
            binding.btnEndTimeLowerBound.text = it
        })

        filterDialogViewModel.endTimeUpperBoundLiveData.observe(viewLifecycleOwner, Observer {
            binding.btnEndTimeUpperBound.text = it
        })

        filterDialogViewModel.durationLowerBoundLiveData.observe(viewLifecycleOwner, Observer {
            binding.btnDurationLowerBound.text = it
        })

        filterDialogViewModel.durationUpperBoundLiveData.observe(viewLifecycleOwner, Observer {
            binding.btnDurationUpperBound.text = it
        })
    }

    override fun onPlatformFilterClick() {
        contentListViewModel.updateContestList()
    }
}
