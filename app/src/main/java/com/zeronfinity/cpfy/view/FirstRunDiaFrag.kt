package com.zeronfinity.cpfy.view

import android.os.Bundle
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.method.LinkMovementMethod
import android.text.style.URLSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.text.color
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.zeronfinity.core.logger.logD
import com.zeronfinity.cpfy.R
import com.zeronfinity.cpfy.databinding.FragmentFirstRunBinding
import com.zeronfinity.cpfy.view.FirstRunDiaFragDirections.Companion.actionFirstRunDiaFragToNotificationFragment
import com.zeronfinity.cpfy.view.FirstRunDiaFragDirections.Companion.actionFirstRunDiaFragToWebViewFragment
import com.zeronfinity.cpfy.view.base.BaseDialogFragment
import com.zeronfinity.cpfy.viewmodel.ContestListViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FirstRunDiaFrag : BaseDialogFragment() {
    private var _binding: FragmentFirstRunBinding? = null
    private val binding get() = _binding!!

    private lateinit var contestListViewModel: ContestListViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFirstRunBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        contestListViewModel = ViewModelProvider(requireActivity()).get(ContestListViewModel::class.java)
        observeContestListViewModel()

        val spannableStringBuilder = SpannableStringBuilder()
            .append(getString(R.string.first_run_web_view_explanation_1st_part))

        context?.let {
            spannableStringBuilder.color(ContextCompat.getColor(it, R.color.primaryDarkColor)) {
                append(
                    getString(R.string.clist_by),
                    URLSpan(getString(R.string.clist_base_url)),
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )
            }
        } ?: run {
            spannableStringBuilder.append(
                getString(R.string.clist_by),
                URLSpan(getString(R.string.clist_base_url)),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }

        spannableStringBuilder.append(getString(R.string.first_run_web_view_explanation_last_part))

        binding.tvWebView.text = spannableStringBuilder
        binding.tvWebView.movementMethod = LinkMovementMethod.getInstance()

        binding.btnWebView.setOnClickListener {
            val action = actionFirstRunDiaFragToWebViewFragment(
                urlStringArg = getString(R.string.clist_login_url),
                errorMsgArg = false
            )

            findNavController().safeNavigate(action)
        }

        binding.btnNotification.setOnClickListener {
            findNavController().safeNavigate(actionFirstRunDiaFragToNotificationFragment())
        }

        binding.btnOk.setOnClickListener {
            contestListViewModel.setFirstRun(false)
            findNavController().popBackStack()
        }
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )
    }

    private fun observeContestListViewModel() {
        contestListViewModel.clistWebViewLiveDataEv.observe(viewLifecycleOwner, {
            it.getContentIfNotHandled()?.let {
                logD("clistWebViewLiveDataEv: creating error toast")
                activity?.let { fragmentActivity ->
                    Toast.makeText(fragmentActivity.applicationContext, getString(R.string.web_view_required_error_msg), Toast.LENGTH_LONG)
                        .show()
                }
            }
        })
    }
}
