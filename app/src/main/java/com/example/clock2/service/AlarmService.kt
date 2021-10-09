package com.example.clock2.service

import android.app.Activity
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.os.Build
import android.os.IBinder
import android.os.Vibrator
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.example.clock2.R
import com.example.clock2.activity.RingActivity
import com.example.clock2.broadcastreceiver.AlarmReceiver.Companion.TITLE
import com.example.clock2.notifications.AppNotification.Companion.CHANNEL_ID_ALARM

class AlarmService : Service(){
    private lateinit var mediaPlayer: MediaPlayer
    override fun onBind(intent: Intent?): IBinder? {
        return null
    }
    override fun onCreate() {
        super.onCreate()
        mediaPlayer = MediaPlayer.create(this,R.raw.alarm)
        mediaPlayer.isLooping = true


    }
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        val notiIntent = Intent(this, RingActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(this,0,notiIntent,0)
        val notification = NotificationCompat.Builder(this, CHANNEL_ID_ALARM)
            .setContentTitle(intent.getStringExtra(TITLE))
            .setContentText("Ring ring... ring")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()
        mediaPlayer.start()

        startForeground(1,notification)
        Log.d("alarm service", "nothing here")
        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer.stop()
    }

}