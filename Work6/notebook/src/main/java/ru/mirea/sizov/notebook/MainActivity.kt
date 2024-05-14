package ru.mirea.sizov.notebook

import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.io.BufferedReader
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.nio.charset.StandardCharsets


class MainActivity : AppCompatActivity() {

    private lateinit var saveButton: Button
    private lateinit var loadButton: Button
    private lateinit var fileNameEditText: EditText
    private lateinit var fileEditText: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        saveButton = findViewById(R.id.writeButton)
        loadButton = findViewById(R.id.loadButton)
        fileNameEditText = findViewById(R.id.fileNameEditText)
        fileEditText = findViewById(R.id.fileTextEditText)

        saveButton.setOnClickListener {
            if(isExternalStorageWritable()) {

                val fileName = fileNameEditText.text.toString()
                val text = fileEditText.text.toString()

                if (fileName.isNotBlank() && text.isNotBlank()) {
                    writeFileToExternalStorage(fileName, text)
                    Toast.makeText(this, "Quote written in file $fileName.txt", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "File name or text is empty!", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Cant write in external storage!", Toast.LENGTH_SHORT).show()
            }
        }

        loadButton.setOnClickListener {
            if(isExternalStorageReadable()) {

                val fileName = fileNameEditText.text.toString()

                if(fileName.isNotBlank()) {
                    val lines = readFileFromExternalStorage(fileName)
                    fileEditText.setText(lines.joinToString(""))
                    Toast.makeText(this, "Read successful!", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "File name is empty!", Toast.LENGTH_SHORT).show()
                }

            } else {
                Toast.makeText(this, "Cant read external storage!", Toast.LENGTH_SHORT).show()
            }
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    fun isExternalStorageWritable(): Boolean {
        val state = Environment.getExternalStorageState()
        return Environment.MEDIA_MOUNTED == state
    }

    fun isExternalStorageReadable(): Boolean {
        val state = Environment.getExternalStorageState()
        return Environment.MEDIA_MOUNTED == state || Environment.MEDIA_MOUNTED_READ_ONLY == state
    }

    fun writeFileToExternalStorage(fileName: String, text: String) {
        val path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS)
        val file = File(path, "$fileName.txt")
        try {
            val fileOutputStream = FileOutputStream(file.absoluteFile)
            val output = OutputStreamWriter(fileOutputStream)
            output.write(text)
            output.close()
        } catch (e: IOException) {
            Log.w("ExternalStorage", "Error	writing	$file", e)
        }
    }

    fun readFileFromExternalStorage(fileName: String) : MutableList<String> {
        val path = Environment.getExternalStoragePublicDirectory(
            Environment.DIRECTORY_DOCUMENTS
        )
        val file = File(path, "$fileName.txt")
        try {
            val fileInputStream = FileInputStream(file.absoluteFile)
            val inputStreamReader = InputStreamReader(fileInputStream, StandardCharsets.UTF_8)
            val lines: MutableList<String> = ArrayList()
            val reader = BufferedReader(inputStreamReader)
            var line = reader.readLine()
            while (line != null) {
                lines.add(line)
                line = reader.readLine()
            }
            Log.w(
                "ExternalStorage",
                String.format("Read	from	file	%s	successful", lines.toString())
            )
            return lines
        } catch (e: Exception) {
            Log.w(
                "ExternalStorage",
                String.format("Read	from	file	%s	failed", e.message)
            )
        }
        return mutableListOf("")
    }

}