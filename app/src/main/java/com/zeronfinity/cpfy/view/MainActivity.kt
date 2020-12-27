package com.zeronfinity.cpfy.view

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.zeronfinity.cpfy.R
import com.zeronfinity.cpfy.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity: AppCompatActivity() {
    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (savedInstanceState == null) {
            supportFragmentManager
                .beginTransaction()
                .add(R.id.main_content, ContestListFragment.newInstance())
                .commit()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_activity_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.miFilter -> {
                FilterDialogFragment.newInstance().show(supportFragmentManager, FilterDialogFragment.LOG_TAG)
                true
            }
            R.id.miClipboard -> {
                Toast.makeText(applicationContext, "Clipboard clicked!", Toast.LENGTH_SHORT).show()
                true
            }
            R.id.miSettings -> {
                Toast.makeText(applicationContext, "Settings clicked!", Toast.LENGTH_SHORT).show()
                true
            }
            R.id.miAbout -> {
                Toast.makeText(applicationContext, "About clicked!", Toast.LENGTH_SHORT).show()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

}
