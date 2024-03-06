package com.example.buttonclicker

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.CheckBox
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    lateinit var checkBox: CheckBox
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val tvOut = findViewById<TextView>(R.id.tvOut)
        val whoAmI = findViewById<Button>(R.id.btnWhoAmI)
        val itsNotMe = findViewById<Button>(R.id.btnItIsNotMe)
        val checkState = findViewById<CheckBox>(R.id.checkState)
        checkBox = checkState
        whoAmI.setOnClickListener {
            tvOut.text = "Мой номер по списку № X"
            if(!checkState.isChecked) {
                checkState.setChecked(true)
            }
        }

//        itsNotMe.setOnClickListener {
//            tvOut.text = "Это не я сделал"
//        }

    }

    fun itsNotMeEvent(view : View) {
        Toast.makeText(this, "Еще один способ!", Toast.LENGTH_SHORT).show()
        if(checkBox.isChecked) {
            checkBox.setChecked(false)
        }
    }


}