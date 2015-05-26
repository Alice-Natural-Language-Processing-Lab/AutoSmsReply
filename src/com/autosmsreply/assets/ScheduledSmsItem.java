package com.autosmsreply.assets;

public class ScheduledSmsItem {

	public int id;
	public String reciever,number, message,contactPhotoUri;
	public int day,month,year,hour,minute;

	public ScheduledSmsItem(int id,String reciever,String number,String message, int day,int month,int year,int hour,int minute,String contactPhotoUri){
		this.id = id;
		this.reciever = reciever;
		this.number = number;
		this.message = message;
		this.day = day;
		this.month = month;
		this.year = year;
		this.hour = hour;
		this.minute = minute;		
		this.contactPhotoUri = contactPhotoUri;
	}
}
