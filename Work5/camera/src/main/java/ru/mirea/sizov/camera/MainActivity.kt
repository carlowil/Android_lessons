package ru.mirea.sizov.camera

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import ru.mirea.sizov.camera.databinding.ActivityMainBinding
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


class MainActivity : AppCompatActivity() {

    private val REQUEST_CODE_PERMISSION = 100
    private var isWork = false
    private lateinit var imageUri: Uri
    private lateinit var binding : ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        checkPermissions()

        val callback = ActivityResultCallback<ActivityResult> {
            if(it.resultCode == Activity.RESULT_OK) {
                val data = it.data
                binding.imageView.setImageURI(imageUri)
            }
        }
        val cameraActivityResultLauncher: ActivityResultLauncher<Intent> =
            registerForActivityResult(
                ActivityResultContracts.StartActivityForResult(),
                callback
            )
        binding.imageButton.setOnClickListener {
            val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            if (isWork) {
                try {
                    val photoFile = createImageFile()
                    val authorities = applicationContext.packageName + ".fileprovider"
                    imageUri = FileProvider.getUriForFile(
                        this@MainActivity,
                        authorities,
                        photoFile
                    )
                    cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri)
                    cameraActivityResultLauncher.launch(cameraIntent)
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }
    }


    @Throws(IOException::class)
    private fun createImageFile(): File {
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.ENGLISH).format(Date())
        val imageFileName = "IMAGE_" + timeStamp + "_"
        val storageDirectory = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(imageFileName, ".jpg", storageDirectory)
    }


    private fun checkPermissions() {
        val cameraPermissionStatus = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
        val storageReadPermissionStatus = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
        val storagePermissionStatus =
            ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)


        if (cameraPermissionStatus == PackageManager.PERMISSION_GRANTED && storagePermissionStatus
            == PackageManager.PERMISSION_GRANTED && storageReadPermissionStatus == PackageManager.PERMISSION_GRANTED
        ) {
            isWork = true
        } else {
            ActivityCompat.requestPermissions(
                this, arrayOf(
                    Manifest.permission.CAMERA,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ), REQUEST_CODE_PERMISSION
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSION) {
            //	permission	granted
            isWork = (grantResults.size > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED)
        }
    }

}