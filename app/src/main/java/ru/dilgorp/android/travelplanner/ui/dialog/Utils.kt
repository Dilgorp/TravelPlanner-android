package ru.dilgorp.android.travelplanner.ui.dialog

import android.view.View
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import ru.dilgorp.android.travelplanner.R

fun showMessage(
    view: View,
    title: String,
    message: String,
    action: (() -> Unit)? = null
) {
    if(message.isEmpty()){
        return
    }

    Snackbar.make(view, title, Snackbar.LENGTH_INDEFINITE)
        .setAction(R.string.open) {

            MaterialAlertDialogBuilder(view.context)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(R.string.ok) { dialog, _ ->
                    dialog.cancel()
                    if (action != null) {
                        action()
                    }
                }
                .show()

        }.show()
}