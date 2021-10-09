package com.example.clock2.fragments

import android.annotation.SuppressLint
import android.app.*
import android.content.Context
import android.content.Context.ALARM_SERVICE
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.clock2.R
import com.example.clock2.adapter.ClockAdapter
import com.example.clock2.adapter.RandomUtil
import com.example.clock2.databinding.FragmentClockBinding
import com.example.clock2.broadcastreceiver.AlarmReceiver
import com.example.clock2.model.ClockData
import com.example.clock2.model.ClockViewModel
import com.google.android.material.timepicker.MaterialTimePicker
import java.util.*
import kotlin.math.roundToInt


class ClockFragment : Fragment(), ClockAdapter.OnItemClickListener{

    private lateinit var binding: FragmentClockBinding
    private val clockViewModel: ClockViewModel by viewModels<ClockViewModel>()
    private val adapter: ClockAdapter by lazy { ClockAdapter(this) }
    lateinit var alarm: SetAlarm


    @SuppressLint("UseRequireInsteadOfGet")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentClockBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        alarm = SetAlarm(context!!)
        binding.floatingActionButton.setOnClickListener{
            findNavController().navigate(R.id.action_clockFragment_to_addClockFragment)
        }

        setUpRecyclerView()

        clockViewModel.getAllData.observe(viewLifecycleOwner, { data ->
            adapter.setData(data)
        })

        return binding.root
    }

    private fun setUpRecyclerView(){
        val recyclerview = binding.recyclerView
        recyclerview.adapter = adapter
        recyclerview.layoutManager =
            StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL)
        swipeToDelete(recyclerview)

    }


    private fun swipeToDelete(recyclerView: RecyclerView){
        val swipeToDeleteCallback = object : SwipeToDelete(){
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val deletedItem = adapter.clockList[viewHolder.adapterPosition]
                val builder = AlertDialog.Builder(context, R.style.AlertDialog)
                builder.setPositiveButton("Có"){  _, _ ->
                    Toast.makeText(context, "Xóa thành công!", Toast.LENGTH_SHORT).show()
                    clockViewModel.deleteData(deletedItem)
                    //adapter.notifyItemRemoved(viewHolder.adapterPosition)
                }
                builder.setNegativeButton("Không") { _, _ ->
                    adapter.notifyDataSetChanged()
                }
                builder.setTitle("Bạn có chắc chắn muốn xóa không?")
                builder.create().show()

            }
        }
        val itemTouchHelper = ItemTouchHelper(swipeToDeleteCallback)
        itemTouchHelper.attachToRecyclerView(recyclerView)
    }
    override fun onItemClick(position: Int, OnSwith: Boolean) {
        val clockData = adapter.clockList[position]
        clockData.active = OnSwith
        Log.e("switch", clockData.active.toString())
        clockViewModel.updateData(clockData)
        if(clockData.active && clockData.clock_timeInMillis >= Calendar.getInstance().timeInMillis)
            alarm.setExactAlarm(clockData.clock_timeInMillis, clockData)
    }

    override fun onViewClick(position: Int) {
        var clockData = adapter.clockList[position]
        val action = ClockFragmentDirections.actionClockFragmentToUpDateClockFragment(clockData)
        findNavController().navigate(action)
    }

}