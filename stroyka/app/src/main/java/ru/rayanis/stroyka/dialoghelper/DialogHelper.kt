package ru.rayanis.stroyka.dialoghelper

import android.app.AlertDialog
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

        if (index == DialogConst.SIGN_UP_STATE) {
            b.tvSignTitle.text = act.resources.getString(R.string.sign_up)
            b.btSignUpIn.text = act.resources.getString(R.string.sign_up_action)
        } else {
            b.tvSignTitle.text = act.resources.getString(R.string.sign_in)
            b.btSignUpIn.text = act.resources.getString(R.string.sign_in_action)
        }
        b.btSignUpIn.setOnClickListener {
            if (index == DialogConst.SIGN_UP_STATE) {
                accHelper.signUpWithEmail(b.edSignEmail.text.toString(),
                    b.edSignPassword.text.toString())
            } else {

            }
        }
        builder.setView(view)
        builder.show()
    }
}