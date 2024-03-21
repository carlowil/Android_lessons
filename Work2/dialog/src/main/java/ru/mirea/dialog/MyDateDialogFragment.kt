package ru.mirea.dialog

import android.app.DatePickerDialog
import android.content.Context
import android.content.DialogInterface

class MyDateDialogFragment(
    context: Context,
    listener: OnDateSetListener?,
    year: Int,
    month: Int,
    dayOfMonth: Int
) : DatePickerDialog(context, listener, year, month, dayOfMonth) {
    override fun onClick(dialog: DialogInterface, which: Int) {
        super.onClick(dialog, which)
    }
}