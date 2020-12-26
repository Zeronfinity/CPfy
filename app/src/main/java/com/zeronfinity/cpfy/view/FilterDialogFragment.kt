package com.zeronfinity.cpfy.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.zeronfinity.cpfy.R
import com.zeronfinity.cpfy.databinding.ActivityMainBinding
import com.zeronfinity.cpfy.databinding.FragmentFilterBinding
import com.zeronfinity.cpfy.view.adapter.AdapterContestList
import com.zeronfinity.cpfy.view.adapter.AdapterPlatformFilters
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

@AndroidEntryPoint
class FilterDialogFragment: DialogFragment() {
    companion object {
        const val LOG_TAG = "FilterDialogFragment"
        fun newInstance() = FilterDialogFragment()
    }

    private var _binding: FragmentFilterBinding? = null
    private val binding get() = _binding!!

    @Inject lateinit var adapterPlatformFilters: AdapterPlatformFilters

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

        binding.rvPlatforms.adapter = adapterPlatformFilters
        binding.rvPlatforms.layoutManager = GridLayoutManager(activity, 3)
        binding.rvPlatforms.setHasFixedSize(true)

        setTimeFilterButtonTexts()
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )
    }

    fun setTimeFilterButtonTexts() {
        binding.btnStartTimeLowerBound.text = SimpleDateFormat(
            "dd-MM-yy\nHH:mm",
            Locale.getDefault()
        ).format(Date())

        binding.btnStartTimeUpperBound.text = SimpleDateFormat(
            "dd-MM-yy\nHH:mm",
            Locale.getDefault()
        ).format(Date())

        binding.btnEndTimeLowerBound.text = SimpleDateFormat(
            "dd-MM-yy\nHH:mm",
            Locale.getDefault()
        ).format(Date())

        binding.btnEndTimeUpperBound.text = SimpleDateFormat(
            "dd-MM-yy\nHH:mm",
            Locale.getDefault()
        ).format(Date())

        binding.btnDurationLowerBound.text = SimpleDateFormat(
            "dd-MM-yy\nHH:mm",
            Locale.getDefault()
        ).format(Date())

        binding.btnDurationUpperBound.text = SimpleDateFormat(
            "dd-MM-yy\nHH:mm",
            Locale.getDefault()
        ).format(Date())
    }
}
