package ru.mirea.sizov.serviceapp

import android.Manifest.permission.FOREGROUND_SERVICE
import android.Manifest.permission.FOREGROUND_SERVICE_MEDIA_PLAYBACK
import android.Manifest.permission.POST_NOTIFICATIONS
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import ru.mirea.sizov.serviceapp.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {
    private val permissionCode	=	200
    private lateinit var binding : ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (ContextCompat.checkSelfPermission(
                this,
                POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            Log.d(MainActivity::class.java.getSimpleName().toString(), "Разрешения получены")
        } else {
            Log.d(MainActivity::class.java.getSimpleName().toString(), "Нет разрешений!")
            ActivityCompat.requestPermissions(
                this,
                arrayOf<String>(POST_NOTIFICATIONS, FOREGROUND_SERVICE_MEDIA_PLAYBACK, FOREGROUND_SERVICE),
                permissionCode
            )
        }

        binding.musicPlay.setOnClickListener {
            val serviceIntent = Intent(this, PlayerService::class.java)
            ContextCompat.startForegroundService(this, serviceIntent)
        }

        binding.musicStop.setOnClickListener {
            stopService(
                Intent(this, PlayerService::class.java)
            )
        }

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}