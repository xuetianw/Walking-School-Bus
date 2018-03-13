package com.thewalkingschoolbus.thewalkingschoolbus.Models;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.constraint.solver.widgets.WidgetContainer;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.thewalkingschoolbus.thewalkingschoolbus.MapFragment;
import com.thewalkingschoolbus.thewalkingschoolbus.R;

import java.util.Objects;

public class EnterGroupNameDialogFragment extends AppCompatDialogFragment {

    View view;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Create the view to show
        view = LayoutInflater.from(getActivity())
                .inflate(R.layout.dialog_enter_group_name, null);

        // Create a button Listener
        DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                // To prevent close on click (regardless of input error), add onClick code in onResume below.
            }
        };

        // Build the alert dialog
        return new AlertDialog.Builder(getActivity())
                //.setTitle("Create new group")
                .setView(view)
                .setPositiveButton(android.R.string.ok, listener)
                .create();
    }

    @Override
    public void onResume()
    {
        super.onResume();
        final AlertDialog alertDialog = (AlertDialog)getDialog();
        final EditText etGroupName = view.findViewById(R.id.etGroupName);
        if(alertDialog != null)
        {
            Button positiveButton = (Button) alertDialog.getButton(Dialog.BUTTON_POSITIVE);
            positiveButton.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    if (Objects.equals(etGroupName.getText().toString(), "")) {
                        Toast.makeText(getActivity(), "Group name cannot be empty.", Toast.LENGTH_SHORT).show();
                    } else if (false) {
                        // TODO: ERROR CHECK FOR DUPLICATE GROUP NAME
                        Toast.makeText(getActivity(), "Group name already exists.", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getActivity(), "Group created!", Toast.LENGTH_SHORT).show();
                        alertDialog.dismiss();
                        MapFragment.createGroup(getActivity(), etGroupName.getText().toString());
                    }
                }
            });
        }
    }
}
