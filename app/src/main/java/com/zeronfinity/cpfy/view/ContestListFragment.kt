package com.zeronfinity.cpfy.view

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.zeronfinity.core.logger.logD
import com.zeronfinity.cpfy.R
import com.zeronfinity.cpfy.databinding.FragmentContestListBinding
import com.zeronfinity.cpfy.view.adapter.AdapterContestList
import com.zeronfinity.cpfy.viewmodel.ContestListViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ContestListFragment : Fragment() {
    private var _binding: FragmentContestListBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: ContestListViewModel

    @Inject
    lateinit var adapterContestList: AdapterContestList

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        logD("onCreateView started")

        _binding = FragmentContestListBinding.inflate(inflater, container, false)

        binding.rvContestList.adapter = adapterContestList
        binding.rvContestList.layoutManager = LinearLayoutManager(binding.root.context)
        binding.rvContestList.setHasFixedSize(true)

        setHasOptionsMenu(true)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        logD("onViewCreated started")

        viewModel = ViewModelProvider(requireActivity()).get(ContestListViewModel::class.java)
        observeViewModel()

        viewModel.fetchContestList()
        viewModel.fetchPlatformList()
    }

    private fun observeViewModel() {

        viewModel.errorToastIncomingLiveDataEv.observe(viewLifecycleOwner, {
            it.getContentIfNotHandled()?.let {
                logD("errorToastIncomingLiveDataEv: creating error toast")
                activity?.let { fragmentActivity ->
                    Toast.makeText(fragmentActivity.applicationContext, it, Toast.LENGTH_SHORT)
                        .show()
                }
            }
        })

        viewModel.contestListUpdatedLiveDataEv.observe(viewLifecycleOwner, {
            it.getContentIfNotHandled()?.let {
                logD("contestListUpdatedLiveDataEv: refreshing adapterContestList")
                adapterContestList.refreshContestList()
            }
        })

        viewModel.platformListUpdatedLiveDataEv.observe(viewLifecycleOwner, {
            it.getContentIfNotHandled()?.let {
                logD("contestListUpdatedLiveDataEv: refreshing adapterContestList")
                adapterContestList.refreshContestList()
            }
        })

        viewModel.clistWebViewLiveDataEv.observe(viewLifecycleOwner, {
            it.getContentIfNotHandled()?.let {
                logD("clistWebViewLiveDataObserver: navigating to web view fragment")
                val action =
                    ContestListFragmentDirections.actionContestListFragmentToWebViewFragment(
                        getString(R.string.clist_login_url)
                    )
                findNavController().navigate(action)
            }
        })
    }
}
