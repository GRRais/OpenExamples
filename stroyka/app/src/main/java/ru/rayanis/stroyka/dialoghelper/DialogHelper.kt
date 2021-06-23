package ru.rayanis.stroyka.dialoghelper

import android.app.AlertDialog
import ru.rayanis.stroyka.MainActivity

class DialogHelper(act: MainActivity) {
    private val act = act
    fun createSignDialog(){
        val builder = AlertDialog.Builder(act)
    }
}