package com.example.clock2.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.example.clock2.R
import com.example.clock2.databinding.ActivityRingBinding
import com.example.clock2.service.AlarmService
import kotlinx.android.synthetic.main.activity_ring_count.*

class RingActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRingBinding
    private var handlerAnimation = Handler()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        startPulse()
        binding.btnDismiss.setOnClickListener {
            val intent = Intent(this, AlarmService::class.java)
            this.stopService(intent)
            finish()
        }
    }
    private fun startPulse(){
        runnable.run()
    }

    private var runnable = object : Runnable {
        override fun run() {

            imgAnimation1.animate().scaleX(4f).scaleY(4f).alpha(0f).setDuration(1000)
                .withEndAction {
                    imgAnimation1.scaleX = 1f
                    imgAnimation1.scaleY = 1f
                    imgAnimation1.alpha = 1f
                }

            imgAnimation2.animate().scaleX(4f).scaleY(4f).alpha(0f).setDuration(700)
                .withEndAction {
                    imgAnimation2.scaleX = 1f
                    imgAnimation2.scaleY = 1f
                    imgAnimation2.alpha = 1f
                }

            handlerAnimation.postDelayed(this, 1000)
        }
    }
}