package com.example.reminderwithnotification;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.SystemClock;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class NotificationService extends Service {

    String[] myDates={"24-09-2019","25-09-2019","24-09-2019","25-09-2019",
            "24-09-2019","26-09-2019","25-09-2019","24-09-2019"};
    int[] ids={1,2,3,4,5,6,7,8};

    private final int METHOD_INVOKE_TIME = 1000;
    private Handler handler;
    private String currentDate;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("NotificationService:", "Service started by user.");

        handler = new Handler();
        currentDate = GetBeforeDate(-1);

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                for (int i = 0; i < myDates.length; i++) {

                    if (myDates[i].equals(currentDate)) {
                        cancelNotification( ids[i]);
                        ShowCustomNotification(myDates[i], ids[i]);

                    }
                }

            }
        }, METHOD_INVOKE_TIME);

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("NotificationService:","Service destroyed by user.");
    }

    public String GetBeforeDate(int days){
        Calendar cal = Calendar.getInstance();;
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
        cal.add(Calendar.DAY_OF_YEAR, days);
        return format.format(new Date(cal.getTimeInMillis()));
    }

    public void ShowCustomNotification(String NotiDate,int NotiID){

        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        final int not_nu=generateRandom();
        PendingIntent pendingIntent = PendingIntent.getActivity(this, not_nu /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("Notification Title")
                .setContentText(NotiDate)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(NotiID, notificationBuilder.build());

    }

    public void cancelNotification(int NotiID) {

        String ns = Context.NOTIFICATION_SERVICE;
        NotificationManager nMgr = (NotificationManager) getApplicationContext().getSystemService(ns);
        nMgr.cancel(NotiID);
    }

    public int generateRandom(){
        Random random = new Random();
        return random.nextInt(9999 - 1000) + 1000;
    }
}
