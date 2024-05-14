package ru.mirea.sizov.mireaproject

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.DialogFragment

class FileWriteDialogFragment : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val filesHandler = FilesHandler()
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            // Get the layout inflater
            val inflater = requireActivity().layoutInflater;

            val view = inflater.inflate(R.layout.fragment_dialog, null)
            // Inflate and set the layout for the dialog
            // Pass null as the parent view because its going in the dialog layout
            builder.setView(view)
                // Add action buttons
                .setPositiveButton("Add",
                    DialogInterface.OnClickListener { dialog, id ->
                        val name = view.findViewById<EditText>(R.id.nameEditText).text.toString()
                        val text = view.findViewById<EditText>(R.id.inputEditText).text.toString()
                        filesHandler.writeFileToExternalStorage(name, filesHandler.encryptString(
                            text
                        )!!)
                        Toast.makeText(requireActivity(), "File saved! $name", Toast.LENGTH_SHORT).show()
                    })
                .setNegativeButton("Cancel",
                    DialogInterface.OnClickListener { dialog, id ->
                        getDialog()?.cancel()
                    })
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

}