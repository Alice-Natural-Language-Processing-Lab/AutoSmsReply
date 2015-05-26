package com.example.autosmsreply;

import java.util.Calendar;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.content.WakefulBroadcastReceiver;

/**
 * If a user adds a scheduledSms then here it will make a intent. 
 * Once the time comes that the event should be run, a sms will be sent. 
 * @author Jocke
 *
 */
public class ScheduledSmsReceiver extends WakefulBroadcastReceiver{

	private AlarmManager alarmMgr;
	private PendingIntent alarmIntent;
	
	@Override
	public void onReceive(Context context, Intent intent) {
		Intent service = new Intent(context, MySchedulingService.class);        
        startWakefulService(context, service);		
	}
	
	public void setSms(Context context, int day, int month, int year, int hour,int minute){
		alarmMgr = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, ScheduledSmsReceiver.class);
        alarmIntent = PendingIntent.getBroadcast(context, 0, intent, 0);
        
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.YEAR,year);
        calendar.set(Calendar.DATE, day);
        calendar.set(Calendar.MONTH,month);
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        
        alarmMgr.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), alarmIntent);
        
        ComponentName receiver = new ComponentName(context, MyBootReceiver.class);
        PackageManager pm = context.getPackageManager();

        pm.setComponentEnabledSetting(receiver,
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                PackageManager.DONT_KILL_APP);         
	}
}
