package com.autosmsreply.assets;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


/**
 * This class is used to communicate with the database that handles autoreplys. 
 * @author Jocke
 *
 */
public class AutoReplyDbHelper extends SQLiteOpenHelper{
	
	public static final String DATABASE_NAME = "autoreply3.db";
	public static final String TABLE_NAME = "AutoReplys";
	public static final String ID = "id";
	public static final String NAME = "name";
	public static final String MESSAGE = "message";
	public static final String STATUS = "status";
	
	public AutoReplyDbHelper(Context context){
		super(context,DATABASE_NAME,null,1);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("CREATE TABLE IF NOT EXISTS AutoReplys(id integer primary key,name VARCHAR,message VARCHAR,status VARCHAR);");
	}

	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub	
	}
	
	public boolean insertAutoReply(String name,String message,String status){	
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues contentValues = new ContentValues();		
		contentValues.put("name", name);
		contentValues.put("message", message);
		contentValues.put("status", status);		
		db.insert("AutoReplys", null, contentValues);		
		return true;
	}
	
	public Cursor getData(int id){
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor res =  db.rawQuery( "select * from AutoReplys where id=" + "'" + id + "'", null );
		return res;
	}
	
	public void deleteAll(){
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_NAME, null, null);
	}
	
	public void delete(int id){
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_NAME, ID + "="  + "'"+ id + "'", null);
	}
	
	/**
	 * Return all the rows from the database. 
	 * @return
	 */
	public ArrayList<AutoReply> getAllAutoReply(){
		ArrayList<AutoReply> arr = new ArrayList<AutoReply>();
		SQLiteDatabase db = this.getReadableDatabase();
	      Cursor res =  db.rawQuery( "select * from AutoReplys", null );
	      res.moveToFirst();
	      while(res.isAfterLast() == false){ 
	    	  int id = Integer.parseInt(res.getString(0));
	    	  String name = res.getString(1);
	    	  String message = res.getString(2);
	    	  String status = res.getString(3);
	    	  
	    	  if(status.equalsIgnoreCase("true"))
	    		  arr.add(new AutoReply(id,name,message,true));
	    	  else
	    		  arr.add(new AutoReply(id,name,message,false));
	      res.moveToNext();
	      }
	   return arr;
	}

}
