package com.example.autosmsreply;



import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract.PhoneLookup;
import android.support.v4.app.NotificationCompat;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;

/**
 * This class is used as a receiver to handle potential incoming sms messages. 
 * @author Jocke
 *
 */
public class Receiver extends BroadcastReceiver{
	
	private String number;
	Object[] protocolDataUnit;
	private SmsMessage[] message;
	private String finalMessage = "";
	
	Context context;
	
	long id;

	@Override
	public void onReceive(Context context, Intent intent) {
		this.context = context;
		Bundle bundle = intent.getExtras();
		if(bundle != null){
			protocolDataUnit = (Object[])bundle.get("pdus");
			message = new SmsMessage[protocolDataUnit.length];
			for(int i=0;i<message.length;i++){
				message[i] = SmsMessage.createFromPdu((byte[])protocolDataUnit[i]);
				number = message[i].getOriginatingAddress();	                
				finalMessage = message[i].getMessageBody().toString();	                
			}

			if(StaticSmsInfo.messageToSendBackToLatestReceived != "")
				if(!StaticSmsInfo.latestRecievedSmsMessage.equalsIgnoreCase(finalMessage)){
					StaticSmsInfo.latestRecievedSmsMessage = finalMessage;
					StaticSmsInfo.latestRecievedSmsNumber = number;
					sendMessageBack();
					showNotificationAutoReplySent(number);
				}
		}
	}
	
	public void showNotificationAutoReplySent(String fromNumber){
		String contactName = getContactName(fromNumber);
		NotificationCompat.Builder notifyBuilder = new NotificationCompat.Builder(context)
	    .setSmallIcon(R.drawable.icon)
	    	.setContentTitle("AutoSmsReply")
	    		.setContentText("A reply was sent to " + contactName);
		NotificationManager notifyManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
				notifyManager.notify(001, notifyBuilder.build());
	}
	
	
	public void sendMessageBack(){
		PendingIntent pi = PendingIntent.getActivity(context, 0, new Intent(), 0);
    	SmsManager sms = SmsManager.getDefault();
    	sms.sendTextMessage(StaticSmsInfo.latestRecievedSmsNumber, null, StaticSmsInfo.messageToSendBackToLatestReceived, pi, null);
	}
	
	public String getContactName(String number){
		ContentResolver cr = context.getContentResolver();
	    Uri uri = Uri.withAppendedPath(PhoneLookup.CONTENT_FILTER_URI, Uri.encode(number));
	    Cursor cursor = cr.query(uri, new String[]{PhoneLookup.DISPLAY_NAME, PhoneLookup._ID}, null, null, null);
	    if (cursor == null) {
	        return null;
	    }
	    String contactName = null;
	    
	    if(cursor.moveToFirst()) {
	        contactName = cursor.getString(cursor.getColumnIndex(PhoneLookup.DISPLAY_NAME));
	        id = cursor.getLong(cursor.getColumnIndex(PhoneLookup._ID));
	    }

	    if(cursor != null && !cursor.isClosed()) {
	        cursor.close();
	    }

	    return contactName;
	}	
}