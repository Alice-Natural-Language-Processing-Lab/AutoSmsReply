package com.autosmsreply.adapter;


import com.autosmsreply.tabs.fragment.AutoReplyHomepageFragment;
import com.autosmsreply.tabs.fragment.ScheduledSmsHomePageFragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
 
/**
 * This class is used as a adapter to the tabs, it starts diffrent 
 * fragments depending on the tab.
 * @author Jocke
 *
 */
public class TabsPagerAdapter extends FragmentPagerAdapter {
 
    public TabsPagerAdapter(FragmentManager fm) {
        super(fm);
    }
 
    @Override
    public Fragment getItem(int index) { 
        switch (index) {
        case 0:
        	return new AutoReplyHomepageFragment();
        case 1:
        	return new ScheduledSmsHomePageFragment();
        }
 
        return null;
    }
 
    @Override
    public int getCount() {
        return 2;
    }
 
}
