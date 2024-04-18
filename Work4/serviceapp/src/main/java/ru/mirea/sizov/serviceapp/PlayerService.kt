package ru.mirea.sizov.serviceapp

import android.R.raw
import android.R.mipmap
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.os.IBinder
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat


class PlayerService : Service() {

    private lateinit var mediaPlayer : MediaPlayer
    companion object {
        val CHANNEL_ID = "ForegroundServiceChannel"
    }
    override fun onBind(intent: Intent): IBinder {
        TODO("Return the communication channel to the service.")
    }

    override fun onDestroy() {
        stopForeground(STOP_FOREGROUND_REMOVE)
        mediaPlayer.stop()
        super.onDestroy()
    }

    override fun onCreate() {
        val builder: NotificationCompat.Builder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentText("Playing.... ASTRAL STEP :D")
            .setSmallIcon(R.mipmap.ic_launcher)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setStyle(
                NotificationCompat.BigTextStyle()
                    .bigText("ASTRAL STEP :D (shadowraze.mp3)")
            )
            .setContentTitle("Music	Player")
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel(CHANNEL_ID, "Student Sizov Matvey Alekseevich Notification", importance)
        channel.description = "MIREA Channel"
        val notificationManager = NotificationManagerCompat.from(this)
        notificationManager.createNotificationChannel(channel)
        startForeground(1, builder.build())
        mediaPlayer = MediaPlayer.create(this, R.raw.shadowraze)
        mediaPlayer.isLooping = false
        super.onCreate()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        mediaPlayer.start()
        mediaPlayer.setOnCompletionListener {
            stopForeground(STOP_FOREGROUND_REMOVE)
        }
        return super.onStartCommand(intent, flags, startId)
    }
}