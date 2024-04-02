package ru.mirea.intentapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.text.SimpleDateFormat
import java.util.Date

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        val sendButton = findViewById<Button>(R.id.sendButton)

        val dateInMillis = System.currentTimeMillis()
        val format = "yyyy-MM-dd HH:mm:ss"
        val dateString = SimpleDateFormat(format).format(Date(dateInMillis))

        sendButton.setOnClickListener {
            val intent = Intent(this, SecondActivity::class.java)
            intent.putExtra("data", dateString)
            startActivity(intent)
        }


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}