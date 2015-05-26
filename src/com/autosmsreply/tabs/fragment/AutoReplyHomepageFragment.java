package com.autosmsreply.tabs.fragment;


import com.autosmsreply.adapter.AutoReplyHomepageListViewAdapter;
import com.example.autosmsreply.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

/**
 * A fragment class the holds the view of AutoreplyHompage. 
 * @author Jocke
 *
 */
public class AutoReplyHomepageFragment extends Fragment{
	
	private ListView autoReplyList;
	private Button addAutoReplyItem;
	private AutoReplyHomepageListViewAdapter adapter;
	
	@Override 
	public View onCreateView(final LayoutInflater inflater, ViewGroup containter,Bundle savedInstanceState){
				
		View rootView = inflater.inflate(R.layout.fragment_auto_reply_homepage,containter,false);		
		autoReplyList = (ListView) rootView.findViewById(R.id.auto_replys_list);
		autoReplyList.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		autoReplyList.setAdapter(adapter);
		
		adapter = new AutoReplyHomepageListViewAdapter(getActivity());
		autoReplyList.setAdapter(adapter);
		addAutoReplyItem = (Button) rootView.findViewById(R.id.add_new_auto_reply);
		
		addAutoReplyItem.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
            	adapter.showAddAutoReplyDialog();
            }
          });
		
		return rootView;
	}
	
	
}
