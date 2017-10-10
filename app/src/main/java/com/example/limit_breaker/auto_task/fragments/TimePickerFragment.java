package com.example.limit_breaker.auto_task.fragments;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.format.DateFormat;
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.limit_breaker.auto_task.interfaces.OnSetDateTimeListener;


/**
 * Created by limit-breaker on 22/7/17.
 */

public class TimePickerFragment extends DialogFragment
        implements TimePickerDialog.OnTimeSetListener {

    private String myDate;
    private String finaldateTime;

    private OnSetDateTimeListener dateTimeListener;

    public void setDateTimeListener(OnSetDateTimeListener dateTimeListener) {
        this.dateTimeListener = dateTimeListener;
    }

    public String getFinaldateTime() {
        return finaldateTime;
    }

    public void setMyDate(String myDate) {
        this.myDate = myDate;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current time as the default values for the picker
        Calendar c;
        int hour = 0;
        int minute=0;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            c = Calendar.getInstance();
            hour = c.get(Calendar.HOUR_OF_DAY);
            minute=c.get(Calendar.MINUTE);
        }

        // Create a new instance of TimePickerDialog and return it
        return new TimePickerDialog(getActivity(), this, hour, minute,
                DateFormat.is24HourFormat(getActivity()));
    }

    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        // Do something with the time chosen by the user
        String myTime = String.format("%02d:%02d",hourOfDay,minute);
        finaldateTime=myDate+" Time : "+myTime;
        if(dateTimeListener!=null){
            dateTimeListener.onSetDate(finaldateTime);
        }
    }
}