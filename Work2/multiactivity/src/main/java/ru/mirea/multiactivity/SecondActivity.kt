package ru.mirea.multiactivity

import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class SecondActivity : AppCompatActivity() {

    private val tag = "ApplicationLifecycle"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_second)

        val bundleTextView : TextView = findViewById(R.id.bundleText)
        val data = intent.getStringExtra("data")

        bundleTextView.text = data.toString()

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