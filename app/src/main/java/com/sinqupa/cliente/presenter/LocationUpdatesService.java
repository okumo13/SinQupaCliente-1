package com.sinqupa.cliente.presenter;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Icon;
import android.location.Location;
import android.media.AudioAttributes;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sinqupa.cliente.R;
import com.sinqupa.cliente.model.Employee;
import com.sinqupa.cliente.model.Utility;
import com.sinqupa.cliente.view.MenuActivity;

public class LocationUpdatesService extends Service  {

    private Handler handler;
    private MyTimerRunnable runnable;
    private NotificationManager mNotificationManager;
    private boolean isFive,isTen,isFifteen,isTwenty,isTwentyFive,isThirty;
    private DatabaseReference databaseReference;

    public LocationUpdatesService() {
        databaseReference = FirebaseDatabase.getInstance().getReference();
    }

    @Override
    public void onCreate() {
        handler = new Handler();
        runnable = new MyTimerRunnable();

        /*
        if (Utility.distance != 0) {
            isFive = Utility.distance == 5 ? true : false;
            isTen = Utility.distance == 10 ? true : false;
            isFifteen =  Utility.distance == 15 ? true : false;
            isTwenty = Utility.distance == 20 ? true : false;
            isTwentyFive = Utility.distance == 25 ? true : false;
            isThirty = Utility.distance == 30 ? true : false;
        }
        */

        mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.app_name);
            NotificationChannel mChannel = new NotificationChannel(Utility.CHANNEL_ID, name, NotificationManager.IMPORTANCE_DEFAULT);
            mChannel.setSound(Utility.uriNotification,new AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .setUsage(AudioAttributes.USAGE_NOTIFICATION_RINGTONE)
                    .build());
            mChannel.setVibrationPattern(new long[]{1000,1000,1000,1000,1000});
            mNotificationManager.createNotificationChannel(mChannel);
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        handler.post(runnable);
        boolean startedFromNotification = intent.getBooleanExtra(Utility.EXTRA_STARTED_FROM_NOTIFICATION,false);
        if (startedFromNotification)
            mNotificationManager.cancel(Utility.NOTIFICATION_ID);
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        handler.removeCallbacks(runnable);
        mNotificationManager.cancel(Utility.NOTIFICATION_ID);
    }

    //Metodo para Crear la Notificacion
    private Notification getNotification() {
        Intent intent = new Intent(this, LocationUpdatesService.class);
        intent.putExtra(Utility.EXTRA_STARTED_FROM_NOTIFICATION, true);
        PendingIntent servicePendingIntent = PendingIntent.getService(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        PendingIntent activityPendingIntent = PendingIntent.getActivity(this, 0,new Intent(this, MenuActivity.class), 0);
        Notification.Action servicePendingAction = new Notification.Action.Builder(Icon.createWithResource(this, R.drawable.icon_cancel),getString(R.string.remove_location_updates),servicePendingIntent).build();
        Notification.Action activityPendingAction = new Notification.Action.Builder(Icon.createWithResource(this, R.drawable.icon_redirection),getString(R.string.open_activity),activityPendingIntent).build();
        Notification.Builder builder = new Notification.Builder(getApplicationContext());
        builder.addAction(activityPendingAction);
        builder.addAction(servicePendingAction);
        builder.setSmallIcon(R.drawable.icon_notification);
        builder.setContentText(getString(R.string.notification_text));
        builder.setColor(Color.RED);
        builder.setWhen(System.currentTimeMillis());
        builder.setVibrate(new long[]{1000,1000,1000,1000,1000});
        builder.setPriority(Notification.PRIORITY_HIGH);
        builder.setSound(Utility.uriNotification);
        builder.setOngoing(true);

        // Set the Channel ID for Android O.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            builder.setChannelId(Utility.CHANNEL_ID); // Channel ID
        return builder.build();
    }

    //Metodo para validar la Notificacion
    private void validateNotification(final Location locationCustomer){
        databaseReference.child("Employee").orderByChild("activated").equalTo(true).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Employee employee = snapshot.getValue(Employee.class);
                    Location locationEmployee = new Location(Utility.TITLE_MARKER_EMPLOYEE);
                    locationEmployee.setLongitude(employee.getLongitudeTravel());
                    locationEmployee.setLatitude(employee.getLatitudeTravel());
                    float distanceTravel = locationCustomer.distanceTo(locationEmployee);

                    if ( distanceTravel >= (Utility.distance - 1)  && distanceTravel <= (Utility.distance + 1)){
                        mNotificationManager.notify(Utility.NOTIFICATION_ID, getNotification());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private class MyTimerRunnable implements Runnable {
        @Override
        public void run() {
            validateNotification(Utility.locationCustomer);
            handler.postDelayed(this, Utility.DEFAULT_TIMEOUT);
        }
    }
}
