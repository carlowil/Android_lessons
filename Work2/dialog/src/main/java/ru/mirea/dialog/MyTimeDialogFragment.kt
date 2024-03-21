package ru.mirea.dialog

import android.app.TimePickerDialog
import android.content.Context
import android.content.DialogInterface

class MyTimeDialogFragment(
    context: Context?,
    listener: OnTimeSetListener?,
    hourOfDay: Int,
    minute: Int,
    is24HourView: Boolean
) : TimePickerDialog(context, listener, hourOfDay, minute, is24HourView) {
    override fun onClick(dialog: DialogInterface?, which: Int) {
        super.onClick(dialog, which)
    }
}