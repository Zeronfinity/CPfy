package com.zeronfinity.cpfy.view

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.zeronfinity.core.repository.ContestRepository
import com.zeronfinity.core.repository.PlatformRepository
import com.zeronfinity.core.usecase.*
import com.zeronfinity.cpfy.databinding.ActivityMainBinding
import com.zeronfinity.cpfy.framework.adapter.AdapterContestList
import com.zeronfinity.cpfy.model.ContestArrayList
import com.zeronfinity.cpfy.model.PlatformMap
import com.zeronfinity.cpfy.model.UseCases
import com.zeronfinity.cpfy.viewmodel.MainActivityViewModel

const val LOG_TAG = "CpfyMainActivity"

class MainActivity: AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainActivityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val contestRepository = ContestRepository(ContestArrayList())
        val platformRepository = PlatformRepository(PlatformMap())

        val useCases = UseCases(
            AddContestList(contestRepository),
            GetContest(contestRepository),
            GetContestCount(contestRepository),
            RemoveAllContests(contestRepository),
            AddPlatform(platformRepository),
            GetPlatformImageUrl(platformRepository),
            RemoveAllPlatforms(platformRepository)
        )

        binding.rvMainActivity.adapter = AdapterContestList(useCases)
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
