package com.example.autosmsreply;

import com.autosmsreply.assets.ScheduledSmsItem;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.telephony.SmsManager;

/**
 * THis class sends the sms and notifys the user once this is done.
 * @author Jocke
 *
 */
public class MySchedulingService extends IntentService{
	
	StaticNextScheduledSmsInfo sms = new StaticNextScheduledSmsInfo();

	public MySchedulingService(){
		super("SchedulingService");
	}
	
	@Override
	protected void onHandleIntent(Intent intent) {

		ScheduledSmsItem  toSend = ScheduledNextSmsHolder.nextScheduledSmsItemToSend;
		
		if(toSend != null){
			PendingIntent pi = PendingIntent.getActivity(this, 0, new Intent(), 0);
			SmsManager sms = SmsManager.getDefault();
			sms.sendTextMessage(parseNumb(toSend.number), null, toSend.message, pi, null);
			notifySmsSent(getApplicationContext(), toSend.message, parseNumb(toSend.number));
		}
		ScheduledSmsReceiver.completeWakefulIntent(intent);
	}

	public String parseNumb(String numb){
		char[] temp = numb.toCharArray();
		if(temp[0] == '+' && temp[1] == '4' && temp[2] == '6')
			return numb;
		else{
			String toRet = "+46" + numb;
			return toRet;
		}
	}
	
	public void notifySmsSent(Context context, String message, String numb){
		
		NotificationCompat.Builder notifyBuilder = new NotificationCompat.Builder(context)
	    					.setSmallIcon(R.drawable.icon)
	    							.setContentTitle("Scheduled message was sent!")
	    									.setContentText(" To : " + numb +"\n" + "Message: " + message);
		
		NotificationManager notifyManager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
		notifyManager.notify(001, notifyBuilder.build());
	}
	
}
