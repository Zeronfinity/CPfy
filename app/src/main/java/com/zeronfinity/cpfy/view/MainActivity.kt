package com.zeronfinity.cpfy.view

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.zeronfinity.cpfy.databinding.ActivityMainBinding
import com.zeronfinity.cpfy.view.adapter.AdapterContestList
import com.zeronfinity.cpfy.model.UseCases
import com.zeronfinity.cpfy.viewmodel.MainActivityViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity: AppCompatActivity() {
    private val LOG_TAG = MainActivity::class.simpleName

    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MainActivityViewModel
    @Inject lateinit var useCases: UseCases
    @Inject lateinit var adapterContestList: AdapterContestList

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Log.d(LOG_TAG, "onCreate started")

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.rvMainActivity.adapter = adapterContestList
        binding.rvMainActivity.layoutManager = LinearLayoutManager(this)
        binding.rvMainActivity.setHasFixedSize(true)

        viewModel = ViewModelProvider(this).get(MainActivityViewModel::class.java)

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
