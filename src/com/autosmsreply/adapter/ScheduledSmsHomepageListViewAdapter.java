package com.autosmsreply.adapter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;

import com.autosmsreply.assets.ScheduledSmsDBHelper;
import com.autosmsreply.assets.ScheduledSmsItem;
import com.example.autosmsreply.CustomComparator;
import com.example.autosmsreply.PhotoLoader;
import com.example.autosmsreply.R;
import com.example.autosmsreply.ScheduledSmsReceiver;
import com.example.autosmsreply.StaticNextScheduledSmsInfo;
import com.example.autosmsreply.ScheduledNextSmsHolder;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.QuickContactBadge;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

/**
 * The adapter class for ScheduledSmsHomepageFragment. User's actions will be handled here. 
 * @author Jocke
 *
 */
public class ScheduledSmsHomepageListViewAdapter extends BaseAdapter{
	
	private Context context;
	private static LayoutInflater inflater = null;
	
	public static ArrayList<ScheduledSmsItem> scheduledSmsList;
	private ArrayAdapter<ScheduledSmsItem> scheduledSmsArrayAdapter;
	
	StaticNextScheduledSmsInfo contactInfo = new StaticNextScheduledSmsInfo();
	ScheduledSmsReceiver sms = new ScheduledSmsReceiver();
	
	private static ScheduledSmsDBHelper scheduledDbHelper;
	
	private TextView scheduledSmsSendReciever;
	private QuickContactBadge contactBadge;
	private TextView scheduledSmsSendDate;
	private TextView scheduledSmsSendContent;
	
	private PhotoLoader photoLoader;
	
	public ScheduledSmsHomepageListViewAdapter(Context context){
		this.context = (FragmentActivity) context;
		inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		scheduledDbHelper = new ScheduledSmsDBHelper(context);
		photoLoader = new PhotoLoader(context);
		scheduledSmsList = scheduledDbHelper.getAllScheduledSms();
		updateData();
		checkToRemove();
	}

	/**
	 * Update the information. 
	 */
	public void updateData(){
		scheduledSmsList = scheduledDbHelper.getAllScheduledSms();
		Collections.sort(scheduledSmsList,new CustomComparator());
		checkToRemove();
		if(scheduledSmsList.size() >=1){
			ScheduledNextSmsHolder.nextScheduledSmsItemToSend = scheduledSmsList.get(0);
			StaticNextScheduledSmsInfo.message = scheduledSmsList.get(0).message;
			StaticNextScheduledSmsInfo.number = scheduledSmsList.get(0).number;
			StaticNextScheduledSmsInfo.name = scheduledSmsList.get(0).reciever;
		}
		if(scheduledSmsList.size() >= 1){
			for(ScheduledSmsItem s : scheduledSmsList){
				if(s != null && scheduledSmsArrayAdapter != null)
					scheduledSmsArrayAdapter.add(s);
			}
			this.notifyDataSetChanged();
		}
		
	}
	
	/**
	 * Run after update, then you dont have to check that they are active.
	 */
	public void checkForOldScheduledEventsToComplete(){
		for(ScheduledSmsItem s : scheduledSmsList){
			sms.setSms(context, s.day, s.month, s.year, s.hour, s.minute);
		}
	}
	
	public static void prepareForNextSms(){
		ScheduledSmsItem temp = null;
		if(scheduledSmsList.size() >= 1){
			temp = scheduledSmsList.get(0);
			StaticNextScheduledSmsInfo.name = temp.reciever;
			StaticNextScheduledSmsInfo.message = temp.message;
			StaticNextScheduledSmsInfo.number = temp.number;
		}
	}
	
	@Override
	public int getCount() {
		if(scheduledSmsList != null)
			return scheduledSmsList.size();
		else
			return 0;
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int id) {
		int i = 0;
		if(scheduledSmsList != null)
			 i = scheduledSmsList.indexOf(scheduledSmsList.get(id));
		return i;
	}

