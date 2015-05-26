package com.autosmsreply.assets;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ScheduledSmsDBHelper extends SQLiteOpenHelper{
	
	public static final String DATABASE_NAME = "scheduledsms4.db";
	public static final String TABLE_NAME = "ScheduledSms";
	public static final String ID = "id";
	public static final String RECEIVER = "receiver";
	public static final String RECEIVER_NUMBER = "number";
	public static final String MESSAGE = "message";
	public static final String DATE_DAY = "day";
	public static final String DATE_MONTH = "month";
	public static final String DATE_YEAR = "year";
	public static final String TIME_HOUR = "hour";
	public static final String TIME_MINUTE = "minute";
	
	public ScheduledSmsDBHelper(Context context){
		super(context,DATABASE_NAME,null,1);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("CREATE TABLE IF NOT EXISTS ScheduledSms(id integer primary key,receiver VARCHAR,number VARCHAR,message int,day int,month int,year int,hour int,minute int,image VARCHAR);");		
	}

	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
		
	}
	
	public boolean insertScheduledSms(String receiver,String number,String message, int day, int month, int year, int hour,int minute,String imageUri){
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues contentValues = new ContentValues();		
		contentValues.put("receiver", receiver);
		contentValues.put("number", number);
		contentValues.put("message", message);
		contentValues.put("day", day);		
		contentValues.put("month", month);
		contentValues.put("year", year);
		contentValues.put("hour", hour);
		contentValues.put("minute", minute);
		contentValues.put("image", imageUri);
		db.insert("ScheduledSms", null, contentValues);
		return true;
	}
	
	public void deleteAll(){
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_NAME, null, null);
	}
	
	public void delete(String name, String message){
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_NAME, RECEIVER + "="  + "'"+ name + "'", null);
	}
	
	public ArrayList<ScheduledSmsItem> getAllScheduledSms(){
		ArrayList<ScheduledSmsItem> arr = new ArrayList<ScheduledSmsItem>();
		SQLiteDatabase db = this.getReadableDatabase();
	      Cursor res =  db.rawQuery( "select * from ScheduledSms", null );
	      res.moveToFirst();
	      while(res.isAfterLast() == false){ 
	    	  int id = Integer.parseInt(res.getString(0));
	    	  String receiver = res.getString(1);
	    	  String number = res.getString(2);
	    	  String message = res.getString(3);
	    	  int day = res.getInt(4);
	    	  int month = res.getInt(5);
	    	  int year = res.getInt(6);
	    	  int hour = res.getInt(7);
	    	  int minute = res.getInt(8);
	    	  String imageUri = res.getString(9);
	    	  
    		  arr.add(new ScheduledSmsItem(id,receiver,number,message,day,month,year,hour,minute,imageUri));
	    	  
	      res.moveToNext();
	      }
	   return arr;
	}

}
