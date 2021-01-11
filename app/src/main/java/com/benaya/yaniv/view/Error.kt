package com.benaya.yaniv.view

import android.content.Context
import android.widget.Toast

class Error {
    fun Context.toast(message: CharSequence) =
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()

    fun toastError(context: Context,string: String) = context.toast(string)

}