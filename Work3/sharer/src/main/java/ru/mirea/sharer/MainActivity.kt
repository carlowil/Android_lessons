package ru.mirea.sharer

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

//        val newIntent = Intent(Intent.ACTION_SEND)
//        newIntent.type = "*/*"
//        newIntent.putExtra(Intent.EXTRA_TEXT, "MIREA")
//        startActivity(Intent.createChooser(newIntent, "Выбор за вами!"))

        val newIntent = Intent(Intent.ACTION_PICK)
        newIntent.type = "*/*"

        val callback: ActivityResultCallback<ActivityResult> =
            ActivityResultCallback { result ->
                if (result.resultCode == RESULT_OK) {
                    val data = result.data
                    Log.d(MainActivity::class.java.getSimpleName(), "Data:" + data!!.dataString)
                }
            }
        val imageActivityResultLauncher = registerForActivityResult (
            ActivityResultContracts.StartActivityForResult(),
            callback
        )
        imageActivityResultLauncher.launch(newIntent)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}