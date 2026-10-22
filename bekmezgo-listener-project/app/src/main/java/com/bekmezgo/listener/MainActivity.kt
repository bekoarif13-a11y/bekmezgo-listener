package com.bekmezgo.listener

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btnStart = findViewById<Button>(R.id.btnStart)
        val btnStop = findViewById<Button>(R.id.btnStop)

        btnStart.setOnClickListener {
            val serviceIntent = Intent(this, BackgroundListenerService::class.java)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                startForegroundService(serviceIntent)
            } else {
                startService(serviceIntent)
            }
            Toast.makeText(this, "Dinleme Başlatıldı", Toast.LENGTH_SHORT).show()
        }

        btnStop.setOnClickListener {
            val serviceIntent = Intent(this, BackgroundListenerService::class.java)
            stopService(serviceIntent)
            Toast.makeText(this, "Dinleme Durduruldu", Toast.LENGTH_SHORT).show()
        }
    }
}