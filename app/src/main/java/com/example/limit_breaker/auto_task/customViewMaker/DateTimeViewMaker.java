package com.example.limit_breaker.auto_task.customViewMaker;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import com.example.limit_breaker.auto_task.R;
import com.example.limit_breaker.auto_task.fragments.DatePickerFragment;
import com.example.limit_breaker.auto_task.fragments.TimePickerFragment;
import com.example.limit_breaker.auto_task.interfaces.OnSetDateTimeListener;

/**
 * Created by limit-breaker on 25/7/17.
 */

public class DateTimeViewMaker implements View.OnClickListener {
    private Context mContext;
    private Button btnEditTime, btnCancelTime;
    private TextView tvDateNTime;
    private Spinner frequency;
    private ViewGroup container;
    private ViewGroup child;
    private OnSetDateTimeListener dateTimeListener;
    private LayoutInflater li;


    public DateTimeViewMaker(Context mContext, ViewGroup container) {
        this.mContext = mContext;
        this.container=container;
        li= (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void setDateTimeListener(OnSetDateTimeListener dateTimeListener) {
        Log.d("ROHAN", "setDateTimeListener: "+dateTimeListener);
        this.dateTimeListener = dateTimeListener;
    }

    public void addDateTimeView(String dateTime){
        child= (ViewGroup) li.inflate(R.layout.date_time_view,null);
        Log.d("ROHAN", "addDateTimeView: "+child);
        btnEditTime=(Button) child.findViewById(R.id.btnEditTime);
        btnCancelTime=(Button)child.findViewById(R.id.btnCancelTime);
        tvDateNTime=(TextView)child.findViewById(R.id.tvDateNTime);
        btnCancelTime.setOnClickListener(this);
        btnEditTime.setOnClickListener(this);
        Log.d("ROHAN", "addDateTimeView: "+dateTime);
        tvDateNTime.setText(dateTime);
        container.addView(child);


    }
    public void removeDateTimeView(){
        child.setVisibility(View.GONE);
    }



    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnEditTime:
                timeAndDatePicker(false);
                break;
            case R.id.btnCancelTime:
                removeDateTimeView();
                break;
        }

    }
    public void timeAndDatePicker(boolean isNew){
        DatePickerFragment datePicker = new DatePickerFragment();
        final TimePickerFragment timePicker = new TimePickerFragment();

        datePicker.show(((AppCompatActivity)mContext)
                        .getSupportFragmentManager(),
                "datePicker");
        datePicker.setDateListener(new OnSetDateTimeListener() {
            @Override
            public void onSetDate(String myDate) {
                timePicker.setMyDate(myDate);
                timePicker.show(((AppCompatActivity)mContext)
                                .getSupportFragmentManager(),
                        "timePicker");
            }
        });
        if(isNew) {
            timePicker.setDateTimeListener(new OnSetDateTimeListener() {
                @Override
                public void onSetDate(String myDate) {
                    if (dateTimeListener != null) {
                        dateTimeListener.onSetDate(myDate);
                    }
                }
            });
        }else{
            timePicker.setDateTimeListener(new OnSetDateTimeListener() {
                @Override
                public void onSetDate(String myDate) {
                    tvDateNTime.setText(myDate);
                }
            });
        }


    }
}
