package ru.mirea.dialog

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        val dateDialogButton : Button = findViewById(R.id.dateDialogButton)
        val timeDialogButton : Button = findViewById(R.id.timeDialogButton)
        val progressDialogButton : Button = findViewById(R.id.progressDialogButton)


        dateDialogButton.setOnClickListener {
            val myDialog = MyDateDialogFragment(this, { view, year, month, dayOfMonth ->
                Toast.makeText(this, "Ваше дата: ${year}-${month}-${dayOfMonth}", Toast.LENGTH_LONG).show()
            },
                2024,
                1,
                1)
            myDialog.show()
        }

        timeDialogButton.setOnClickListener {
            val myDialog = MyTimeDialogFragment(this, { view, hourOfDay, minute ->
                                                Toast.makeText(this, "Ваше время: ${hourOfDay}-${minute}", Toast.LENGTH_LONG).show()
            },
                24,
                60,
                true)
            myDialog.show()
        }

        progressDialogButton.setOnClickListener {
            val myDialog = MyProgressDialogFragment(this)
            myDialog.setMessage("Привет! Я гружусь")
            myDialog.setTitle("Правда гружусь!")
            myDialog.show()
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

    }
}