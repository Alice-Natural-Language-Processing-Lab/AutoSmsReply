package com.example.autosmsreply;

import java.util.Comparator;

import com.autosmsreply.assets.ScheduledSmsItem;

/**
 * Simple comparator class that is used to sort the ArrayList of ScheduledsmsItems.
 * @author Jocke
 *
 */
public class CustomComparator implements Comparator<ScheduledSmsItem>{

	@Override
	public int compare(ScheduledSmsItem s1, ScheduledSmsItem s2) {
		
		int year = s1.year - s2.year;
		if(year != 0)
			return year;
		int month = s1.month - s2.month;
		if(month != 0)
			return month;
		int day = s1.day - s2.day;
		if(day != 0)
			return day;
		int hour = s1.hour - s2.hour;
		if(hour!= 0)
			return hour;
		return s1.minute - s2.minute;
	}
	
}
