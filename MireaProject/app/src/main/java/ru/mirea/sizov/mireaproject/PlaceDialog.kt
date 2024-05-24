package ru.mirea.sizov.mireaproject

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment

class PlaceDialog : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val args = arguments
            val title = args!!.getString("title")
            val desc = args.getString("desc")
            val builder = AlertDialog.Builder(it)
                .setTitle(title)
                .setMessage(desc)
                .setPositiveButton("Спасибо") {
                        dialog, id ->  dialog.cancel()
                }
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}