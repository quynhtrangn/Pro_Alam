package com.example.clock2.broadcastreceiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import android.widget.Toast
import com.example.clock2.model.ClockData
import com.example.clock2.service.AlarmService
import com.example.clock2.service.RescheduleClockService
import java.util.*

class AlarmReceiver : BroadcastReceiver() {
    private lateinit var clock: ClockData
    companion object{
        var MONDAY = "MONDAY";
        var TUESDAY = "TUESDAY";
        var WEDNESDAY = "WEDNESDAY";
        var THURSDAY = "THURSDAY";
        var FRIDAY = "FRIDAY";
        var SATURDAY = "SATURDAY";
        var SUNDAY = "SUNDAY";
        var RECURRING = "RECURRING";
        var TITLE = "TITLE";
    }
    override fun onReceive(context: Context, intent: Intent) {
        Log.e("on receive", "Hello")
        if(Intent.ACTION_BOOT_COMPLETED == intent.action){
            Toast.makeText(context,"Alarm is rescheduled", Toast.LENGTH_LONG).show()
            startRescheduleAlarmService(context)
        }
        else{
            if(!intent.getBooleanExtra("RECURRING",false)){
                startAlarmService(context,intent)
            }
            else{
                Log.e("receiver", "repeat")
                if(alarmIsToday(intent)){
                    Log.d("prepare for sevice","running")
                    startAlarmService(context,intent)
                }
            }
        }
    }
    private fun startRescheduleAlarmService(context: Context){
        var intentService = Intent(context, RescheduleClockService::class.java)
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            context.startForegroundService(intentService)
        }
        else{
            context.startService(intentService)
        }
    }
    private fun alarmIsToday(intent: Intent): Boolean {
        var calendar = Calendar.getInstance()
        calendar.timeInMillis = System.currentTimeMillis()
        var today:Int = calendar.get(Calendar.DAY_OF_WEEK)
        Log.e("today on receiver", today.toString())
        when(today){
            Calendar.MONDAY -> {
                if(intent.getBooleanExtra(MONDAY,false)) return true
                return false
            }
            Calendar.TUESDAY -> {
                if(intent.getBooleanExtra(TUESDAY,false)) return true
                return false
            }
            Calendar.WEDNESDAY -> {
                if(intent.getBooleanExtra(WEDNESDAY,false)) return true
                return false
            }
            Calendar.THURSDAY -> {
                if(intent.getBooleanExtra(THURSDAY,false)) return true
                return false
            }
            Calendar.FRIDAY -> {
                if(intent.getBooleanExtra(FRIDAY,false)) return true
                return false
            }
            Calendar.SATURDAY -> {
                if(intent.getBooleanExtra(SATURDAY,false)) return true
                return false
            }
            Calendar.SUNDAY -> {
                if(intent.getBooleanExtra(SUNDAY,false)) return true
                return false
            }
        }
        return false
    }



    private fun startAlarmService(context: Context, intent: Intent){
        var intentService = Intent(context, AlarmService::class.java)
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            Log.d("ForeGroungService","running")
            context.startForegroundService(intentService)
        }
        else{
            context.startService(intentService)
        }
    }

}