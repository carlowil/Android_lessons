package ru.mirea.systemintentsapp

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        val phoneButton : Button = findViewById(R.id.phoneButton)
        val openBrowserButton : Button = findViewById(R.id.openBrowserButton)
        val openMapButton : Button = findViewById(R.id.openMapButton)

        phoneButton.setOnClickListener(::onClickCall)
        openBrowserButton.setOnClickListener(::onClickOpenBrowser)
        openMapButton.setOnClickListener(::onClickOpenMaps)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun onClickCall(view: View?) {
        val intent = Intent(Intent.ACTION_DIAL)
        intent.setData(Uri.parse("tel:89811112233"))
        startActivity(intent)
    }

    private fun onClickOpenBrowser(view: View?) {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.setData(Uri.parse("http://developer.android.com"))
        startActivity(intent)
    }

    private fun onClickOpenMaps(view: View?) {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.setData(Uri.parse("geo:55.749479,37.613944"))
        startActivity(intent)
    }
}