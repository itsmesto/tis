package com.example.lovemessagescheduler;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;

public class MainActivity extends Activity {
    EditText phone, morningWindow, afternoonWindow, nightWindow, morningMessages, afternoonMessages, nightMessages;
    TextView status;
    @Override public void onCreate(Bundle state) {
        super.onCreate(state);
        getWindow().setStatusBarColor(Color.rgb(255,245,247));
        getWindow().setNavigationBarColor(Color.rgb(255,245,247));
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR|View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR);
        ScrollView scroll = new ScrollView(this);
        LinearLayout root = new LinearLayout(this); root.setOrientation(LinearLayout.VERTICAL); root.setPadding(dp(22),dp(26),dp(22),dp(28)); root.setBackgroundColor(Color.rgb(255,245,247)); scroll.addView(root);
        TextView title = new TextView(this); title.setText("A little message, every day 💗"); title.setTextSize(25); title.setTextColor(Color.rgb(90,33,55)); title.setTypeface(null,Typeface.BOLD); root.addView(title);
        TextView intro = new TextView(this); intro.setText("Choose a contact, message ideas, and time windows. The app picks a random time and one random message for each enabled period."); intro.setTextSize(15); intro.setTextColor(Color.DKGRAY); intro.setPadding(0,dp(8),0,dp(16)); root.addView(intro);
        phone = field(root,"WhatsApp number (country code, digits only)","",false);
        morningWindow=field(root,"Morning random window (HH:mm-HH:mm)","07:00-09:00",false);
        morningMessages=field(root,"Good morning messages (one per line)","Good morning, my love ❤️\nI hope your day starts with a smile ☀️\nGood morning! Thinking of you 💕",true);
        afternoonWindow=field(root,"Afternoon random window (HH:mm-HH:mm)","12:00-14:00",false);
        afternoonMessages=field(root,"Good afternoon messages (one per line)","Good afternoon, love 💛 I hope your day is going well.\nJust sending you a little afternoon smile 😊\nHope you’re having a lovely afternoon 💕",true);
        nightWindow=field(root,"Night random window (HH:mm-HH:mm)","20:00-22:00",false);
        nightMessages=field(root,"Good night messages (one per line)","Good night, my love 🌙 Sleep well ❤️\nSweet dreams, darling 💕\nGood night! I’ll be thinking of you ✨",true);
        status = new TextView(this); status.setTextSize(14); status.setTextColor(Color.rgb(90,33,55)); status.setPadding(0,dp(12),0,dp(8)); root.addView(status);
        Button save = button("Save and schedule",Color.rgb(186,57,101)); root.addView(save); save.setOnClickListener(v->{saveSettings(); Scheduler.scheduleAll(this); status.setText("Saved. Your randomized daily messages are scheduled.");});
        Button stop = button("Stop all scheduled messages",Color.rgb(105,93,98)); root.addView(stop); stop.setOnClickListener(v->{getSharedPreferences(Scheduler.PREF,0).edit().putBoolean("enabled",false).apply(); Scheduler.cancelAll(this); status.setText("Scheduling is stopped.");});
        Button access = button("One-time setup: enable WhatsApp sending",Color.rgb(90,33,55)); root.addView(access); access.setOnClickListener(v->startActivity(new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)));
        TextView note = new TextView(this); note.setText("Setup needed once: enable “WhatsApp message sender” in Android Accessibility settings. Android requires this permission for the app to tap Send. Keep WhatsApp installed and logged in. Battery saver, Android updates, or WhatsApp changes can delay or stop sending. Messages are sent without a final confirmation once scheduled."); note.setTextSize(13); note.setTextColor(Color.DKGRAY); note.setPadding(0,dp(14),0,0); root.addView(note);
        loadSettings(); setContentView(scroll);
    }
    private EditText field(LinearLayout root,String label,String value,boolean multi) {
        TextView t=new TextView(this); t.setText(label); t.setTextSize(14); t.setTypeface(null,Typeface.BOLD); t.setTextColor(Color.rgb(90,33,55)); t.setPadding(0,dp(11),0,dp(5)); root.addView(t);
        EditText e=new EditText(this); e.setText(value); e.setTextSize(15); e.setTextColor(Color.BLACK); e.setPadding(dp(12),dp(7),dp(12),dp(7)); e.setBackground(round(Color.WHITE,dp(12))); e.setSingleLine(!multi); if(multi){e.setMinLines(3);e.setGravity(48);} root.addView(e,new LinearLayout.LayoutParams(-1,-2)); return e;
    }
    private Button button(String text,int color){Button b=new Button(this);b.setText(text);b.setTextColor(Color.WHITE);b.setAllCaps(false);b.setTextSize(15);b.setBackground(round(color,dp(14)));LinearLayout.LayoutParams lp=new LinearLayout.LayoutParams(-1,-2);lp.topMargin=dp(10);b.setLayoutParams(lp);return b;}
    private GradientDrawable round(int color,int radius){GradientDrawable d=new GradientDrawable();d.setColor(color);d.setCornerRadius(radius);return d;}
    private int dp(int x){return (int)(x*getResources().getDisplayMetrics().density+.5f);}
    private void saveSettings(){getSharedPreferences(Scheduler.PREF,0).edit().putString("phone",phone.getText().toString().trim()).putString("window0",morningWindow.getText().toString().trim()).putString("messages0",morningMessages.getText().toString()).putString("window1",afternoonWindow.getText().toString().trim()).putString("messages1",afternoonMessages.getText().toString()).putString("window2",nightWindow.getText().toString().trim()).putString("messages2",nightMessages.getText().toString()).putBoolean("enabled",true).apply();}
    private void loadSettings(){android.content.SharedPreferences p=getSharedPreferences(Scheduler.PREF,0);phone.setText(p.getString("phone",""));morningWindow.setText(p.getString("window0","07:00-09:00"));morningMessages.setText(p.getString("messages0",morningMessages.getText().toString()));afternoonWindow.setText(p.getString("window1","12:00-14:00"));afternoonMessages.setText(p.getString("messages1",afternoonMessages.getText().toString()));nightWindow.setText(p.getString("window2","20:00-22:00"));nightMessages.setText(p.getString("messages2",nightMessages.getText().toString()));status.setText(p.getBoolean("enabled",false)?"Scheduling is on.":"Scheduling is off until you save and schedule.");}
}
