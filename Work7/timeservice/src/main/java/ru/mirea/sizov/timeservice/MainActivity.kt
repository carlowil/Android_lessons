package ru.mirea.sizov.timeservice

import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.io.BufferedReader
import java.io.IOException
import java.net.Socket


class MainActivity : AppCompatActivity() {

    private lateinit var dateTimeTextView:TextView
    private lateinit var getTimeButton:Button
    private val host = "time.nist.gov"
    private val port = 13

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        dateTimeTextView = findViewById(R.id.dateTimeTextView)
        getTimeButton = findViewById(R.id.getTimeButton)
        getTimeButton.setOnClickListener {
            val timeTask = GetTimeTask()
            timeTask.execute()
        }
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private inner class GetTimeTask : AsyncTask<Void?, Void?, String>() {
        override fun doInBackground(vararg params: Void?): String {
            var timeResult = ""
            try {
                val socket: Socket = Socket(host, port)
                val reader: BufferedReader = SocketUtils().getReader(socket)
                reader.readLine()
                timeResult = reader.readLine()
                Log.d("MainActivity", timeResult)
                socket.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
            return timeResult
        }

        override fun onPostExecute(result: String) {
            val data = result.split(" ")
            super.onPostExecute(result)
            dateTimeTextView.text = "Date: ${data[1]}\nTime: ${data[2]}"
        }
    }
}