package com.example.workmanager.main

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.example.workmanager.R
import kotlinx.android.synthetic.main.main_fragment.view.*
import java.util.*
import java.util.concurrent.TimeUnit

class MainFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.main_fragment, container, false)
        initChargeStorageButton(view)
        initLocationButton(view)
        return view
    }

    private fun initChargeStorageButton(view: View) {
        view.charge_and_storage.setOnClickListener {
            val batteryResult =
                OneTimeWorkRequestBuilder<BatteryWorker>().build()
            val storageAvailable =
                OneTimeWorkRequestBuilder<StorageWorker>().build()

            WorkManager
                .getInstance(requireContext())
                .beginWith(batteryResult)
                .then(storageAvailable)
                .enqueue()
        }
    }

    private fun initLocationButton(view: View) {
        view.location.setOnClickListener {
            val intent = Intent(requireContext(), LocationService::class.java)
            requireActivity().startService(intent)
        }
    }
}