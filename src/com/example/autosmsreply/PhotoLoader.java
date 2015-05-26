package com.example.autosmsreply;

import java.io.FileDescriptor;
import java.io.FileNotFoundException;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.provider.Contacts;
import android.provider.ContactsContract.Contacts.Photo;

/**
 * A simple class that is used to return a bitmap to the image of a contact. 
 * @author Jocke
 *
 */
public class PhotoLoader {
	
	private Context context;
	
	public PhotoLoader(Context context){
		this.context = context;
	}
	
	public Bitmap loadContactPhotoThumbnail(String photoData) {
        AssetFileDescriptor assetFileDescriptor = null;
        if(photoData != ""){
        try {
            Uri contactThumbImageUri;
            
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
                contactThumbImageUri = Uri.parse(photoData);
            else {
                final Uri contactUri = Uri.withAppendedPath(Contacts.CONTENT_URI, photoData);
                contactThumbImageUri = Uri.withAppendedPath(contactUri, Photo.CONTENT_DIRECTORY);
            }
            	assetFileDescriptor = context.getContentResolver().openAssetFileDescriptor(contactThumbImageUri, "r");
            	FileDescriptor fileDescriptor = assetFileDescriptor.getFileDescriptor();
            	
            if (fileDescriptor != null) {
            	return BitmapFactory.decodeFileDescriptor(fileDescriptor, null, null);
            }
            
        } catch (FileNotFoundException e) {
        	
        }
        
    	
       }
        return null;
    }
        
}