	@Override
	public View getView(int id, View arg1, ViewGroup arg2) {
		View rowView = (inflater.inflate(R.layout.fragment_scheduled_sms_homepage_single_liste_item, null));
		scheduledSmsArrayAdapter = new ArrayAdapter<ScheduledSmsItem>(context, R.layout.fragment_auto_reply_homepage_single_list_item,scheduledSmsList);
		
		scheduledSmsSendReciever = (TextView) rowView.findViewById(R.id.scheduled_sms_receiver);
		contactBadge = (QuickContactBadge) rowView.findViewById(R.id.quickContactBadge1);
		scheduledSmsSendDate = (TextView) rowView.findViewById(R.id.scheduled_sms_send_date);
		scheduledSmsSendContent = (TextView) rowView.findViewById(R.id.scheduled_sms_content);		
		
		if(scheduledSmsList.size() >= 1){
			ScheduledSmsItem temp = scheduledSmsList.get(id);
			scheduledSmsSendReciever.setText(temp.reciever);
			scheduledSmsSendDate.setText(temp.year + ":" + temp.month +":"+ temp.day + " at " + temp.hour +":" + temp.minute);
			scheduledSmsSendContent.setText(temp.message);
			
			if(temp.contactPhotoUri != null || !temp.contactPhotoUri.equalsIgnoreCase("")){
				contactBadge.setImageBitmap(photoLoader.loadContactPhotoThumbnail(temp.contactPhotoUri));
			}
			
		}
		checkToRemove();
		
		return rowView;
	}
	
	
	/**
	 * This method checks whether a scheduled sms has been sent, this is done by 
	 * comparing the current time with the scheduled events time. 
	 */
	public void checkToRemove(){
		if(scheduledSmsList.size() >= 1){
			Calendar currentTime = Calendar.getInstance();
			ScheduledSmsItem  sms = scheduledSmsList.get(0);
			
			int currMinute =currentTime.get(Calendar.MINUTE); 
			int currHour = currentTime.get(Calendar.HOUR_OF_DAY);
			int currYear = currentTime.get(Calendar.YEAR);
			int currMonth = currentTime.get(Calendar.MONTH);
			int currDay = currentTime.get(Calendar.DAY_OF_MONTH);
			
			if(currYear >= sms.year)
				if(currMonth >= sms.month)
					if(currDay >= sms.day)
						if(currHour >= sms.hour && currMinute > sms.minute){
							scheduledDbHelper.delete(sms.reciever,sms.message);
							updateData();
						}else if(currHour > sms.hour && currMinute < sms.minute){
							scheduledDbHelper.delete(sms.reciever,sms.message);
							updateData();
						}
		}
	}
	
	/**
	 * Insert item into the database. 
	 * @param receiver
	 * @param number
	 * @param message
	 * @param day
	 * @param month
	 * @param year
	 * @param hour
	 * @param minute
	 * @param contactImageUri
	 */
	public void addItem(String receiver,String number,String message, int day, int month, int year, int hour,int minute, String contactImageUri){
		scheduledDbHelper.insertScheduledSms(receiver,number, message, day, month, year, hour, minute,contactImageUri);
		updateData();
	}
	
	/**
	 * If the user decides to add a scheduled sms, a dialog will apear 
	 * where the user has to fill out certain things. 
	 */
	public void addScheduledSmsDialog(){
		LayoutInflater linf = LayoutInflater.from(context);            
		final View inflator = linf.inflate(R.layout.dialog_add_scheduled_sms, null);
		AlertDialog.Builder alert = new AlertDialog.Builder(context); 
		alert.setView(inflator);
		final TextView name = (TextView) inflator.findViewById(R.id.scheduled_sms_receiver);
		final EditText messageField = (EditText) inflator.findViewById(R.id.scheduled_sms_add_message);
		final TimePicker time = (TimePicker) inflator.findViewById(R.id.timePicker1);
		final DatePicker date = (DatePicker) inflator.findViewById(R.id.datePicker1);
		alert.setPositiveButton("Add", new DialogInterface.OnClickListener() { 
		   public void onClick(DialogInterface dialog, int whichButton) 
		   { 
		          String message=messageField.getText().toString();
		          int hour = time.getCurrentHour();
		          int minute = time.getCurrentMinute();
		          int year = date.getYear();
		          int month = date.getMonth();
		          int day = date.getDayOfMonth();
		          if(StaticNextScheduledSmsInfo.name != ""){
		        	  addItem(StaticNextScheduledSmsInfo.name,StaticNextScheduledSmsInfo.number, message, day, month, year, hour, minute,StaticNextScheduledSmsInfo.uriToContactImage);
		        	  StaticNextScheduledSmsInfo.message = message;
		        	  sms.setSms(context, day, month, year, hour, minute);
		          }else{
		        	  Toast.makeText(context, "Contact has bad info.", Toast.LENGTH_SHORT).show();
		          }
		          prepareForNextSms();
		   } 
		}); 
		alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() { 
		   public void onClick(DialogInterface dialog, int whichButton) { 
			   dialog.cancel(); 
		   } 
		}); 
		alert.show(); 
	}
}
