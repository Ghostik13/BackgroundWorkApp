package com.example.workmanager.main

import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.BatteryManager
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.workmanager.R

class BatteryWorker(appContext: Context, workerParams: WorkerParameters) :
    Worker(appContext, workerParams) {

    override fun doWork(): Result {
        notificationCreate()
        return Result.success()
    }

    private fun notificationCreate() {
        val notification = NotificationCompat.Builder(applicationContext, CHANNEL_ID)
            .setContentTitle("Battery Charge")
            .setContentText("The charge is " + batteryLevel())
            .setSmallIcon(R.drawable.ic_baseline_battery_charging_full_24)
            .setPriority(NotificationCompat.PRIORITY_HIGH)

        with(NotificationManagerCompat.from(applicationContext)) {
            notify(1, notification.build())
        }
    }

    private fun batteryLevel(): String {
        val intent: Intent? =
            applicationContext.registerReceiver(null, IntentFilter(Intent.ACTION_BATTERY_CHANGED))
        val level = intent?.getIntExtra(BatteryManager.EXTRA_LEVEL, 0)
        val scale = intent?.getIntExtra(BatteryManager.EXTRA_SCALE, 100)
        val percent = level!! * 100 / scale!!
        return "$percent%"
    }
}