package com.example.lovemessagescheduler;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
public class ScheduleReceiver extends BroadcastReceiver {
    @Override public void onReceive(Context context,Intent intent){Scheduler.launchWhatsApp(context,intent.getIntExtra("slot",0));}
}
