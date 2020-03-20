package com.kill.whappro.utils;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.SystemClock;

import com.kill.whappro.receivers.NotificationReceiver;

public class UtilsApp {

    public static void setNotification(Context context, Boolean enable, Integer hours) {
        Intent intent = new Intent(context, NotificationReceiver.class);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        if (enable) {
            alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime(), getHoursToMilliseconds(hours), pendingIntent);
        } else {
            alarmManager.cancel(pendingIntent);
            pendingIntent.cancel();
        }
    }

    public static Boolean isNotificationRunning(Context context) {
        Intent intent = new Intent(context, NotificationReceiver.class);
        return (PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_NO_CREATE) != null);
    }

    /**
     * Retrieve your own app version
     * @param context Context
     * @return String with the app version
     */
    public static String getAppVersionName(Context context) {
        String res = "0.0.0.0";
        try {
            res = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return res;
    }

    /**
     * Retrieve your own app version code
     * @param context Context
     * @return int with the app version code
     */
    public static int getAppVersionCode(Context context) {
        int res = 0;
        try {
            res = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionCode;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return res;
    }

    private static Integer getHoursToMilliseconds(Integer hours) {
        return hours * 60 * 60 * 1000;
    }

    public static String getVersionFromString(String s) {
        String res = "0.0.0.0";

        Uri uri = Uri.parse(s);
        String withoutHost = uri.getPath();
        String[] split = withoutHost.split("/");
        String version = split[2];

        if (!version.equals("current")) {
            res = version;
        }

        return res;
    }

}
