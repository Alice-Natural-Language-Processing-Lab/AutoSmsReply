package com.example.autosmsreply;

import java.util.ArrayList;
import java.util.Collections;

import com.autosmsreply.assets.ScheduledSmsDBHelper;
import com.autosmsreply.assets.ScheduledSmsItem;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * A Receiver class that is used so that a scheduled sms can be sent even if the app is turned off. 
 * @author Jocke
 *
 */
public class MyBootReceiver extends BroadcastReceiver {
	ScheduledSmsReceiver sms = new ScheduledSmsReceiver();
	
    @Override
    public void onReceive(Context context, Intent intent) {
    	ScheduledSmsDBHelper dbHelper = new ScheduledSmsDBHelper(context);
    	
        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED"))
        {        	
        	
        	ArrayList<ScheduledSmsItem> temp = dbHelper.getAllScheduledSms();
        	Collections.sort(temp,new CustomComparator());
        	
        	for(ScheduledSmsItem s : temp){
        		sms.setSms(context,s.day,s.month,s.year,s.hour,s.minute);
        	}
            
        }
    }
}
