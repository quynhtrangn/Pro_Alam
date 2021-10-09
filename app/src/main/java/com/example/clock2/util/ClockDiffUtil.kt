package com.example.clock2.util

import androidx.recyclerview.widget.DiffUtil
import com.example.clock2.model.ClockData

class ClockDiffUtil (
    private val oldList: List<ClockData>,
    private val newList: List<ClockData>
    ) : DiffUtil.Callback(){
    override fun getOldListSize(): Int {
        return oldList.size
    }

    override fun getNewListSize(): Int {
        return newList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
       return oldList[oldItemPosition] == newList[newItemPosition]
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].active == newList[newItemPosition].active
                && oldList[oldItemPosition].hour == newList[newItemPosition].hour
                && oldList[oldItemPosition].id == newList[newItemPosition].id
                && oldList[oldItemPosition].minute == newList[newItemPosition].minute
                && oldList[oldItemPosition].monday == newList[newItemPosition].monday
                && oldList[oldItemPosition].tuesday == newList[newItemPosition].tuesday
                && oldList[oldItemPosition].wednesday == newList[newItemPosition].wednesday
                && oldList[oldItemPosition].thursday == newList[newItemPosition].thursday
                && oldList[oldItemPosition].friday == newList[newItemPosition].friday
                && oldList[oldItemPosition].saturday == newList[newItemPosition].saturday
                && oldList[oldItemPosition].sunday == newList[newItemPosition].sunday
    }
}