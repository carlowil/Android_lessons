package ru.mirea.sizov.mireaproject

import android.app.Activity
import android.content.Intent
import android.media.MediaPlayer
import android.media.MediaRecorder
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.imageview.ShapeableImageView
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ProfileFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ProfileFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private val TAG = MainActivity::class.java.getSimpleName()
    private lateinit var imageUri: Uri
    private lateinit var recordFilePath: String
    private lateinit var recordButton: FloatingActionButton
    private lateinit var playButton: FloatingActionButton
    private var recorder: MediaRecorder? = null
    private var player: MediaPlayer? = null
    var isStartRecording = true
    var isStartPlaying = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        recordButton = view.findViewById<FloatingActionButton>(R.id.recordButton)
        playButton = view.findViewById<FloatingActionButton>(R.id.playButton)
        playButton.setEnabled(false)
        recordFilePath = File(
            activity?.getExternalFilesDir(Environment.DIRECTORY_MUSIC),
            "/aboutmetest.3gp"
        ).absolutePath

        recordButton.setOnClickListener {
            if (isStartRecording) {
                playButton.setEnabled(false)
                startRecording()
            } else {
                playButton.setEnabled(true)
                stopRecording()
            }
            isStartRecording = !isStartRecording;
        }


        playButton.setOnClickListener {
            if	(isStartPlaying)	{
                playButton.setImageResource(R.drawable.pause_fill0_wght400_grad0_opsz24)
                recordButton.setEnabled(false)
                startPlaying()
            }	else	{
                playButton.setImageResource(R.drawable.play_arrow_fill0_wght300_grad0_opsz24)
                recordButton.setEnabled(true)
                stopPlaying()
            }
            isStartPlaying = !isStartPlaying;
        }

        val profileImage = view.findViewById<ShapeableImageView>(R.id.profileImage)

        val callback = ActivityResultCallback<ActivityResult> {
            if(it.resultCode == Activity.RESULT_OK) {
                val data = it.data
                profileImage.setImageURI(imageUri)
            }
        }

        val cameraActivityResultLauncher: ActivityResultLauncher<Intent> =
            registerForActivityResult(
                ActivityResultContracts.StartActivityForResult(),
                callback
            )

        profileImage.setOnClickListener {
            val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            if ((activity as MainActivity).isWork) {
                try {
                    val photoFile = createImageFile()
                    val authorities = activity?.applicationContext?.packageName + ".fileprovider"
                    imageUri = FileProvider.getUriForFile(
                        requireActivity().baseContext,
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
        super.onViewCreated(view, savedInstanceState)

    }

    private fun startRecording() {
        recorder = MediaRecorder(requireActivity().baseContext)
        recorder!!.setAudioSource(MediaRecorder.AudioSource.MIC)
        recorder!!.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
        recorder!!.setOutputFile(recordFilePath)
        recorder!!.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)
        try {
            recorder!!.prepare()
        } catch (e: IOException) {
            Log.e(TAG, "prepare()	failed")
        }
        recorder!!.start()
    }

    private fun stopRecording() {
        this.recorder?.stop()
        this.recorder?.release()
        recorder = null
    }

    private fun startPlaying() {
        player = MediaPlayer()
        try {
            player!!.setDataSource(recordFilePath)
            player!!.prepare()
            player!!.start()
        } catch (e: IOException) {
            Log.e(TAG, "prepare()	failed")
        }
    }

    private fun stopPlaying() {
        player!!.release()
        player = null
    }

    @Throws(IOException::class)
    private fun createImageFile(): File {
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.ENGLISH).format(Date())
        val imageFileName = "IMAGE_" + timeStamp + "_"
        val storageDirectory = activity?.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(imageFileName, ".jpg", storageDirectory)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ProfileFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ProfileFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}