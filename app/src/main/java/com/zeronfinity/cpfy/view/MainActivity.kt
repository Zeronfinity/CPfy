package com.zeronfinity.cpfy.view

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.zeronfinity.cpfy.databinding.ActivityMainBinding
import com.zeronfinity.cpfy.viewmodel.MainActivityViewModel

const val LOG_TAG = "CpfyMainActivity"

class MainActivity: AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainActivityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.rvMainActivity.adapter = AdapterContestList(viewModel.useCases)
        binding.rvMainActivity.layoutManager = LinearLayoutManager(this)
        binding.rvMainActivity.setHasFixedSize(true)

        observeViewModel()

        viewModel.fetchContestListFromClist()
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
