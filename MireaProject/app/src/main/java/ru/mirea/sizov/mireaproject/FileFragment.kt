package ru.mirea.sizov.mireaproject

import android.os.Bundle
import android.os.Environment
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.FragmentTransaction
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.io.BufferedReader
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.nio.charset.StandardCharsets

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [FileFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class FileFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

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
        return inflater.inflate(R.layout.fragment_file, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val addEncryptedFileFab = view.findViewById<FloatingActionButton>(R.id.addEncryptedFileFab)
        val readFileButton = view.findViewById<Button>(R.id.readFileButton)
        val fileTextView = view.findViewById<TextView>(R.id.fileTextView)
        val fileNameEditText = view.findViewById<EditText>(R.id.fileNameEditText)
        val filesHandler = FilesHandler()

        val readable = filesHandler.isExternalStorageReadable()
        val writable = filesHandler.isExternalStorageWritable()

        readFileButton.setOnClickListener {
            val name = fileNameEditText.text.toString()
            if (name.isBlank()) {
                Toast.makeText(requireContext(), "Name need to passed!", Toast.LENGTH_SHORT).show()
            } else {
                if (readable) {
                    fileTextView.text = filesHandler
                        .decryptString(filesHandler.readFileFromExternalStorage(name)
                                                    .joinToString(""))
                }
            }
        }

        addEncryptedFileFab.setOnClickListener {
            if (writable) {
                val dialog = FileWriteDialogFragment()
                val manager = requireActivity().supportFragmentManager
                val transaction: FragmentTransaction = manager.beginTransaction()
                dialog.show(transaction, "dialog")
            }
        }
        super.onViewCreated(view, savedInstanceState)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment FileFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            FileFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}