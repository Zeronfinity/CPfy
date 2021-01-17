package com.zeronfinity.cpfy.view.adapter

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.zeronfinity.core.entity.License
import com.zeronfinity.core.logger.logD
import com.zeronfinity.cpfy.databinding.ItemLicenseBinding
import javax.inject.Inject

class AdapterLicenseList @Inject constructor(
    private val activity: Activity,
) : RecyclerView.Adapter<AdapterLicenseList.ViewHolder>() {

    private var licenseList = ArrayList<License>()

    inner class ViewHolder(
        private val binding: ItemLicenseBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(license: License) {
            binding.tvLibraryName.text = license.libraryName
            binding.tvAuthorName.text = license.authorName
            binding.tvLicenseType.text = license.licenseType

            binding.cardView.setOnClickListener {
                val launchUrlIntent = Intent(Intent.ACTION_VIEW, Uri.parse(license.licenseUrl))
                try {
                    activity.startActivity(launchUrlIntent)
                } catch (ex: ActivityNotFoundException) {
                    activity.let {
                        Toast.makeText(
                            it,
                            "No external app found for opening url!",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: ItemLicenseBinding =
            ItemLicenseBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(licenseList[position])
    }

    override fun getItemCount(): Int {
        return licenseList.size
    }

    fun refreshLicenseList(list: List<License>) {
        logD("refreshLicenseList() started")
        licenseList.clear()
        licenseList.addAll(list)
        notifyDataSetChanged()
    }
}
