package com.example.limit_breaker.auto_task.activities;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutCompat;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.limit_breaker.auto_task.R;
import com.example.limit_breaker.auto_task.customViewMaker.DateTimeViewMaker;
import com.example.limit_breaker.auto_task.fragments.TimePickerFragment;
import com.example.limit_breaker.auto_task.interfaces.OnSetDateTimeListener;
import com.example.limit_breaker.auto_task.utils.AppConditionsInfoUtils;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Set;

public class ConditionsActivity extends AppCompatActivity implements View.OnClickListener {

    FloatingActionMenu floatingConditionsMenu;
    FloatingActionButton floatingSaveBtn;
    HashMap<String,Integer> conditionsMenu;
    TextView tvSelectedAppName;
    TextView tvConditionDetails;
    Button btnCancel;
    LinearLayout AppConditionsContainer;

    int hourOfDay,minute;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conditions);

        conditionsMenu = getConditionsMenu();
        AppConditionsContainer=(LinearLayout)findViewById(R.id.AppConditionsContainer);
        tvSelectedAppName=(TextView)findViewById(R.id.tvSelectedAppName);
        floatingConditionsMenu=(FloatingActionMenu)findViewById(R.id.floatingConditionsMenu);
        floatingSaveBtn=(FloatingActionButton)findViewById(R.id.floatingSaveBtn);
        floatingSaveBtn.setVisibility(View.GONE);
        setFloatingBtns(conditionsMenu);
        String selectedAppName = getIntent().getStringExtra(AppConditionsInfoUtils.APP_NAME);
        tvSelectedAppName.setText(selectedAppName);
    }
    private HashMap<String,Integer> getConditionsMenu(){
        HashMap<String,Integer> conditionsMenu = new HashMap<>();
        conditionsMenu.put(AppConditionsInfoUtils.ADD_TIME,R.drawable.ic_access_time_white_24dp);
        conditionsMenu.put(AppConditionsInfoUtils.ADD_LOCATION,R.drawable.ic_location_on_white_24dp);
        return conditionsMenu;
    }



    private void setFloatingBtns(HashMap<String,Integer>conditionsMenu){

        Set<String> actionKey=conditionsMenu.keySet();
        for(String thisAction : actionKey){

            FloatingActionButton btnAdd = new FloatingActionButton(this);
            btnAdd.setId(conditionsMenu.get(thisAction));
            btnAdd.setImageResource(conditionsMenu.get(thisAction));
            btnAdd.setLabelText(thisAction);
            btnAdd.setLayoutParams(new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT));
            btnAdd.setButtonSize(FloatingActionButton.SIZE_MINI);

            floatingConditionsMenu.addMenuButton(btnAdd);
            btnAdd.setOnClickListener(this);
        }

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.drawable.ic_access_time_white_24dp:
                floatingConditionsMenu.close(true);
                addDateNTimeView();
                break;
            case R.drawable.ic_location_on_white_24dp:
                startActivity(new Intent(this,testSetLocationActivity.class));
                break;
            case R.id.floatingSaveBtn:
                setWakeUpAlarmForApp();
                break;
        }
    }

    private void addDateNTimeView(){
        final DateTimeViewMaker dateTimeMaker = new DateTimeViewMaker(this,AppConditionsContainer);
        dateTimeMaker.setDateTimeListener(new OnSetDateTimeListener() {
            @Override
            public void onSetDate(String myDate) {
                dateTimeMaker.addDateTimeView(myDate);
            }
        });
        dateTimeMaker.timeAndDatePicker(true);

    }

    private void setWakeUpAlarmForApp(){
        String packageName = getIntent().getStringExtra(AppConditionsInfoUtils.PACKAGE_NAME);
        Intent i = getPackageManager().getLaunchIntentForPackage(packageName);
        PendingIntent pi = PendingIntent.getActivity(
                this,
                111,
                i,
                PendingIntent.FLAG_ONE_SHOT
        );
        Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY,hourOfDay);
        c.set(Calendar.MINUTE,minute);
        c.set(Calendar.SECOND,0);
        c.set(Calendar.MILLISECOND,0);
        Log.d("pui", "setWakeUpAlarmForApp: "+c.getTime());

        AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
        am.set(
                AlarmManager.RTC_WAKEUP,
                c.getTimeInMillis(),
                pi
                );
    }

    private void addConditionLayout(int whichCondition,String conditionSelected){
        LinearLayout newCondition= new LinearLayout(this);
        newCondition.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        ));
        newCondition.setOrientation(LinearLayout.VERTICAL);
        TextView conditionHeading = new TextView(this);
        conditionHeading.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        ));
        switch (whichCondition){
            case AppConditionsInfoUtils.DATE_TIME:
                conditionHeading.setText(AppConditionsInfoUtils.SEL_DATE_TIME);
                break;
            case AppConditionsInfoUtils.LOCATION:
                conditionHeading.setText(AppConditionsInfoUtils.SEL_LOCATION);
                break;
        }
        conditionHeading.setTextSize(20);
        conditionHeading.setPadding(10,10,10,10);

        TextView conditionDetails= new TextView(this);
        conditionDetails.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        ));
        conditionDetails.setText(conditionSelected);
        conditionDetails.setTextSize(30);
        conditionDetails.setPadding(10,10,10,10);

        newCondition.addView(conditionHeading);
        newCondition.addView(conditionDetails);
        AppConditionsContainer.addView(newCondition);
    }


}
