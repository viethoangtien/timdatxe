package com.example.anothertimdatxe.util

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.view.Window
import com.example.anothertimdatxe.R
import kotlinx.android.synthetic.main.dialog_driver_book_request.*

object DialogUtil {
    fun showConfirmDiaglog(context: Context, layout: Int, setCancel: Boolean, resBackground: Int): Dialog {
        var dialog: Dialog = Dialog(context)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(layout)
        dialog.setCancelable(setCancel)
        dialog.window.setBackgroundDrawableResource(resBackground)
        dialog.show()
        return dialog
    }

    fun showConfirmDialog(context: Context, layout: Int, setCancel: Boolean, resBackground: Int, mListener: BaseDialogListener): Dialog {
        var dialog = Dialog(context)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(layout)
        dialog.setCancelable(setCancel)
        dialog.window.setBackgroundDrawableResource(resBackground)
        mListener.onAddDataToDialog(context, dialog)
        dialog.show()
        return dialog
    }

    private fun showMessageDialog(context: Context, msg: Int, title: Int, okListener: DialogInterface.OnClickListener?): AlertDialog {
        var builder = AlertDialog.Builder(context)
        builder.setCancelable(false)
        builder.setTitle(title)
        builder.setIcon(R.mipmap.ic_launcher)
        builder.setMessage(msg)
        builder.setPositiveButton("OK", okListener)
        var alertDialog = builder.create()
        return alertDialog
    }

    fun customBaseAlertDialog(context: Context, title: String, msg: String, cancelable: Boolean, positiveButton: String, negativeButton: String, mListener: BaseAlertDialogListener) {
        var builder = AlertDialog.Builder(context)
        builder.setTitle(title)
        builder.setMessage(msg)
        builder.setCancelable(cancelable)
        builder.setPositiveButton(
                positiveButton,
                { dialog, which -> mListener.onPositiveClick(dialog) }
        )
        builder.setNegativeButton(
                negativeButton,
                { dialog, which -> mListener.onNegativeClick(dialog) }
        )
        val alertDialog = builder.create()
        alertDialog.show()
    }

    fun customBaseAlertDialogNoTitle(context: Context, msg: String, cancelable: Boolean, positiveButton: String, negativeButton: String, mListener: BaseAlertDialogListener) {
        var builder = AlertDialog.Builder(context)
        builder.setMessage(msg)
        builder.setCancelable(cancelable)
        builder.setPositiveButton(
                positiveButton,
                { dialog, which -> mListener.onPositiveClick(dialog) }
        )
        builder.setNegativeButton(
                negativeButton,
                { dialog, which -> mListener.onNegativeClick(dialog) }
        )
        val alertDialog = builder.create()
        alertDialog.show()
    }

    fun customBaseAlertDialogNoNegative(context: Context, title: String, msg: String, cancelable: Boolean, positiveButton: String, mListener: BaseAlertDialogListener) {
        var builder = AlertDialog.Builder(context)
        builder.setMessage(msg)
        builder.setTitle(title)
        builder.setCancelable(cancelable)
        builder.setPositiveButton(
                positiveButton,
                { dialog, which -> mListener.onPositiveClick(dialog) }
        )
        val alertDialog = builder.create()
        alertDialog.show()
    }

    fun showConfirmDialogDriverBookRequest(context: Context, layout: Int, cancelable: Boolean, resBackground: Int, mListener: BaseDialogListener) {
        var dialog = Dialog(context)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(layout)
        dialog.setCancelable(cancelable)
        dialog.window.setBackgroundDrawableResource(resBackground)
        dialog.btn_regis.setOnClickListener {
            mListener.onClickDialog(dialog)
        }
        dialog.imv_close.setOnClickListener {
            dialog.dismiss()
        }
        mListener.onAddDataToDialog(context, dialog)
        dialog.show()
    }

    fun showConfirmDiaglogNotCancel(context: Context, layout: Int, resBackground: Int): Dialog {
        return showConfirmDiaglog(context, layout, false, resBackground)
    }

    fun showConfirmDialogCancel(context: Context, layout: Int, resBackground: Int): Dialog {
        return showConfirmDiaglog(context, layout, true, resBackground)
    }

    fun showMessageDialog(context: Context, msg: Int): AlertDialog {
        return showMessageDialog(context, msg, R.string.alert_message_title, null)
    }

    fun showAlertDialogTitle(context: Context, title: String, msg: String, cancelable: Boolean, positiveButton: String, negativeButton: String, mListener: BaseAlertDialogListener) {
        customBaseAlertDialog(context, title, msg, cancelable, positiveButton, negativeButton, mListener)
    }

    fun showAlertDialogNoTitle(context: Context, msg: String, cancelable: Boolean, positiveButton: String, negativeButton: String, mListener: BaseAlertDialogListener) {
        customBaseAlertDialogNoTitle(context, msg, cancelable, positiveButton, negativeButton, mListener)
    }

    fun showAlertDialogNoNegative(context: Context, title: String, msg: String, cancelable: Boolean, positiveButton: String, mListener: BaseAlertDialogListener) {
        customBaseAlertDialogNoNegative(context, title, msg, cancelable, positiveButton, mListener)
    }


    interface BaseAlertDialogListener {
        fun onPositiveClick(dialogInterface: DialogInterface)
        fun onNegativeClick(dialogInterface: DialogInterface)
    }

    interface BaseDialogListener {
        fun onAddDataToDialog(context: Context, dialog: Dialog)
        fun onClickDialog(dialog: Dialog)
    }
}