package com.ilrilan.kortros_auto_opener;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.app.Notification;
import android.app.Notification.Action;

public class NotificationService extends NotificationListenerService {
    Context context;
    String titleData="", textData="", actionsData="";
    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
    }
    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        String packageName = sbn.getPackageName();
        Notification n = sbn.getNotification();
        Bundle extras = n.extras;
        if(extras.getString("android.title")!=null){
            titleData = extras.getString("android.title");
        }else{
            titleData = "";
        }
        if(extras.getCharSequence("android.text")!=null){
            textData = extras.getCharSequence("android.text").toString();
        }else{
            textData = "";
        }


        Log.d("Package", packageName);
        Log.d("Title", titleData);
        Log.d("Text", textData);
        for(Action action : n.actions){
            String title = action.title.toString();
            Log.d("Action", title);
            if (actionsData.length() > 0) {
                actionsData += ",";
            }
            if (title.equals("Пометить как прочитанное") || title.equalsIgnoreCase("Открыть дверь")) {
                try {
                    action.actionIntent.send();
                    actionsData += title + "_PRESSED_";
                } catch (PendingIntent.CanceledException e) {
                    e.printStackTrace();
                }
            } else {
                actionsData += title + "_no_press_";
            }
        }
        Intent msgrcv = new Intent("Msg");
        msgrcv.putExtra("package", packageName);
        msgrcv.putExtra("title", titleData);
        msgrcv.putExtra("text", textData);
        msgrcv.putExtra("actions", actionsData);
        LocalBroadcastManager.getInstance(context).sendBroadcast(msgrcv);
    }
    @Override
    public void onNotificationRemoved(StatusBarNotification sbn) {
        Log.d("Msg", "Notification Removed");
    }
}