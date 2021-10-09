package com.example.clock2.fragments

import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.clock2.R
import com.example.clock2.adapter.RandomUtil
import com.example.clock2.databinding.FragmentUpDateClockBinding
import com.example.clock2.model.ClockData
import com.example.clock2.model.ClockViewModel
import java.util.*


class UpDateClockFragment : Fragment() {
    private lateinit var binding: FragmentUpDateClockBinding
    private val args by navArgs<UpDateClockFragmentArgs>()
    private val clockViewModel: ClockViewModel by viewModels<ClockViewModel>()
    var hour =0
    var minute=0
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentUpDateClockBinding.inflate(inflater, container, false)

        viewCurrent()
        binding.btnSave.setOnClickListener {
            saveData()
        }
        binding.btnCancel.setOnClickListener {
            findNavController().navigate(R.id.action_upDateClockFragment_to_clockFragment)
        }
        return binding.root
    }


    @RequiresApi(Build.VERSION_CODES.M)
    private fun saveData() {
        var hours = binding.timePicker.hour.toString()
        var minutes = binding.timePicker.minute.toString()
        if(hours.toInt()<10){
            hours= "0" + hours
        }
        if(minutes.toInt()<10){
            minutes = "0" + minutes
        }
        val cal = Calendar.getInstance()
        Log.d("timepicker hour", binding.timePicker.hour.toString())
        Log.d("timepicker minute", minutes)
        val clock = ClockData(args.clockCurrent.id,
            args.clockCurrent.active,
            hours,
            minutes,
            cal.timeInMillis, args.clockCurrent.clock_RequestCode,
            binding.fragmentCreatealarmCheckMon.isChecked,
            binding.fragmentCreatealarmCheckTue.isChecked,
            binding.fragmentCreatealarmCheckWed.isChecked,
            binding.fragmentCreatealarmCheckThu.isChecked,
            binding.fragmentCreatealarmCheckFri.isChecked,
            binding.fragmentCreatealarmCheckSat.isChecked,
            binding.fragmentCreatealarmCheckSun.isChecked,
            binding.editText.text.toString(),
    binding.fragmentCreatealarmCheckMon.isChecked or
            binding.fragmentCreatealarmCheckTue.isChecked or
            binding.fragmentCreatealarmCheckWed.isChecked or
            binding.fragmentCreatealarmCheckThu.isChecked or
            binding.fragmentCreatealarmCheckFri.isChecked or
            binding.fragmentCreatealarmCheckSat.isChecked or
            binding.fragmentCreatealarmCheckSun.isChecked)
        Log.d("update checked", binding.fragmentCreatealarmCheckThu.isChecked.toString())
        clockViewModel.updateData(clock)
        activity?.let { clock.schedule(it) }
        findNavController().navigate(R.id.action_upDateClockFragment_to_clockFragment)
    }
    @RequiresApi(Build.VERSION_CODES.M)
    private fun viewCurrent() {
        binding.timePicker.setIs24HourView(true)
        binding.fragmentCreatealarmCheckMon.setChecked(args.clockCurrent.monday)
        binding.fragmentCreatealarmCheckTue.setChecked(args.clockCurrent.tuesday)
        binding.fragmentCreatealarmCheckWed.setChecked(args.clockCurrent.wednesday)
        binding.fragmentCreatealarmCheckThu.setChecked(args.clockCurrent.thursday)
        binding.fragmentCreatealarmCheckFri.setChecked(args.clockCurrent.friday)
        binding.fragmentCreatealarmCheckSat.setChecked(args.clockCurrent.saturday)
        binding.fragmentCreatealarmCheckSun.setChecked(args.clockCurrent.sunday)
        binding.timePicker.hour = args.clockCurrent.hour.toInt()
        binding.timePicker.minute = args.clockCurrent.minute.toInt()
        binding.editText.setText(args.clockCurrent.label)
        hour = binding.timePicker.hour
        minute = binding.timePicker.minute
    }

}