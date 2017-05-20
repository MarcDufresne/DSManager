package net.imatruck.dsmanager.fragments

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatDialogFragment
import android.support.v7.widget.LinearLayoutCompat
import android.widget.EditText

import net.imatruck.dsmanager.R


class EditTaskDialog : AppCompatDialogFragment() {

    interface EditTaskListener {
        fun onDialogConfirmMove(newDestination: String)
        fun onDialogCancelMove()
    }

    internal var mListener: EditTaskListener? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val defaultMargin = activity.resources.getDimension(R.dimen.dialog_margin)

        val container = LinearLayoutCompat(activity)
        val containerParams = LinearLayoutCompat.LayoutParams(
                LinearLayoutCompat.LayoutParams.MATCH_PARENT,
                LinearLayoutCompat.LayoutParams.WRAP_CONTENT)
        container.layoutParams = containerParams

        val newDestinationEditText = EditText(activity)
        val lp = LinearLayoutCompat.LayoutParams(
                0, LinearLayoutCompat.LayoutParams.WRAP_CONTENT, 1.0f)

        lp.setMargins(defaultMargin.toInt(), 0, defaultMargin.toInt(), 0)

        newDestinationEditText.layoutParams = lp

        container.addView(newDestinationEditText)

        val builder = AlertDialog.Builder(activity)

        builder.setTitle(R.string.edit_task_dialog_title)
                .setMessage(R.string.edit_task_dialog_message)
                .setView(container)
                .setPositiveButton(R.string.edit_task_dialog_confirm
                ) { _, _ -> mListener!!.onDialogConfirmMove(newDestinationEditText.text.toString()) }
                .setNegativeButton(R.string.edit_task_dialog_cancel
                ) { _, _ -> mListener!!.onDialogCancelMove() }
                .setOnDismissListener { mListener!!.onDialogCancelMove() }

        return builder.create()
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)

        try {
            mListener = context as EditTaskListener
        } catch (cce: ClassCastException) {
            throw ClassCastException(context!!.toString() + " must implement EditTaskListener")
        }

    }
}
