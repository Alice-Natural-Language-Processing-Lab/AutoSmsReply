package com.autosmsreply.adapter;

import java.util.ArrayList;


import com.autosmsreply.assets.AutoReply;
import com.autosmsreply.assets.AutoReplyDbHelper;
import com.example.autosmsreply.R;
import com.example.autosmsreply.StaticSmsInfo;

import android.app.AlertDialog;
import android.support.v4.app.FragmentActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

/**
 * This class represents the adapter for the items in the AutoReplyHompageListViewFragment. 
 * @author Jocke
 *
 */
public class AutoReplyHomepageListViewAdapter extends BaseAdapter{
	
	private Context context;
	private static LayoutInflater inflater = null;
	
	private TextView autoReplyMessageName;
	private TextView autoReplyMessage;
	private CheckBox autoReplyStatus;
	
	private AutoReplyDbHelper dbHelper;
	private ArrayList<AutoReply> autoReplyList;	
	private ArrayAdapter<AutoReply> autoReplyArrayAdapter;
	
	int selectedIndex = -1;
	
	public AutoReplyHomepageListViewAdapter(Context context){
		this.context = (FragmentActivity) context;
		inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		dbHelper = new AutoReplyDbHelper(context);
		autoReplyList = dbHelper.getAllAutoReply();
	}
	
	/**
	 * Get data from the Database. Then update the view. 
	 */
	public void updatedData() {
		autoReplyList = dbHelper.getAllAutoReply();		
		if(autoReplyList.size() >=1) {
			for(AutoReply autoReplyFromList : autoReplyList)
				if(autoReplyFromList!=null && autoReplyArrayAdapter != null){
					autoReplyArrayAdapter.add(autoReplyFromList);
				}
		}
		this.notifyDataSetChanged();
	}
	
	@Override
	public int getCount() {
		if(autoReplyList != null)
			return autoReplyList.size();
		else 
			return 0;
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int arg0) {
		int i = autoReplyList.indexOf(autoReplyList.get(arg0));
		return i;
	}

	@Override
	public View getView(final int id, View arg1, ViewGroup arg2) {
		final View rowView = inflater.inflate(R.layout.fragment_auto_reply_homepage_single_list_item,null);
		autoReplyArrayAdapter = new ArrayAdapter<AutoReply>(context, R.layout.fragment_auto_reply_homepage_single_list_item,autoReplyList);
		autoReplyMessageName = (TextView) rowView.findViewById(R.id.auto_reply_name);
		autoReplyMessage = (TextView) rowView.findViewById(R.id.auto_reply_message);
		autoReplyStatus = (CheckBox) rowView.findViewById(R.id.auto_reply_status);
		
		final AutoReply temp = autoReplyList.get(id);
		autoReplyMessageName.setText(temp.autoReplyName);
		autoReplyMessage.setText(temp.autoReplyMessage);
		
		rowView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
            	deleteDialog(temp, id);
            }
          });
		
		autoReplyStatus.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				setSelectedIndex(id);
				updatedData();
			}
		});
		
		if(selectedIndex == id){
			autoReplyStatus.setChecked(true);
		}else
			autoReplyStatus.setChecked(false);
	
		return rowView;
	}
	
	/**
	 * if the user decides to remove a list item then a dialog will apear.
	 * If yes is chosen the item will be removed from the view and database. 
	 * @param toRemove
	 * @param id
	 */
	private void deleteDialog(final AutoReply toRemove, final int id){
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setMessage("Delete " + "'" + toRemove.autoReplyName + "'" + " AutoReplyMessage?")
						.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								dbHelper.delete(toRemove.id);
								updatedData();
							}
						})
						.setNegativeButton("No", new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								
							}
						}).show();
	}

	/**
	 * The selected index is the sms that will be sent as a reply to any incoming messages. 
	 * @param index
	 */
	public void setSelectedIndex(int index){
		AutoReply a = autoReplyList.get(index);
		StaticSmsInfo.messageToSendBackToLatestReceived = a.autoReplyMessage;	
	    selectedIndex = index;
	}
	
	
	public void showAddAutoReplyDialog(){
		autoReplyDialog();
	}
	
	public void addAutoReplyItem(String name, String message, String status){
		dbHelper.insertAutoReply(name, message, status);
		updatedData();
	}
	
	/**
	 * If the user wants to add a item a custom dialog will apear. 
	 * Here the user can insert info and decide to save or not. 
	 */
	public void autoReplyDialog(){
		LayoutInflater linf = LayoutInflater.from(context);            
		final View inflator = linf.inflate(R.layout.dialog_add_auto_reply, null);
		AlertDialog.Builder alert = new AlertDialog.Builder(context); 
		alert.setView(inflator); 
		final EditText name = (EditText) inflator.findViewById(R.id.add_name);
		final EditText message = (EditText) inflator.findViewById(R.id.add_message);

		alert.setPositiveButton("Add", new DialogInterface.OnClickListener() { 
		   public void onClick(DialogInterface dialog, int whichButton) 
		   { 
			   String nameString=name.getText().toString();
		       String messageString=message.getText().toString();
		       addAutoReplyItem(nameString, messageString, "true");
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
