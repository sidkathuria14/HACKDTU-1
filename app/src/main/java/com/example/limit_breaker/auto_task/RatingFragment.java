package com.example.limit_breaker.auto_task;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

/**
 * Created by the-limit-breaker on 7/10/17.
 */

public class RatingFragment extends DialogFragment implements DialogInterface.OnClickListener {

    public static final String TAG="pui";
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater li = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = li.inflate(R.layout.layout_rating,null);
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
        dialogBuilder.setView(view);
        return dialogBuilder.create();
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        switch (which){
            case -1:
                Log.i(TAG, "onClick: btn_Ok "+R.string.btn_ok);
                Toast.makeText(getActivity(),"To be implemented",Toast.LENGTH_LONG).show();
                break;
            case -2:
                Log.i(TAG, "onClick: btn_cancel "+R.string.btn_cancel);
                this.getDialog().cancel();
                break;
        }
    }
}
