package com.example.workmanager.ui.main

import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.os.StatFs
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkRequest
import androidx.work.Worker
import com.example.workmanager.R
import kotlinx.android.synthetic.main.main_fragment.view.*
import java.util.concurrent.TimeUnit


class MainFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.main_fragment, container, false)

        val batteryResult =
            PeriodicWorkRequestBuilder<BatteryWorker>(15, TimeUnit.MINUTES).build()
        val storageAvailable =
            PeriodicWorkRequestBuilder<StorageWorker>(15, TimeUnit.MINUTES).build()
        val workers = listOf(batteryResult, storageAvailable)

        WorkManager
            .getInstance(requireContext())
            .enqueue(workers)

        view.message.text = getAvailableInternalMemorySize()
        return view
    }

    private fun getAvailableInternalMemorySize(): String {
        val stat = StatFs(Environment.getDataDirectory().path)
        val bytesAvailable: Long =
            stat.blockSizeLong * stat.availableBlocksLong
        val megAvailable = bytesAvailable / (1024 * 1024)
        return megAvailable.toString()
    }
}
