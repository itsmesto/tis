package com.example.lovemessagescheduler;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Calendar;
import java.util.Random;

final class Scheduler {
    static final String PREF="love_scheduler";
    static void scheduleAll(Context c){for(int i=0;i<3;i++)schedule(c,i);}
    static void cancelAll(Context c){AlarmManager am=(AlarmManager)c.getSystemService(Context.ALARM_SERVICE);for(int i=0;i<3;i++){PendingIntent pi=pending(c,i);am.cancel(pi);pi.cancel();}}
    static void schedule(Context c,int slot){
        android.content.SharedPreferences p=c.getSharedPreferences(PREF,0);if(!p.getBoolean("enabled",false))return;
        try{
            String[] range=p.getString("window"+slot,"").split("-");if(range.length!=2)return;
            int start=minutes(range[0]),end=minutes(range[1]);if(end<=start)return;
            Calendar now=Calendar.getInstance(), when=Calendar.getInstance();when.set(Calendar.HOUR_OF_DAY,0);when.set(Calendar.MINUTE,0);when.set(Calendar.SECOND,0);when.set(Calendar.MILLISECOND,0);
            int span=end-start;int minute=start+(span==0?0:new Random().nextInt(span));int second=new Random().nextInt(60);
            when.add(Calendar.MINUTE,minute);when.add(Calendar.SECOND,second);if(when.getTimeInMillis()<=now.getTimeInMillis())when.add(Calendar.DAY_OF_YEAR,1);
            AlarmManager am=(AlarmManager)c.getSystemService(Context.ALARM_SERVICE);am.setAndAllowWhileIdle(AlarmManager.RTC_WAKEUP,when.getTimeInMillis(),pending(c,slot));
        }catch(Exception ignored){}
    }
    private static int minutes(String time){String[] p=time.trim().split(":");if(p.length!=2)throw new IllegalArgumentException();int h=Integer.parseInt(p[0]),m=Integer.parseInt(p[1]);if(h<0||h>23||m<0||m>59)throw new IllegalArgumentException();return h*60+m;}
    private static PendingIntent pending(Context c,int slot){Intent i=new Intent(c,ScheduleReceiver.class).putExtra("slot",slot);return PendingIntent.getBroadcast(c,slot,i,PendingIntent.FLAG_UPDATE_CURRENT|PendingIntent.FLAG_IMMUTABLE);}
    static void launchWhatsApp(Context c,int slot){
        android.content.SharedPreferences p=c.getSharedPreferences(PREF,0);String phone=p.getString("phone","").replaceAll("[^0-9]","");String[] lines=p.getString("messages"+slot,"").split("\\R");java.util.ArrayList<String> choices=new java.util.ArrayList<>();for(String line:lines)if(!line.trim().isEmpty())choices.add(line.trim());
        if(phone.length()<8||choices.isEmpty()){schedule(c,slot);return;}
        String message=choices.get(new Random().nextInt(choices.size()));p.edit().putString("pending_message",message).putLong("pending_until",System.currentTimeMillis()+90000).putBoolean("pending_send",true).apply();
        try{String url="https://wa.me/"+phone+"?text="+URLEncoder.encode(message,StandardCharsets.UTF_8.name());Intent open=new Intent(Intent.ACTION_VIEW, Uri.parse(url));open.setPackage("com.whatsapp");open.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);c.startActivity(open);}catch(Exception e){p.edit().putBoolean("pending_send",false).apply();}
        schedule(c,slot);
    }
}
