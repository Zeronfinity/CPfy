package com.zeronfinity.cpfy.view

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.CookieManager
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.zeronfinity.core.logger.logD
import com.zeronfinity.core.logger.logE
import com.zeronfinity.cpfy.R
import com.zeronfinity.cpfy.databinding.FragmentWebViewBinding
import com.zeronfinity.cpfy.viewmodel.CookieViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.net.URL

@AndroidEntryPoint
class WebViewFragment : Fragment() {
    private var _binding: FragmentWebViewBinding? = null
    private val binding get() = _binding!!

    private val args: WebViewFragmentArgs by navArgs()

    private lateinit var cookieViewModel: CookieViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        logD("onCreateView started")

        _binding = FragmentWebViewBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("SetJavaScriptEnabled")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        logD("onViewCreated started")

        cookieViewModel = ViewModelProvider(this).get(CookieViewModel::class.java)

        binding.webView.settings.builtInZoomControls = false
        binding.webView.settings.javaScriptEnabled = true
        binding.webView.settings.loadsImagesAutomatically = true
        binding.webView.settings.userAgentString = getString(R.string.app_name)
        binding.webView.settings.setGeolocationEnabled(true)

        binding.webView.webViewClient = WebViewClient()

        when (args.urlStringArg) {
            getString(R.string.clist_login_url) -> {
                binding.webView.loadUrl(args.urlStringArg)
                Toast.makeText(
                    activity?.applicationContext,
                    "API limit reached!\nSign up or log into your own clist.by account to continue",
                    Toast.LENGTH_LONG
                ).show()
            }
            else -> logE("No url argument found!")
        }

        findNavController().addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.contestListFragment -> {
                    logD("addOnDestinationChangedListener called, destination: contestListFragment")
                    getSessionCookieFromAppCookieManager(getString(R.string.clist_base_url))?.let {
                        cookieViewModel.setCookie(
                            getString(R.string.clist_session_cookie),
                            it
                        )
                    }
                }
                else -> {
                    logD("addOnDestinationChangedListener: destination is not contestListFragment")
                }
            }
        }
    }

    private fun getSessionCookieFromAppCookieManager(url: String): String? {
        val cookieManager: CookieManager = CookieManager.getInstance()
        val rawCookieHeader = cookieManager.getCookie(URL(url).host)
        logD("Raw Cookie Header: [$rawCookieHeader]")

        rawCookieHeader?.let {
            val cookieStrings = rawCookieHeader.split(";", " ").toTypedArray()

            for (cookie in cookieStrings) {
                val parts = cookie.split("=").toTypedArray()
                if (parts[0] == "sessionid") {
                    logD("Session cookie: [$cookie]")
                    return cookie
                }
            }
        }

        return null
    }
}
