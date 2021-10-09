package com.example.clock2.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.example.clock2.databinding.ActivityRingCountBinding
import com.example.clock2.service.CountService
import kotlinx.android.synthetic.main.activity_ring_count.*


class RingCountActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRingCountBinding
    lateinit var intentMain: Intent
    private var handlerAnimation = Handler()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRingCountBinding.inflate(layoutInflater)
        intentMain = Intent(this, MainActivity::class.java)
        setContentView(binding.root)
        startPulse()
        binding.btnDismiss.setOnClickListener {
            val intent = Intent(this, CountService::class.java)
            this.stopService(intent)
            startActivity(intentMain)
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