package com.example.workmanager.main

import android.content.Context
import android.os.Environment
import android.os.StatFs
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.workmanager.R

class StorageWorker(appContext: Context, workerParams: WorkerParameters) :
    Worker(appContext, workerParams) {

    override fun doWork(): Result {
        notificationCreate()
        return Result.success()
    }

    private fun notificationCreate() {
        val notification = NotificationCompat.Builder(applicationContext, CHANNEL_ID)
            .setContentTitle("Storage")
            .setContentText("Available storage is " + getAvailableInternalMemorySize()+" MB")
            .setSmallIcon(R.drawable.ic_baseline_sd_storage_24)
            .setPriority(NotificationCompat.PRIORITY_HIGH)

        with(NotificationManagerCompat.from(applicationContext)) {
            notify(0, notification.build())
        }
    }

    private fun getAvailableInternalMemorySize(): String {
        val stat = StatFs(Environment.getDataDirectory().path)
        val bytesAvailable: Long =
            stat.blockSizeLong * stat.availableBlocksLong
        val megAvailable = bytesAvailable / (1024 * 1024)
        return megAvailable.toString()
    }
}