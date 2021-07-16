package ru.rayanis.stroyka.dialogs

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import ru.rayanis.stroyka.R

class DialogSpinnerHelper {
    fun showSpinnerDialog(context: Context, list: ArrayList<String>) {
        val builder = AlertDialog.Builder(context)
        val rootView = LayoutInflater.from(context).inflate(R.layout.spinner_layout, null)
        builder.setView(rootView)
        builder.show()
    }
}