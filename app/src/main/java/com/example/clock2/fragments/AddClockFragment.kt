package com.example.clock2.fragments

import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.clock2.R
import com.example.clock2.adapter.RandomUtil
import com.example.clock2.broadcastreceiver.AlarmReceiver
import com.example.clock2.databinding.FragmentAddClockBinding
import com.example.clock2.model.ClockData
import com.example.clock2.model.ClockViewModel
import java.util.*
import kotlin.math.min

class AddClockFragment : Fragment() {
    private val clockViewModel: ClockViewModel by viewModels<ClockViewModel>()
    private lateinit var binding: FragmentAddClockBinding
    private lateinit var intentSent: Intent
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAddClockBinding.inflate(inflater, container, false)
        binding.timePicker.setIs24HourView(true)
        intentSent = Intent(context,AlarmReceiver::class.java)
        binding.btnCancel.setOnClickListener {
            findNavController().navigate(R.id.action_addClockFragment_to_clockFragment2)
        }
        binding.btnSave.setOnClickListener {
            findNavController().navigate(R.id.action_addClockFragment_to_clockFragment2)
            sentData()
        }
        binding.repeat.setOnClickListener {
            if(binding.repeat.isChecked){
                binding.fragmentCreatealarmRecurringOptions.visibility = View.VISIBLE
            }
            else{
                binding.fragmentCreatealarmRecurringOptions.visibility = View.VISIBLE
            }
        }
        setHasOptionsMenu(true)
        return binding.root
    }
    @RequiresApi(Build.VERSION_CODES.M)
    private fun sentData(){
        var hours = binding.timePicker.hour.toString()
        var minutes = binding.timePicker.minute.toString()
        if(hours.toInt()<10){
            hours= "0" + hours.toString()
        }
        if(minutes.toInt()<10){
            minutes = "0"+ minutes.toString()
        }
        val cal = Calendar.getInstance()
        val clock = ClockData(0,true,hours,minutes,cal.timeInMillis,RandomUtil.getRandomInt(),
        binding.fragmentCreatealarmCheckMon.isChecked,
        binding.fragmentCreatealarmCheckTue.isChecked,
        binding.fragmentCreatealarmCheckWed.isChecked,
        binding.fragmentCreatealarmCheckThu.isChecked,
        binding.fragmentCreatealarmCheckFri.isChecked,
        binding.fragmentCreatealarmCheckSat.isChecked,
        binding.fragmentCreatealarmCheckSun.isChecked,
        binding.editText.text.toString(),
        binding.repeat.isChecked)
        clockViewModel.insertData(clock)
        intentSent.putExtra("Clock", clock)
        activity?.let { clock.schedule(it) }
    }

}


