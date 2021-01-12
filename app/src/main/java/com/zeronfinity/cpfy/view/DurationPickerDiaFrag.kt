package com.zeronfinity.cpfy.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.zeronfinity.cpfy.databinding.FragmentDurationPickerBinding
import com.zeronfinity.cpfy.viewmodel.FiltersViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DurationPickerDiaFrag : DialogFragment() {
    private var _binding: FragmentDurationPickerBinding? = null
    private val binding get() = _binding!!

    private val args: DurationPickerDiaFragArgs by navArgs()

    private lateinit var filtersViewModel: FiltersViewModel

    private var days = 7
    private var hours = 0
    private var minutes = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDurationPickerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        filtersViewModel = ViewModelProvider(requireActivity()).get(FiltersViewModel::class.java)

        parseDuration(filtersViewModel.getDurationFilters(args.filterDurationEnumArg))

        binding.numPickerDays.minValue = 0
        binding.numPickerDays.maxValue = 365
        binding.numPickerDays.value = days

        binding.numPickerHours.minValue = 0
        binding.numPickerHours.maxValue = 23
        binding.numPickerHours.value = hours

        binding.numPickerMinutes.minValue = 0
        binding.numPickerMinutes.maxValue = 59
        binding.numPickerMinutes.value = minutes

        binding.btnCancel.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.btnOk.setOnClickListener {
            var duration = binding.numPickerDays.value
            duration = duration * 24 + binding.numPickerHours.value
            duration = duration * 60 + binding.numPickerMinutes.value
            duration *= 60
            filtersViewModel.setDurationFilters(args.filterDurationEnumArg, duration)

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

    private fun parseDuration(duration: Int) {
        var remainingDuration = duration / 60
        minutes = remainingDuration % 60
        remainingDuration /= 60
        hours = remainingDuration % 24
        remainingDuration /= 24
        days = remainingDuration
    }
}
