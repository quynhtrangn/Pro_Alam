package com.example.clock2.model

import android.os.CountDownTimer
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class CountViewModel(
    var hour: Double,
    var minute: Double,
    var timeSum: Double,
    var active : Boolean
): ViewModel() {


}