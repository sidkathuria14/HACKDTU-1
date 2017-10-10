package com.example.limit_breaker.auto_task.fragments;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import java.text.DateFormat;
import android.widget.DatePicker;
import android.widget.TextView;

import com.example.limit_breaker.auto_task.interfaces.OnSetDateTimeListener;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by limit-breaker on 25/7/17.
 */

public class DatePickerFragment extends DialogFragment
        implements DatePickerDialog.OnDateSetListener {

    OnSetDateTimeListener dateListener;

    public void setDateListener(OnSetDateTimeListener dateListener) {
        this.dateListener = dateListener;
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current date as the default date in the picker
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        // Create a new instance of DatePickerDialog and return it
        return new DatePickerDialog(getActivity(), this, year, month, day);
    }

    public void onDateSet(DatePicker view, int year, int month, int day) {
        // Do something with the date chosen by the user
        Date date = new Date(year-1900,month,day);
        String myDate = DateFormat.getDateInstance(DateFormat.LONG).format(date);
        if(dateListener!=null){
            dateListener.onSetDate(myDate);
        }
    }
}