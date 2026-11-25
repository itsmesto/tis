package com.example.lovemessagescheduler;

import android.accessibilityservice.AccessibilityService;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

public class WhatsAppAccessibilityService extends AccessibilityService {
    @Override public void onAccessibilityEvent(AccessibilityEvent event){
        if(event==null||!"com.whatsapp".equals(String.valueOf(event.getPackageName())))return;
        android.content.SharedPreferences p=getSharedPreferences(Scheduler.PREF,0);if(!p.getBoolean("pending_send",false))return;
        if(System.currentTimeMillis()>p.getLong("pending_until",0)){p.edit().putBoolean("pending_send",false).apply();return;}
        AccessibilityNodeInfo root=getRootInActiveWindow();if(root==null)return;
        String expected=p.getString("pending_message","");if(expected.isEmpty()||!hasScheduledText(root,expected))return;
        AccessibilityNodeInfo send=findSend(root);if(send!=null&&send.isEnabled()){
            boolean clicked=send.performAction(AccessibilityNodeInfo.ACTION_CLICK);
            if(clicked)p.edit().putBoolean("pending_send",false).putLong("last_sent",System.currentTimeMillis()).apply();
        }
    }
    private boolean hasScheduledText(AccessibilityNodeInfo node,String expected){
        CharSequence text=node.getText();
        if(text!=null&&text.toString().trim().equals(expected.trim()))return true;
        for(int i=0;i<node.getChildCount();i++){AccessibilityNodeInfo child=node.getChild(i);if(child!=null&&hasScheduledText(child,expected))return true;}
        return false;
    }
    private AccessibilityNodeInfo findSend(AccessibilityNodeInfo node){
        CharSequence text=node.getText(),desc=node.getContentDescription();
        if(node.isClickable()&&node.isEnabled()&&(equalsSend(text)||equalsSend(desc)))return node;
        for(int i=0;i<node.getChildCount();i++){AccessibilityNodeInfo child=node.getChild(i);if(child!=null){AccessibilityNodeInfo found=findSend(child);if(found!=null)return found;}}
        return null;
    }
    private boolean equalsSend(CharSequence s){if(s==null)return false;String v=s.toString().trim();return v.equalsIgnoreCase("Send")||v.equalsIgnoreCase("Send message");}
    @Override public void onInterrupt(){}
}
