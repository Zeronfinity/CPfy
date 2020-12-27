package com.zeronfinity.cpfy.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.zeronfinity.cpfy.databinding.FragmentFiltersBinding
import com.zeronfinity.cpfy.view.adapter.AdapterPlatformFilters
import com.zeronfinity.cpfy.view.adapter.AdapterPlatformFilters.PlatformFilterClickListener
import com.zeronfinity.cpfy.viewmodel.ContestListViewModel
import com.zeronfinity.cpfy.viewmodel.FiltersViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class FiltersFragment
    : Fragment(), PlatformFilterClickListener {
    companion object {
        const val LOG_TAG = "FilterDialogFragment"
        fun newInstance() = FiltersFragment()
    }

    private var _binding: FragmentFiltersBinding? = null
    private val binding get() = _binding!!

    private lateinit var filtersViewModel: FiltersViewModel
    private lateinit var contentListViewModel: ContestListViewModel

    @Inject
    lateinit var adapterPlatformFilters: AdapterPlatformFilters

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFiltersBinding.inflate(inflater, container, false)
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

        filtersViewModel = ViewModelProvider(this).get(FiltersViewModel::class.java)
        observeFilterDialogViewModel()
        filtersViewModel.loadTimeFilterButtonTexts()
    }

    private fun observeContestListViewModel() {
        contentListViewModel.contestListUpdatedLiveData.observe(viewLifecycleOwner, Observer {
            adapterPlatformFilters.refreshPlatformList()
        })
    }

    private fun observeFilterDialogViewModel() {
        filtersViewModel.startTimeLowerBoundLiveData.observe(viewLifecycleOwner, Observer {
            binding.btnStartTimeLowerBound.text = it
        })

        filtersViewModel.startTimeUpperBoundLiveData.observe(viewLifecycleOwner, Observer {
            binding.btnStartTimeUpperBound.text = it
        })

        filtersViewModel.endTimeLowerBoundLiveData.observe(viewLifecycleOwner, Observer {
            binding.btnEndTimeLowerBound.text = it
        })

        filtersViewModel.endTimeUpperBoundLiveData.observe(viewLifecycleOwner, Observer {
            binding.btnEndTimeUpperBound.text = it
        })

        filtersViewModel.durationLowerBoundLiveData.observe(viewLifecycleOwner, Observer {
            binding.btnDurationLowerBound.text = it
        })

        filtersViewModel.durationUpperBoundLiveData.observe(viewLifecycleOwner, Observer {
            binding.btnDurationUpperBound.text = it
        })
    }

    override fun onPlatformFilterClick() {
        contentListViewModel.updateContestList()
    }
}
