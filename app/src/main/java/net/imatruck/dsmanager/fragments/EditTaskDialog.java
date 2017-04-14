package net.imatruck.dsmanager.fragments;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;
import android.support.v7.widget.LinearLayoutCompat;
import android.widget.EditText;

import net.imatruck.dsmanager.R;


public class EditTaskDialog extends AppCompatDialogFragment {

    public interface EditTaskListener {
        void onDialogConfirmMove(String newDestination);
        void onDialogCancelMove();
    }

    EditTaskListener mListener;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        float defaultMargin = getActivity().getResources().getDimension(R.dimen.dialog_margin);

        final LinearLayoutCompat container = new LinearLayoutCompat(getActivity());
        LinearLayoutCompat.LayoutParams containerParams = new LinearLayoutCompat.LayoutParams(
                LinearLayoutCompat.LayoutParams.MATCH_PARENT,
                LinearLayoutCompat.LayoutParams.WRAP_CONTENT);
        container.setLayoutParams(containerParams);

        final EditText newDestinationEditText = new EditText(getActivity());
        LinearLayoutCompat.LayoutParams lp = new LinearLayoutCompat.LayoutParams(
                0, LinearLayoutCompat.LayoutParams.WRAP_CONTENT, 1.0f);

        lp.setMargins((int) defaultMargin, 0, (int) defaultMargin, 0);

        newDestinationEditText.setLayoutParams(lp);

        container.addView(newDestinationEditText);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setTitle(R.string.edit_task_dialog_title)
                .setMessage(R.string.edit_task_dialog_message)
                .setView(container)
                .setPositiveButton(R.string.edit_task_dialog_confirm,
                        new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mListener.onDialogConfirmMove(newDestinationEditText.getText().toString());
                    }
                })
                .setNegativeButton(R.string.edit_task_dialog_cancel,
                        new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mListener.onDialogCancelMove();
                    }
                })
                .setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        mListener.onDialogCancelMove();
                    }
                });

        return builder.create();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            mListener = (EditTaskListener) context;
        } catch (ClassCastException cce) {
            throw new ClassCastException(context.toString() + " must implement EditTaskListener");
        }
    }
}
