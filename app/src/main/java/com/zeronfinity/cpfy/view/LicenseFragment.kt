package com.zeronfinity.cpfy.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.zeronfinity.core.entity.License
import com.zeronfinity.core.logger.logD
import com.zeronfinity.cpfy.databinding.FragmentLicenseBinding
import com.zeronfinity.cpfy.view.adapter.AdapterLicenseList
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class LicenseFragment : BaseFragment() {
    private var _binding: FragmentLicenseBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var adapterLicenseList: AdapterLicenseList

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        logD("onCreateView started")

        _binding = FragmentLicenseBinding.inflate(inflater, container, false)

        val cRomWhitelistLicense = License(
            "CRomAppWhitelist",
            "WanghongLin",
            "Apache License 2.0",
            "https://github.com/WanghongLin/CRomAppWhitelist/blob/master/LICENSE"
        )

        val clistLicense = License(
            "clist",
            "Aleksey Ropan",
            "Apache License 2.0",
            "https://github.com/aropan/clist/blob/master/LICENSE"
        )

        binding.rvLicenseList.adapter = adapterLicenseList
        binding.rvLicenseList.layoutManager = LinearLayoutManager(binding.root.context)
        binding.rvLicenseList.setHasFixedSize(true)

        adapterLicenseList.refreshLicenseList(listOf(cRomWhitelistLicense, clistLicense))

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        logD("onViewCreated started")
    }
}
