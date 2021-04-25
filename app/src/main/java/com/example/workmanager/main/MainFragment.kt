package com.example.workmanager.main

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
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
                PeriodicWorkRequestBuilder<BatteryWorker>(1, TimeUnit.HOURS).build()
            val storageAvailable =
                PeriodicWorkRequestBuilder<StorageWorker>(1, TimeUnit.HOURS).build()
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
            val alarm = requireContext().getSystemService(Context.ALARM_SERVICE) as AlarmManager
            alarm.setInexactRepeating(AlarmManager.RTC_WAKEUP, cal.timeInMillis, 30000, pendingIntent)
        }
    }
}
