package ru.mirea.sizov.internalfilestorage

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException


class MainActivity : AppCompatActivity() {
    private val LOG_TAG = "InternalFileStorageProject"
    private val fileName : String = "mirea.txt"
    private lateinit var textFromFileTextView: TextView
    private lateinit var fileTextEditText : EditText
    private lateinit var writeTextButton : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)


        textFromFileTextView = findViewById(R.id.textFromFileTextView)
        fileTextEditText = findViewById(R.id.fileTextEditText)
        writeTextButton = findViewById(R.id.writeTextButton)


        writeTextButton.setOnClickListener {
            try {
                val string = fileTextEditText.text.toString()
                val outputStream = openFileOutput(fileName, Context.MODE_PRIVATE)
                outputStream.write(string.toByteArray())
                outputStream.close()
            } catch (e:Exception) {
                e.printStackTrace()
            }
        }

        Thread( Runnable {
            run {
                try {
                    Thread.sleep(5000)
                } catch (e:InterruptedException) {
                    e.printStackTrace()
                }
                textFromFileTextView.post(kotlinx.coroutines.Runnable {
                    run {
                        textFromFileTextView.text = getTextFromFile()
                    }
                })
            }
        }).start()


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun getTextFromFile(): String? {
        var fin: FileInputStream? = null
        try {
            fin = openFileInput(fileName)
            val bytes = ByteArray(fin.available())
            fin.read(bytes)
            val text = String(bytes)
            Log.d(LOG_TAG, text)
            return text
        } catch (ex: IOException) {
            Toast.makeText(this, ex.message, Toast.LENGTH_SHORT).show()
        } finally {
            try {
                fin?.close()
            } catch (ex: IOException) {
                Toast.makeText(this, ex.message, Toast.LENGTH_SHORT).show()
            }
        }
        return null
    }
}