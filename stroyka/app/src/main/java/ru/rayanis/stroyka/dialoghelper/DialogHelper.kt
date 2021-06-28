package ru.rayanis.stroyka.dialoghelper

import android.app.AlertDialog
import android.view.View
import ru.rayanis.stroyka.MainActivity
import ru.rayanis.stroyka.R
import ru.rayanis.stroyka.accounthelper.AccountHelper
import ru.rayanis.stroyka.databinding.SignDialogBinding

class DialogHelper(act: MainActivity) {
    private val act = act
    private val accHelper = AccountHelper(act)

    fun createSignDialog(index: Int){
        val builder = AlertDialog.Builder(act)
        val b = SignDialogBinding.inflate(act.layoutInflater)
        val view = b.root
        builder.setView(view)

        setDialogState(state, b)

        val dialog = builder.create()
        b.btSignUpIn.setOnClickListener {
            dialog.dismiss()
            if (index == DialogConst.SIGN_UP_STATE) {
                accHelper.signUpWithEmail(b.edSignEmail.text.toString(),
                    b.edSignPassword.text.toString())
            } else {
                accHelper.signInWithEmail(b.edSignEmail.text.toString(),
                    b.edSignPassword.text.toString())
            }
        }
        dialog.show()
    }
}