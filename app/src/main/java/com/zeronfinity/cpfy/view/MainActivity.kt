package com.zeronfinity.cpfy.view

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.zeronfinity.cpfy.databinding.ActivityMainBinding
import com.zeronfinity.cpfy.framework.SubApplication
import com.zeronfinity.cpfy.framework.adapter.AdapterContestList
import com.zeronfinity.cpfy.model.UseCases
import com.zeronfinity.cpfy.viewmodel.MainActivityViewModel
import javax.inject.Inject

const val LOG_TAG = "CpfyMainActivity"

class MainActivity: AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainActivityViewModel by viewModels()
    @Inject
    lateinit var useCases: UseCases
    @Inject
    lateinit var adapterContestList: AdapterContestList

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        (application as SubApplication).getApplicationComponent().inject(this)

        binding.rvMainActivity.adapter = adapterContestList
        binding.rvMainActivity.layoutManager = LinearLayoutManager(this)
        binding.rvMainActivity.setHasFixedSize(true)

        observeViewModel()

        viewModel.fetchContestListAndPersist()
    }

    private fun observeViewModel() {
        viewModel.errorToastIncomingLiveData.observe(this, Observer {
            Toast.makeText(applicationContext, it, Toast.LENGTH_SHORT).show()
        })

        viewModel.contestListLiveData.observe(this, Observer {
            binding.rvMainActivity.adapter!!.notifyDataSetChanged()
        })
    }
}
