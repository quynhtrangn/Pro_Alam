package com.example.clock2.service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import androidx.fragment.app.viewModels
import androidx.lifecycle.LifecycleService
import com.example.clock2.model.*

class RescheduleClockService : Service(){
    override fun onBind(intent: Intent?): IBinder? {
        return null;
    }

    override fun onCreate() {
        super.onCreate()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)
        var clockDao = ClockDatabase.getInstance(applicationContext).clockDao()
        var alarmRepository = ClockViewModel(application)
        alarmRepository.getAllData.value.let {
            if (it != null) {
                for(alarm: ClockData in it){
                    if(alarm.active){
                        alarm.schedule(applicationContext)
                    }
                }
            }
        }
        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
    }

}
