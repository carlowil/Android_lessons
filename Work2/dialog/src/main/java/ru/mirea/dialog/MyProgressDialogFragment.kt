package ru.mirea.dialog

import android.app.ProgressDialog
import android.content.Context

class MyProgressDialogFragment(context: Context?) : ProgressDialog(context) {
    override fun setMessage(message: CharSequence?) {
        super.setMessage("${message} (MIREA)")
    }
}