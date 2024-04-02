package ru.mirea.favoritebook

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {

    private lateinit var activityResultLauncher: ActivityResultLauncher<Intent>
    companion object {
        val KEY = "book_name"
        val USER_MESSAGE = "MESSAGE"
    }
    private lateinit var favBookTextView: TextView
    private lateinit var openInputButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        favBookTextView = findViewById(R.id.favBookTextView)
        openInputButton = findViewById(R.id.openInput)

        val callback: ActivityResultCallback<ActivityResult> =
            ActivityResultCallback { result ->
                if (result.resultCode == RESULT_OK) {
                    val data = result.data
                    val userBook = data?.getStringExtra(USER_MESSAGE)
                    favBookTextView.text = userBook
                }
            }

        activityResultLauncher = registerForActivityResult (
            ActivityResultContracts.StartActivityForResult(),
            callback
        )

        openInputButton.setOnClickListener(::getInfoAboutBook)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun getInfoAboutBook(view: View?) {
        val intent = Intent(this, ShareActivity::class.java)
        intent.putExtra(KEY, "Анабасис")
        activityResultLauncher.launch(intent)
    }
}