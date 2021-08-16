package ru.rayanis.stroyka.dialoghelper

import android.app.Activity
import android.app.AlertDialog
import ru.rayanis.stroyka.databinding.ProgressDialogLayputBinding
import ru.rayanis.stroyka.databinding.SignDialogBinding

object ProgressDialog {
    fun createProgressDialog(act: Activity): AlertDialog{
        val builder = AlertDialog.Builder(act)
        val b = ProgressDialogLayputBinding.inflate(act.layoutInflater)
        val view = b.root
        builder.setView(view)
        val dialog = builder.create()
        dialog.setCancelable(false)
        dialog.show()

        return dialog
    }
}