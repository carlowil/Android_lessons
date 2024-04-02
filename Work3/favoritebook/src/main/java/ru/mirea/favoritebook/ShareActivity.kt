package ru.mirea.favoritebook

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat


class ShareActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_share)

        val favDevBookButton = findViewById<Button>(R.id.favDevBookButton)
        val favDevBookEditText = findViewById<EditText>(R.id.favDevBookEditText)

        favDevBookButton.setOnClickListener {
            var text = favDevBookEditText.text.toString()
            text = if (text.isNullOrBlank()) {
                val extras = intent.extras
                val bookName = extras!!.getString(MainActivity.KEY)
                "${favDevBookEditText.hint} $bookName"
            } else {
                "${favDevBookEditText.hint} $text"
            }
            val data = Intent()
            data.putExtra(MainActivity.USER_MESSAGE, text)
            setResult(Activity.RESULT_OK, data)
            finish()
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}