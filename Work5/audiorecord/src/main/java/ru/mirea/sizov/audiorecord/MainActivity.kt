package ru.mirea.sizov.audiorecord

import android.Manifest
import android.content.pm.PackageManager
import android.media.MediaPlayer
import android.media.MediaRecorder
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import ru.mirea.sizov.audiorecord.databinding.ActivityMainBinding
import java.io.File
import java.io.IOException


class MainActivity : AppCompatActivity() {
    private val TAG = MainActivity::class.java.getSimpleName()
    private var isWork = false
    private val REQUEST_CODE_PERMISSION = 200
    private lateinit var binding : ActivityMainBinding
    private lateinit var recordFilePath: String
    private lateinit var recordButton: Button
    private lateinit var playButton: Button
    private var recorder: MediaRecorder? = null
    private var player: MediaPlayer? = null
    var isStartRecording = true
    var isStartPlaying = true


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        recordButton = binding.recordButton
        playButton = binding.playButton
        playButton.setEnabled(false)
        recordFilePath = File(
            getExternalFilesDir(Environment.DIRECTORY_MUSIC),
            "/audiorecordtest.3gp"
        ).absolutePath

        checkPermissions()

        recordButton.setOnClickListener {
            if (isStartRecording) {
                recordButton.setText("Stop	recording")
                playButton.setEnabled(false)
                startRecording()
            } else {
                recordButton.setText("Start	recording")
                playButton.setEnabled(true)
                stopRecording()
            }
            isStartRecording = !isStartRecording;
        }

        playButton.setOnClickListener {
            if	(isStartPlaying)	{
                playButton.setText("Stop playing")
                recordButton.setEnabled(false)
                startPlaying()
            }	else	{
                playButton.setText("Start playing")
                recordButton.setEnabled(true)
                stopPlaying()
            }
            isStartPlaying = !isStartPlaying;
        }

    }


    private fun checkPermissions() {
        val audioPermissionStatus = ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)
        val storagePermissionStatus =
            ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)


        if (audioPermissionStatus == PackageManager.PERMISSION_GRANTED && storagePermissionStatus
            == PackageManager.PERMISSION_GRANTED
        ) {
            isWork = true
        } else {
            ActivityCompat.requestPermissions(
                this, arrayOf(
                    Manifest.permission.RECORD_AUDIO,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ), REQUEST_CODE_PERMISSION
            )
        }
    }

    private fun startRecording() {
        recorder = MediaRecorder()
        recorder!!.setAudioSource(MediaRecorder.AudioSource.MIC)
        recorder!!.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
        recorder!!.setOutputFile(recordFilePath)
        recorder!!.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)
        try {
            recorder!!.prepare()
        } catch (e: IOException) {
            Log.e(TAG, "prepare()	failed")
        }
        recorder!!.start()
    }

    private fun stopRecording() {
        this.recorder?.stop()
        this.recorder?.release()
        recorder = null
    }

    private fun startPlaying() {
        player = MediaPlayer()
        try {
            player!!.setDataSource(recordFilePath)
            player!!.prepare()
            player!!.start()
        } catch (e: IOException) {
            Log.e(TAG, "prepare()	failed")
        }
    }

    private fun stopPlaying() {
        player!!.release()
        player = null
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            REQUEST_CODE_PERMISSION -> isWork =
                grantResults[0] == PackageManager.PERMISSION_GRANTED
        }
        if (!isWork) finish()
    }
}