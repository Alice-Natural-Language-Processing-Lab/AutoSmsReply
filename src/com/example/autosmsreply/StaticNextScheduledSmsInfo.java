package com.example.autosmsreply;

import android.graphics.Bitmap;

/**
 * Simple helper class that holds information about the next sms to be sent. 
 * @author Jocke
 *
 */
public class StaticNextScheduledSmsInfo {
	public static String name = "";
	public static String number = "";
	public static String message = "";
	public static Bitmap image = null;
	public static String uriToContactImage = "";
	
	public void eraseAll(){
		name = "";
		number = "";
		message = "";
		image =null;
		uriToContactImage = "";
	}
}
