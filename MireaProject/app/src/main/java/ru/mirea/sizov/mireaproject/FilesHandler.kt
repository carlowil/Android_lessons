package ru.mirea.sizov.mireaproject

import android.os.Environment
import android.util.Log
import java.io.BufferedReader
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.nio.charset.StandardCharsets

class FilesHandler {

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