package ru.mirea.multiactivity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {

    private val tag = "ApplicationLifecycle"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        val startActivityButton : Button = findViewById(R.id.startActivityButton)
        val bundleEditText : TextView = findViewById(R.id.bundleEditText)


        startActivityButton.setOnClickListener {
            val intent = Intent(this, SecondActivity::class.java)
            val myText = bundleEditText.text

            if (myText.isNullOrBlank()) {
                Toast.makeText(this, "No text in EditText!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            intent.putExtra("data", myText.toString())

            startActivity(intent)
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

    }

    override fun onStart() {
        super.onStart()
        Log.i(tag, "on.${this.javaClass}Start")
    }

    override fun onPause() {
        super.onPause()
        Log.i(tag, "on.${this.javaClass}Pause")
    }

    override fun onRestoreInstanceState(
        savedInstanceState: Bundle?,
        persistentState: PersistableBundle?
    ) {
        super.onRestoreInstanceState(savedInstanceState, persistentState)
        Log.i(tag, "on.${this.javaClass}RestoreInstanceState")
    }

    override fun onSaveInstanceState(
        outState: Bundle,
        outPersistentState: PersistableBundle
    ) {
        super.onSaveInstanceState(outState, outPersistentState)
        Log.i(tag, "on.${this.javaClass}SaveInstanceState")
    }

    override fun onPostCreate(
        savedInstanceState: Bundle?,
        persistentState: PersistableBundle?) {
        super.onPostCreate(savedInstanceState, persistentState)
        Log.i(tag, "on.${this.javaClass}PostCreate")
    }

    override fun onPostResume() {
        super.onPostResume()
        Log.i(tag, "on.${this.javaClass}PostResume")
    }

    override fun onRestart() {
        super.onRestart()
        Log.i(tag, "on.${this.javaClass}Restart")
    }

    override fun onStop() {
        super.onStop()
        Log.i(tag, "on.${this.javaClass}Stop")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.i(tag, "on.${this.javaClass}Destroy")
    }


}