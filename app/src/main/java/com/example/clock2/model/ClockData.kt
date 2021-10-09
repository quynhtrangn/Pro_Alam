package com.example.clock2.model

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Parcelable
import android.util.Log
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.clock2.broadcastreceiver.AlarmReceiver
import kotlinx.android.parcel.Parcelize
import java.util.*

@Entity(tableName = "clock_time")
@Parcelize
data class ClockData(
     @PrimaryKey(autoGenerate = true)
     var  id: Int,
     var active: Boolean,
     var hour: String,
     var minute: String,
     var clock_timeInMillis: Long,
     var clock_RequestCode: Int,
     var monday: Boolean,
     var tuesday: Boolean,
     var wednesday: Boolean,
     var thursday: Boolean,
     var friday: Boolean,
     var saturday: Boolean,
     var sunday: Boolean,
     var label: String,
     var recurring: Boolean

)  : Parcelable{
     fun schedule(context: Context){
          Log.e("ALarm", "schedule")
          val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

          val intent= Intent(context, AlarmReceiver::class.java)
          intent.putExtra("HOUR",hour)
          intent.putExtra("MINUTE",minute)
          intent.putExtra("RECURRING",recurring)
          intent.putExtra("MONDAY",monday)
          intent.putExtra("TUESDAY",tuesday)
          intent.putExtra("WEDNESDAY",wednesday)
          intent.putExtra("THURSDAY",thursday)
          intent.putExtra("FRIDAY",friday)
          intent.putExtra("SATURDAY",saturday)
          intent.putExtra("SUNDAY",sunday)
          intent.putExtra("LABEL",label)
          intent.putExtra("ID",id)
          val pendingIntent = id.let { PendingIntent.getBroadcast(context, it.toInt(), intent, 0) };

          var calendar = Calendar.getInstance()
          calendar.timeInMillis = System.currentTimeMillis();
          calendar.set(Calendar.HOUR_OF_DAY, hour.toInt())
          calendar.set(Calendar.MINUTE,minute.toInt())
          calendar.set(Calendar.SECOND,0)

          if(calendar.timeInMillis <= System.currentTimeMillis()){
               calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH) + 1)
          }
          else{
               Log.e("Alarm", "set")
               alarmManager.setExact(
                    AlarmManager.RTC_WAKEUP,
                    calendar.timeInMillis,
                    pendingIntent
               )
          }
          if(!recurring){
               Log.e("Alarm", "!recurring")
               alarmManager.setExact(
                    AlarmManager.RTC_WAKEUP,
                    calendar.timeInMillis,
                    pendingIntent
               )
          }
          else{
               Log.e("Alarm", "repeat")
               val daily: Long = 24*60*60*1000
               alarmManager.setRepeating(
                    AlarmManager.RTC_WAKEUP,
                    calendar.timeInMillis,
                    daily,
                    pendingIntent
               )
          }

          this.active = true
     }
}