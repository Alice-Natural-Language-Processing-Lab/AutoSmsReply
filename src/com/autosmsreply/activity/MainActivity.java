package com.autosmsreply.activity;


import com.autosmsreply.adapter.TabsPagerAdapter;
import com.example.autosmsreply.PhotoLoader;
import com.example.autosmsreply.R;
import com.example.autosmsreply.Receiver;
import com.example.autosmsreply.StaticNextScheduledSmsInfo;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

/**
 * This is the main activity class, it holds all the tabs. 
 * Tabs implement a adapter that handles there action.
 * @author Jocke
 *
 */
@SuppressLint("NewApi") 
public class MainActivity extends FragmentActivity implements
		ActionBar.TabListener {
	
	private final int CONTACT_CHOOSER = 0;

	private ViewPager viewPager;
	private TabsPagerAdapter tabsAdapter;
	private ActionBar actionBar;
	private final String[] tabs = { "Auto replys","Scheduled Sms"};
	
	Receiver r = new Receiver();
	
	StaticNextScheduledSmsInfo contactInfo = new StaticNextScheduledSmsInfo();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		viewPager = (ViewPager) findViewById(R.id.pager);
		actionBar = getActionBar();
		tabsAdapter = new TabsPagerAdapter(getSupportFragmentManager());
		viewPager.setAdapter(tabsAdapter);
		
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);	
		actionBar.setDisplayShowHomeEnabled(false);
		actionBar.setDisplayShowCustomEnabled(true);
		
		LayoutInflater inflator = (LayoutInflater) this .getSystemService(this.LAYOUT_INFLATER_SERVICE);
		View v = inflator.inflate(R.layout.action_bar_layout, null);
		actionBar.setCustomView(v);
		
		for (String tab_name : tabs) {
			actionBar.addTab(actionBar.newTab().setText(tab_name)
					.setTabListener(this));
		}

		viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

			@Override
			public void onPageSelected(int position) {
				actionBar.setSelectedNavigationItem(position);
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
			}
		});
	}

	@Override
	public void onTabReselected(Tab tab, FragmentTransaction ft) {
	}

	@Override
	public void onTabSelected(Tab tab, FragmentTransaction ft) {
		viewPager.setCurrentItem(tab.getPosition());
	}

	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction ft) {
	}
	
	@Override
	protected void onDestroy() {		
//		ComponentName receiver = new ComponentName(this, Receiver.class);
//		   PackageManager pm = this.getPackageManager();
//		   pm.setComponentEnabledSetting(receiver,
//		           PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
//		           PackageManager.DONT_KILL_APP);
	}
	
	/**
	 * Start the contact chooser intent. Result is handled in onActivityResult, with resultCode CONTACT_CHOOSER;
	 * @param v
	 */
	public void chooseContact(View v){
		Intent intent = new Intent(Intent.ACTION_PICK);
		   intent.setType(ContactsContract.Contacts.CONTENT_TYPE);
		  startActivityForResult(intent, CONTACT_CHOOSER); 
	}
	
	/**
	 * This method handles the result of the contact chooser. 
	 * When a user schedules a sms they pick a contact, this method handles that. 
	 */
	@Override  
	protected void onActivityResult(int requestCode, int resultCode, Intent data) { 
		if(resultCode == RESULT_OK){
			if(requestCode == CONTACT_CHOOSER){
				Cursor cursor = null;
				String nameOfContact = null;
				String numberOfContact = null;
				String contactImageUri = null;
				int contactId = 0;
				 try {
				Uri result = data.getData();  
                String id = result.getLastPathSegment();  
                cursor = getContentResolver().query(Phone.CONTENT_URI, null, Phone.CONTACT_ID + "=?", new String[] { id }, null);  
                contactId = cursor.getColumnIndex(Phone.DISPLAY_NAME_PRIMARY);
                if (cursor.moveToFirst()) {
                      nameOfContact = cursor.getString(contactId);
                      int j = cursor.getColumnIndex(Phone.PHOTO_THUMBNAIL_URI);
                      int k = cursor.getColumnIndex(Phone.NUMBER);
                      numberOfContact = cursor.getString(k);
                      contactImageUri = cursor.getString(j);
                      cursor.moveToNext();
				}
                if(nameOfContact == null)
                	Toast.makeText(this,"A contact wasnt chosen." , Toast.LENGTH_SHORT).show();
                else if(nameOfContact.equalsIgnoreCase(""))
                	Toast.makeText(this,"Contact has bad info" , Toast.LENGTH_SHORT).show();
                else{
                	Toast.makeText(this,nameOfContact + " was chosen" , Toast.LENGTH_SHORT).show();
                	StaticNextScheduledSmsInfo.name = nameOfContact;
                	StaticNextScheduledSmsInfo.number = numberOfContact;
                	//Photoloader is a class that handles the image retrieval from the contact URI. 
                	PhotoLoader ph = new PhotoLoader(this);
                	StaticNextScheduledSmsInfo.image = ph.loadContactPhotoThumbnail(contactImageUri);
                	StaticNextScheduledSmsInfo.uriToContactImage = contactImageUri;
                }
				 } catch (Exception e) {  
	            }
				
			}
		}
	}
}
