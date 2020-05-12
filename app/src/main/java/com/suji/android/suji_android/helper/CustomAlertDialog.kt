package com.suji.android.suji_android.helper

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.view.ContextThemeWrapper

class CustomAlertDialog(context: Context) : AlertDialog(context) {
    constructor(context: Context, layoutResId: Int) : this(context)

    private val builder = AlertDialog.Builder(context)
//    private val dialog = builder.create()

    fun setPositiveButton(text: String, listener: DialogInterface.OnClickListener) {
        builder.setPositiveButton(text, listener)
    }

    fun setNegativeButton(text: String, listener: DialogInterface.OnClickListener) {
        builder.setNegativeButton(text, listener)
    }

    fun setNeutralButton(text: String, listener: DialogInterface.OnClickListener) {
        builder.setNeutralButton(text, listener)
    }

    fun setView(layoutResId: Int) {
        builder.setView(layoutResId)
    }

    override fun show() {
        builder.show().dismiss()
    }
}