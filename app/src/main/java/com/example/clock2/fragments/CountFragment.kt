package com.example.clock2.fragments

import android.app.ActivityManager
import android.app.TimePickerDialog
import android.content.*
import android.content.Context.ACTIVITY_SERVICE
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.example.clock2.databinding.FragmentCountBinding
import com.example.clock2.service.CountService
import kotlin.math.roundToInt
class CountFragment : Fragment(){
    lateinit var binding: FragmentCountBinding
    lateinit var serviceCount: Intent
    private var isActive = false
    var timeSum = 0.0
    var hour = 0.0
    var minute = 0.0
    var second = 0.0
//    var ok = true
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreate(savedInstanceState)
        binding = FragmentCountBinding.inflate(inflater, container, false)
        serviceCount = Intent(context, CountService::class.java)
        val sharedPreferences = activity?.getSharedPreferences("countShare", Context.MODE_PRIVATE)
        val lastStarted = sharedPreferences?.getBoolean("started", false)
        isActive = sharedPreferences!!.getBoolean("active", false)
        timeSum = sharedPreferences?.getFloat("timeRunning", 0.0F)!!.toDouble()
        if (timeSum != 0.0 && lastStarted == true) { // truoc khi tat app: time van chay va ko bi stop
            binding.txtTime.text = getTimeStringFromDouble(timeSum)
            binding.timePicker.visibility = View.GONE
            binding.timeLeftLayout.visibility = View.VISIBLE
            binding.btnDelete.isEnabled = true
            binding.btnStartStop.isEnabled = true
            if(isActive== true){
                binding.btnStartStop.text = "Stop"
            }
            else{
                binding.btnStartStop.text = "Start"
            }
        }
        else{
                binding.timePicker.visibility = View.VISIBLE
                binding.timeLeftLayout.visibility = View.GONE
                binding.btnStartStop.text = "Start"
                timeSum = 0.0
        }
        binding.pickHour.maxValue=23
        binding.pickHour.minValue = 0

        binding.pickMinute.maxValue=59
        binding.pickMinute.minValue = 0

        binding.pickSecond.maxValue=59
        binding.pickSecond.minValue = 0
        binding.pickHour.setOnValueChangedListener{ _, _, i2->
            hour = i2.toDouble()
            timeSum = (hour*60*60 + minute*60 +second)
            Log.d("Hour ",hour.toString() )
            binding.btnStartStop.isEnabled = true
        }

        binding.pickMinute.setOnValueChangedListener{ _, _, i2->
            minute = i2.toDouble()
            timeSum = (hour*60*60 + minute*60 +second)
            Log.d("Minute ", minute.toString() )
            binding.btnStartStop.isEnabled = true
        }
        binding.pickSecond.setOnValueChangedListener{ _, _, i2->
            second = i2.toDouble()
            timeSum =(hour*60*60 + minute*60 +second)
            Log.d("second ",second.toString() )
            binding.btnStartStop.isEnabled = true
        }
        pressButton()

        Log.d("timeSume", timeSum.toString())
        requireActivity().registerReceiver(updateTime, IntentFilter(CountService.TIMER_UPDATED))
        return binding.root
    }
    private fun pressButton(){
        binding.btnStartStop.setOnClickListener {
            binding.timePicker.visibility = View.GONE
            binding.timeLeftLayout.visibility = View.VISIBLE
            startStopCount()

        }
        binding.btnDelete.setOnClickListener {
            resetCount()

        }
    }
    private fun resetCount() {

        binding.timePicker.visibility = View.VISIBLE
        binding.timeLeftLayout.visibility = View.GONE

        binding.btnStartStop.isEnabled = false
        binding.btnStartStop.text = "Start"
        isActive = false

        binding.pickHour.value = 0
        binding.pickMinute.value = 0
        binding.pickSecond.value = 0
        hour = 0.0
        minute = 0.0
        second = 0.0
        timeSum = 0.0
        val sharedPreferences = activity?.getSharedPreferences("countShare",
            Context.MODE_PRIVATE
        )
        val editor : SharedPreferences.Editor = sharedPreferences!!.edit()
        editor.putBoolean("started",false)
        editor.putFloat("timeRunning",0.0F)
        editor.apply()
        activity?.stopService(serviceCount)

    }
    @Suppress("DEPRECATION") // Deprecated for third party Services.
    fun <T> Context.isServiceRunning(service: Class<T>) =
        (getSystemService(ACTIVITY_SERVICE) as ActivityManager)
            .getRunningServices(Integer.MAX_VALUE)
            .any { it.service.className == service.name }

    private fun startStopCount() {
        if(isActive){
            stopTimer()
        }
        else
            startTimer()
    }

    private fun startTimer() {
        if(isActive){
            timeSum = binding.txtTime.text.toString().toDouble()
        }
        serviceCount.putExtra(CountService.TIME_EXTRA, timeSum)
        activity?.startService(serviceCount)
        binding.btnStartStop.text = "Stop"
        isActive = true
        isActive = true
        binding.btnDelete.isEnabled = true
        val sharedPreferences = activity?.getSharedPreferences("countShare",
            Context.MODE_PRIVATE
        )
        val editor : SharedPreferences.Editor = sharedPreferences!!.edit()
        editor.putFloat("timeRunning",timeSum.toFloat())
        editor.putBoolean("started",true)
        editor.apply()
    }

    private fun stopTimer() {
        activity?.stopService(serviceCount)
        binding.btnStartStop.text = "Start"
        isActive = false
        val sharedPreferences = activity?.getSharedPreferences("countShare",
            Context.MODE_PRIVATE
        )
        val editor : SharedPreferences.Editor = sharedPreferences!!.edit()
        editor.putFloat("timeRunning",timeSum.toFloat())
        editor.putBoolean("started",true)
        editor.apply()
    }

    private val updateTime: BroadcastReceiver = object : BroadcastReceiver(){
        override fun onReceive(context: Context, intent: Intent) {
            timeSum = intent.getDoubleExtra(CountService.TIME_EXTRA,0.0)

            if(timeSum!=0.0 ){
                binding.btnStartStop.text = "Stop"
                isActive = true
            }
            if(timeSum >= 0.0){
                binding.timeLeftLayout.visibility = View.VISIBLE
                binding.timePicker.visibility = View.GONE
                binding.txtTime.text = getTimeStringFromDouble(timeSum)
            }
            if(binding.pickHour.value.toInt()>0 ||
                binding.pickMinute.value.toInt()>0 ||
                binding.pickMinute.value.toInt()>0){
                binding.btnStartStop.text = "Stop"
                binding.btnStartStop.isEnabled= true
            }
        }

    }

    private fun getTimeStringFromDouble(time: Double): String{
        val resultInt = time.roundToInt()
        val hours = resultInt %86400 /3600
        val minutes = resultInt %86400 %3600/60
        val seconds = resultInt %86400 %3600%60
        return makeTimeString(hours,minutes,seconds)
    }

    private fun makeTimeString(hours: Int, minutes: Int, seconds: Int): String =String.format("%02d:%02d:%02d",hours,minutes,seconds)


}

