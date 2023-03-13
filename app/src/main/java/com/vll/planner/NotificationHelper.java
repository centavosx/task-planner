package com.vll.planner;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class NotificationHelper extends ContextWrapper {
    public static final String channelID = "channelID";
    public static final String channelName = "Channel Name";
    private NotificationManager mManager;
    public NotificationHelper(Context base) {
        super(base);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createChannel();
        }
    }
    @TargetApi(Build.VERSION_CODES.O)
    private void createChannel() {
        NotificationChannel channel = new NotificationChannel(channelID, channelName, NotificationManager.IMPORTANCE_HIGH);
        getManager().createNotificationChannel(channel);
    }
    public NotificationManager getManager() {
        if (mManager == null) {
            mManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        }
        return mManager;
    }
    @SuppressLint("ResourceAsColor")
    public NotificationCompat.Builder getChannelNotification() {
        SharedPreferences myPref = getSharedPreferences("MyPref", Context.MODE_PRIVATE);
        Date datenow = new Date(System.currentTimeMillis());
        DateFormat format = new SimpleDateFormat("MM/dd/yyyy hh:mm aa", Locale.ENGLISH);
        String title = myPref.getString(format.format(datenow) + "0", "");
        String desc = myPref.getString(format.format(datenow) + "1", "");
        myPref.edit().remove(format.format(datenow)+"0").apply();
        myPref.edit().remove(format.format(datenow)+"1").apply();
        final MediaPlayer mp = MediaPlayer.create(this, R.raw.switch_type);
        mp.start();
        return new NotificationCompat.Builder(getApplicationContext(), channelID)
                .setContentTitle("There's something you have to do now!")
                .setContentText("TODO: " + title + " : " + desc)
                .setSmallIcon(R.drawable.logo)
                .setAutoCancel(true);
    }
}