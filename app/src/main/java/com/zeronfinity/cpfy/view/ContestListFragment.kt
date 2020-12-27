package com.zeronfinity.cpfy.view

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.zeronfinity.cpfy.databinding.FragmentContestListBinding
import com.zeronfinity.cpfy.view.adapter.AdapterContestList
import com.zeronfinity.cpfy.viewmodel.ContestListViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ContestListFragment : Fragment() {
    companion object {
        const val LOG_TAG = "ContestListFragment"
        fun newInstance() = ContestListFragment()
    }

    private var _binding: FragmentContestListBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: ContestListViewModel

    @Inject
    lateinit var adapterContestList: AdapterContestList

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentContestListBinding.inflate(inflater, container, false)

        binding.rvContestList.adapter = adapterContestList
        binding.rvContestList.layoutManager = LinearLayoutManager(binding.root.context)
        binding.rvContestList.setHasFixedSize(true)

        setHasOptionsMenu(true)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(requireActivity()).get(ContestListViewModel::class.java)
        observeViewModel()

        viewModel.fetchContestListAndPersist()
    }

    private fun observeViewModel() {
        viewModel.errorToastIncomingLiveData.observe(viewLifecycleOwner, Observer {
            activity?.let{ fragmentActivity ->
                Toast.makeText(fragmentActivity.applicationContext, it, Toast.LENGTH_SHORT).show()
            }
        })

        viewModel.contestListUpdatedLiveData.observe(viewLifecycleOwner, Observer {
            adapterContestList.refreshContestList()
        })
    }
}
