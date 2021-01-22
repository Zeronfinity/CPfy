package com.zeronfinity.cpfy.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.zeronfinity.core.repository.FilterTimeRangeRepository.FilterTimeTypeEnum.END_TIME
import com.zeronfinity.core.repository.FilterTimeRangeRepository.FilterTimeTypeEnum.START_TIME
import com.zeronfinity.cpfy.databinding.FragmentDayPickerBinding
import com.zeronfinity.cpfy.view.base.BaseDialogFragment
import com.zeronfinity.cpfy.viewmodel.FiltersViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DayPickerDiaFrag : BaseDialogFragment() {
    private var _binding: FragmentDayPickerBinding? = null
    private val binding get() = _binding!!

    private val args: DayPickerDiaFragArgs by navArgs()

    private lateinit var filtersViewModel: FiltersViewModel

    private var days = 7

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDayPickerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        filtersViewModel = ViewModelProvider(requireActivity()).get(FiltersViewModel::class.java)

        days = filtersViewModel.getDaysAfterToday(args.filterTimeTypeEnumArg)

        binding.numPickerDays.minValue = 0
        binding.numPickerDays.maxValue = 365
        binding.numPickerDays.value = days

        binding.btnCancel.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.btnOk.setOnClickListener {
            when (args.filterTimeTypeEnumArg) {
                START_TIME -> filtersViewModel.setStartTimeDaysAfterToday(binding.numPickerDays.value)
                END_TIME -> filtersViewModel.setEndTimeDaysAfterToday(binding.numPickerDays.value)
            }

            findNavController().popBackStack()
        }
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )
    }
}
