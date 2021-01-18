package com.zeronfinity.cpfy.view

import android.content.*
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.google.android.gms.oss.licenses.OssLicensesMenuActivity
import com.zeronfinity.core.logger.logD
import com.zeronfinity.cpfy.BuildConfig
import com.zeronfinity.cpfy.R
import com.zeronfinity.cpfy.databinding.FragmentAboutBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AboutFragment : BaseFragment() {
    private var _binding: FragmentAboutBinding? = null
    private val binding get() = _binding!!

    private val clipboard: ClipboardManager by lazy {
        activity?.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        logD("onCreateView started")

        _binding = FragmentAboutBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        logD("onViewCreated started")

        binding.tvVersion.text = getString(R.string.version, BuildConfig.VERSION_NAME, BuildConfig.BUILD_TYPE)

        binding.linearLayoutVersion.setOnClickListener {
            clipboard.setPrimaryClip(ClipData.newPlainText(getString(R.string.cpfy_version_label), binding.tvVersion.text))
            activity?.let {
                Toast.makeText(
                    it,
                    "Copied: ${binding.tvVersion.text}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        binding.linearLayoutDev.setOnClickListener {
            launchUrl(getString(R.string.developer_portfolio_link))
        }

        binding.linearLayoutGitHub.setOnClickListener {
            launchUrl(getString(R.string.github_link_cpfy))
        }

        binding.tvGooglePlayLicenses.setOnClickListener {
            startActivity(Intent(activity, OssLicensesMenuActivity::class.java))
            OssLicensesMenuActivity.setActivityTitle(getString(R.string.google_play_licenses));
        }

        binding.tv3rdPartyLicenses.setOnClickListener {
            val action = AboutFragmentDirections.actionAboutFragmentToLicenseFragment()
            findNavController().safeNavigate(action)
        }
    }

    private fun launchUrl(url: String) {
        val launchUrlIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        try {
            startActivity(launchUrlIntent)
        } catch (ex: ActivityNotFoundException) {
            activity?.let {
                Toast.makeText(
                    it,
                    "No external app found for opening url!",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}
