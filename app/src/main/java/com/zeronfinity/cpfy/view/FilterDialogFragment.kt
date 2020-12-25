package com.zeronfinity.cpfy.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import com.zeronfinity.cpfy.R
import com.zeronfinity.cpfy.databinding.ActivityMainBinding
import com.zeronfinity.cpfy.databinding.FragmentFilterBinding
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class FilterDialogFragment: DialogFragment() {
    companion object {
        const val LOG_TAG = "FilterDialogFragment"

        fun newInstance() = FilterDialogFragment()
    }

    private var _binding: FragmentFilterBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFilterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnStartTimeLowerBound.text = SimpleDateFormat(
            "dd-MM-yy HH:mm",
            Locale.getDefault()
        ).format(Date())

        binding.btnStartTimeUpperBound.text = SimpleDateFormat(
            "dd-MM-yy HH:mm",
            Locale.getDefault()
        ).format(Date())

        binding.btnEndTimeLowerBound.text = SimpleDateFormat(
            "dd-MM-yy HH:mm",
            Locale.getDefault()
        ).format(Date())

        binding.btnEndTimeUpperBound.text = SimpleDateFormat(
            "dd-MM-yy HH:mm",
            Locale.getDefault()
        ).format(Date())

        binding.btnDurationLowerBound.text = SimpleDateFormat(
            "dd-MM-yy HH:mm",
            Locale.getDefault()
        ).format(Date())

        binding.btnDurationUpperBound.text = SimpleDateFormat(
            "dd-MM-yy HH:mm",
            Locale.getDefault()
        ).format(Date())
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )
    }
}
