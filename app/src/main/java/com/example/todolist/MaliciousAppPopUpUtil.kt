package com.example.todolist

import android.app.Activity
import android.app.AlertDialog
import android.content.Context

object MaliciousAppPopUpUtil {
    // Function that shows pop window to notify user of the presence of malicious applications
    fun showMaliciousAppPopUp(context: Context) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle("Malicious Apps Detected!")
        builder.setMessage("Click here to close the app.")
        builder.setPositiveButton("Close App") { dialog, _ ->
            (context as Activity).finish() // Cast context to Activity and call finish()
            dialog.dismiss()
        }
        builder.setNegativeButton("Cancel") { dialog, _ ->
            dialog.dismiss()
        }
        builder.create().show()
    }
}