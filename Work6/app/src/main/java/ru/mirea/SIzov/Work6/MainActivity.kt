package ru.mirea.SIzov.Work6

import android.content.Context
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
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

        val groupEditText = findViewById<EditText>(R.id.groupEditText)
        val listNumberEditText = findViewById<EditText>(R.id.listNumberEditText)
        val lovelyFilm = findViewById<EditText>(R.id.lovelyFilmEditText)
        val saveDataButton = findViewById<Button>(R.id.saveDataButton)

        val sharedPref = getSharedPreferences("mirea_settings", Context.MODE_PRIVATE)

        groupEditText.setText(sharedPref.getString("GROUP", "").toString())
        listNumberEditText.setText(sharedPref.getInt("NUMBER", 0).toString())
        lovelyFilm.setText(sharedPref.getString("FILM", "").toString())

        saveDataButton.setOnClickListener {
            val sharedEditor = sharedPref.edit()

            sharedEditor.putString("GROUP", groupEditText.text.toString())
            sharedEditor.putInt("NUMBER", listNumberEditText.text.toString().toInt())
            sharedEditor.putString("FILM", lovelyFilm.text.toString())

            sharedEditor.apply()

            Toast.makeText(this, "Data saved in SharedPreferences!", Toast.LENGTH_SHORT).show()
        }


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}