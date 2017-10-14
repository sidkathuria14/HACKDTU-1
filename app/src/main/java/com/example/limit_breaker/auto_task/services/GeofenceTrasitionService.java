package com.example.limit_breaker.auto_task.services;

import android.app.Activity;
import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.media.AudioManager;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.telephony.SmsManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.limit_breaker.auto_task.MainActivity;
import com.example.limit_breaker.auto_task.R;
import com.example.limit_breaker.auto_task.SmsDeliveredReceiver;
import com.example.limit_breaker.auto_task.SmsSentReceiver;
import com.example.limit_breaker.auto_task.activities.ConditionsActivity;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofenceStatusCodes;
import com.google.android.gms.location.GeofencingEvent;

import java.util.ArrayList;
import java.util.List;


public class GeofenceTrasitionService extends IntentService {

    private static final String TAG = "GeofenceLocation";
    public static final int GEOFENCE_NOTIFICATION_ID = 0;
    public static final String ACTION_SMS_SENT = "com.techblogon.android.apis.os.SMS_SENT_ACTION";
    public GeofenceTrasitionService() {
        super(GeofenceTrasitionService.class.getSimpleName());
    }



    @Override
    protected void onHandleIntent(Intent intent) {
        // Retrieve the Geofencing intent
        GeofencingEvent geofencingEvent = GeofencingEvent.fromIntent(intent);
        Log.d(TAG, "onHandleIntent: ");
//        AudioManager audioManager = (AudioManager) getApplicationContext().getSystemService(Context.AUDIO_SERVICE);
//        audioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);

//        //sms body coming from user input
//        String strSMSBody = "hey I am at location- " + " . It seems its not safe over here.Please remain in touch .";
//        //sms recipient added by user from the activity screen
//        String strReceipentsList = "8076997187";
//        //recipientTextEdit.getText().toString();
//        SmsManager sms = SmsManager.getDefault();
//        List<String> messages = sms.divideMessage(strSMSBody);
//        for (String message : messages) {
//            sms.sendTextMessage(strReceipentsList, null, message, PendingIntent.getBroadcast(
//                    this, 0, new Intent(ACTION_SMS_SENT), 0), null);
//        }




        // Handling errors
        if ( geofencingEvent.hasError() ) {
            String errorMsg = getErrorString(geofencingEvent.getErrorCode() );
            Log.e( TAG, errorMsg );
            return;
        }

        // Retrieve GeofenceTrasition
        int geoFenceTransition = geofencingEvent.getGeofenceTransition();
        // Check if the transition type
        if ( geoFenceTransition == Geofence.GEOFENCE_TRANSITION_ENTER ||
                geoFenceTransition == Geofence.GEOFENCE_TRANSITION_EXIT ) {
            // Get the geofence that were triggered
            List<Geofence> triggeringGeofences = geofencingEvent.getTriggeringGeofences();
            Log.d(TAG, "onHandleIntent: "+triggeringGeofences);
            // Create a detail message with Geofences received
            String geofenceTransitionDetails = getGeofenceTrasitionDetails(geoFenceTransition, triggeringGeofences );
            // Send notification details as a String
            sendNotification( geofenceTransitionDetails );
        }
    }

    // Create a detail message with Geofences received
    private String getGeofenceTrasitionDetails(int geoFenceTransition, List<Geofence> triggeringGeofences) {
        // get the ID of each geofence triggered
        ArrayList<String> triggeringGeofencesList = new ArrayList<>();
        for ( Geofence geofence : triggeringGeofences ) {
            Log.d(TAG, "getGeofenceTrasitionDetails: geogfence Request Id "+geofence.getRequestId());
            triggeringGeofencesList.add( geofence.getRequestId() );
        }

        String status = null;
        if ( geoFenceTransition == Geofence.GEOFENCE_TRANSITION_ENTER )
            status = "Entering ";
        else if ( geoFenceTransition == Geofence.GEOFENCE_TRANSITION_EXIT )
            status = "Exiting ";
        Log.d(TAG, "getGeofenceTrasitionDetails: "+triggeringGeofencesList);
        return status + TextUtils.join( ", ", triggeringGeofencesList);
    }

    // Send a notification
    private void sendNotification( String msg ) {
        Log.i(TAG, "sendNotification: " + msg );

        // Intent to start the main Activity
        Intent notificationIntent = new Intent(this, ConditionsActivity.class);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(MainActivity.class);
        stackBuilder.addNextIntent(notificationIntent);
        PendingIntent notificationPendingIntent = stackBuilder.getPendingIntent(0,
                PendingIntent.FLAG_UPDATE_CURRENT);

        // Creating and sending Notification
        NotificationManager notificatioMng =
                (NotificationManager) getSystemService( Context.NOTIFICATION_SERVICE );
        notificatioMng.notify(
                GEOFENCE_NOTIFICATION_ID,
                createNotification(msg, notificationPendingIntent));
    }

    // Create a notification
    private Notification createNotification(String msg, PendingIntent notificationPendingIntent) {
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this);
        notificationBuilder
                .setSmallIcon(R.drawable.ic_location_on_white_24dp)
                .setColor(Color.RED)
                .setContentTitle(msg)
                .setContentText("Geofence Notification!")
                .setContentIntent(notificationPendingIntent)
                .setDefaults(Notification.DEFAULT_LIGHTS | Notification.DEFAULT_VIBRATE | Notification.DEFAULT_SOUND)
                .setAutoCancel(true);
        return notificationBuilder.build();
    }

    // Handle errors
    private static String getErrorString(int errorCode) {
        switch (errorCode) {
            case GeofenceStatusCodes.GEOFENCE_NOT_AVAILABLE:
                return "GeoFence not available";
            case GeofenceStatusCodes.GEOFENCE_TOO_MANY_GEOFENCES:
                return "Too many GeoFences";
            case GeofenceStatusCodes.GEOFENCE_TOO_MANY_PENDING_INTENTS:
                return "Too many pending intents";
            default:
                return "Unknown error.";
        }
    }
}
