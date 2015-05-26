package com.autosmsreply.tabs.fragment;

import com.autosmsreply.adapter.ScheduledSmsHomepageListViewAdapter;
import com.example.autosmsreply.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

/**
 * Fragment class that holds the view for Scheduled sms fragment. 
 * @author Jocke
 *
 */
public class ScheduledSmsHomePageFragment extends Fragment{
	
	private ListView scheduledSmsList;
	private Button addScheduledSmsListButton;
	private ScheduledSmsHomepageListViewAdapter adapter;
	
	@Override 
	public View onCreateView(final LayoutInflater inflater, ViewGroup containter,Bundle savedInstanceState){				
		View rootView = inflater.inflate(R.layout.fragment_scheduled_sms_homepage,containter,false);
		scheduledSmsList = (ListView) rootView.findViewById(R.id.scheduled_sms_list);
		adapter = new ScheduledSmsHomepageListViewAdapter(getActivity());
		scheduledSmsList.setAdapter(adapter);
		addScheduledSmsListButton =(Button) rootView.findViewById(R.id.add_new_scheduled_sms);
		
		addScheduledSmsListButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
            	adapter.addScheduledSmsDialog();
            }
          });
		
		scheduledSmsList.setOnItemLongClickListener(new OnItemLongClickListener() {

			/**
			 * If the user long clicks a item in the list, it updes the view. 
			 */
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int pos, long id) {
            	adapter.checkToRemove();
            	adapter.notifyDataSetChanged();
            	
                Toast.makeText(getActivity(), "Updating", Toast.LENGTH_SHORT).show();

                return true;
            }
        }); 
		
		return rootView;
	}
}
