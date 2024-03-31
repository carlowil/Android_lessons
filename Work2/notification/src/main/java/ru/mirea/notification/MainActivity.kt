package ru.mirea.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {

    private val chanelId = "com.mirea.asd.notification.ANDROID"
    private val permissionCode = 200
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            Log.d("Somethingtocheckpls", "Разрешения получены")
        } else {
            Log.d("Somethingtocheckpls", "Нет разрешений!")
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.POST_NOTIFICATIONS), permissionCode)
        }

        MainActivity::class.java.simpleName
        val sendButton : Button = findViewById(R.id.sendButton)

        sendButton.setOnClickListener {
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                return@setOnClickListener
            }

            val builder = NotificationCompat.Builder(this, chanelId)
                .setContentText("Congratulations!")
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setStyle(NotificationCompat.BigTextStyle()
                    .bigText("Much longer text that cannot fit one\n" +
                            "line..."))
                .setContentTitle("Mirea")

            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(chanelId, "Student Sizov Matvey Alecseevich Notification", importance)
            channel.description = "MIREA CHANNEL"

            val notificationManager = NotificationManagerCompat.from(this)

            notificationManager.createNotificationChannel(channel)
            notificationManager.notify(1, builder.build())

        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

    }
}