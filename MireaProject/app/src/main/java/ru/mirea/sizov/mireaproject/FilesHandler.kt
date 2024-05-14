package ru.mirea.sizov.mireaproject

import android.os.Environment
import android.util.Base64
import android.util.Log
import java.io.BufferedReader
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.nio.charset.StandardCharsets
import javax.crypto.Cipher
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.PBEKeySpec
import javax.crypto.spec.SecretKeySpec

class FilesHandler {

    private val secretKey = "tK5UTui+DPh8lIlBxya5XVsmeDCoUl6vHhdIESMB6sQ="
    private val salt = "QWlGNHNhMTJTQWZ2bGhpV3U="
    private val iv = "bVQzNFNhRkQ1Njc4UUFaWA=="

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

    fun encryptString(strToEncrypt: String) :  String?
    {
        try
        {
            val ivParameterSpec = IvParameterSpec(Base64.decode(iv, Base64.DEFAULT))

            val factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1")
            val spec =  PBEKeySpec(secretKey.toCharArray(), Base64.decode(salt, Base64.DEFAULT), 10000, 256)
            val tmp = factory.generateSecret(spec)
            val secretKey =  SecretKeySpec(tmp.encoded, "AES")

            val cipher = Cipher.getInstance("AES/CBC/PKCS7Padding")
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivParameterSpec)
            return Base64.encodeToString(cipher.doFinal(strToEncrypt.toByteArray(Charsets.UTF_8)), Base64.DEFAULT)
        }
        catch (e: Exception)
        {
            Log.w("Encrypting", "Error while encrypting: $e")
        }
        return null
    }

    fun decryptString(strToDecrypt : String) : String? {
        try
        {

            val ivParameterSpec =  IvParameterSpec(Base64.decode(iv, Base64.DEFAULT))

            val factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1")
            val spec =  PBEKeySpec(secretKey.toCharArray(), Base64.decode(salt, Base64.DEFAULT), 10000, 256)
            val tmp = factory.generateSecret(spec);
            val secretKey =  SecretKeySpec(tmp.encoded, "AES")

            val cipher = Cipher.getInstance("AES/CBC/PKCS7Padding");
            cipher.init(Cipher.DECRYPT_MODE, secretKey, ivParameterSpec);
            return  String(cipher.doFinal(Base64.decode(strToDecrypt, Base64.DEFAULT)))
        }
        catch (e : Exception) {
            Log.w("Encrypting", "Error while encrypting: $e")
        }
        return null
    }
}
