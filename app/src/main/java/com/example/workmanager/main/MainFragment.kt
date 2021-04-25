package com.example.workmanager.main

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.example.workmanager.R
import kotlinx.android.synthetic.main.main_fragment.*
import kotlinx.android.synthetic.main.main_fragment.view.*
import java.lang.String.format
import java.util.*
import java.util.concurrent.TimeUnit


class MainFragment : Fragment() {

    private lateinit var locationManager: LocationManager

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.main_fragment, container, false)

        initChargeStorageButton(view)
        initLocationButton(view)
        locationManager = requireContext().getSystemService(LocationManager::class.java)
        return view
    }

    private fun initChargeStorageButton(view: View) {
        view.charge_and_storage.setOnClickListener {
            val batteryResult =
                PeriodicWorkRequestBuilder<BatteryWorker>(15, TimeUnit.MINUTES).build()
            val storageAvailable =
                PeriodicWorkRequestBuilder<StorageWorker>(15, TimeUnit.MINUTES).build()
            val workers = listOf(batteryResult, storageAvailable)

            WorkManager
                .getInstance(requireContext())
                .enqueue(workers)
        }
    }

    private fun initLocationButton(view: View) {
        view.location.setOnClickListener {
            val cal = Calendar.getInstance()
            val intent = Intent(requireContext(), LocationService::class.java)
            val pendingIntent = PendingIntent.getService(requireContext(), 0, intent, 0)
            val alarm = requireContext().getSystemService(Context.ALARM_SERVICE) as AlarmManager?
            alarm!!.setRepeating(AlarmManager.RTC_WAKEUP, cal.timeInMillis, 15000, pendingIntent)
        }
    }
}
