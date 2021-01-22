package com.zeronfinity.cpfy.view

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Context.CLIPBOARD_SERVICE
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.zeronfinity.core.logger.logD
import com.zeronfinity.cpfy.R
import com.zeronfinity.cpfy.databinding.FragmentClipboardBinding
import com.zeronfinity.cpfy.view.base.BaseFragment
import com.zeronfinity.cpfy.viewmodel.ClipboardViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ClipboardFragment : BaseFragment() {
    private var _binding: FragmentClipboardBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: ClipboardViewModel

    private val clipboard: ClipboardManager by lazy {
        activity?.getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        logD("onCreateView started")

        _binding = FragmentClipboardBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        logD("onViewCreated started")

        viewModel = ViewModelProvider(requireActivity()).get(ClipboardViewModel::class.java)
        observeViewModel()

        viewModel.fetchClipboardText()

        binding.btnCopy.setOnClickListener {
            clipboard.setPrimaryClip(
                ClipData.newPlainText(
                    getString(R.string.contests),
                    binding.etClipboard.text
                )
            )

            activity?.let {
                Toast.makeText(
                    it,
                    "Copied",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        binding.etClipboard.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                activity?.let {
                    val imm: InputMethodManager =
                        it.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(binding.etClipboard.windowToken, 0)
                }
            }
        }
    }

    private fun observeViewModel() {
        viewModel.clipboardTextLiveData.observe(viewLifecycleOwner, {
            logD("clipboardTextLiveData observer -> value: [$it]")
            binding.etClipboard.setText(it)
        })

        viewModel.isProgressVisibleLiveDataEv.observe(viewLifecycleOwner, { it ->
            it.getContentIfNotHandled()?.let { value ->
                logD("isProgressVisibleLiveDataEv observer -> value: [${value}]")
                if (value) {
                    binding.progressBar.visibility = View.VISIBLE
                } else {
                    binding.progressBar.visibility = View.GONE
                }
            }
        })
    }
}
