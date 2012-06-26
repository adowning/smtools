package com.fifthfloor.gps.helpers;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;


public class TimeHelper {

	
	public static int figureTardiness(String timeofnextjob, String traveltime){
		int i = 0;
		int hours_add = 0;
		int minutes_add = 0;
		//current date time
		String[]sa = timeofnextjob.split(" ");
		String tonj = sa[0];
		String[]sa2 = tonj.split(":");
		String hour = sa2[0];
		String minute = sa2[1];
		
		String dateString_of_nextjob = "2012-06-25 "+tonj+":00";

		// parse the string
		DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
		DateTime current_time_plus_traveltime = new DateTime();
		System.out.println("current_time "+ current_time_plus_traveltime);
		DateTime next_job_time = formatter.parseDateTime(dateString_of_nextjob);
		System.out.println("dateString_of_nextjob "+ next_job_time);

		String[] sa_tt = traveltime.split(",");
		System.out.println("travel time ="+ traveltime);
		if(!sa_tt[0].equals("0")){
			hours_add += Integer.valueOf(sa_tt[0]);
			current_time_plus_traveltime = current_time_plus_traveltime.plusHours(hours_add); // easier than mucking about with Calendar and constants
			System.out.println("current_time_plus_traveltime with hours added "+ current_time_plus_traveltime);

		}
		if(!sa_tt[1].equals("0")){
			minutes_add += Integer.valueOf(sa_tt[1]);
			current_time_plus_traveltime = current_time_plus_traveltime.plusMinutes(minutes_add); // easier than mucking about with Calendar and constants
			System.out.println("current_time_plus_traveltime with minutes added"+ current_time_plus_traveltime);

		}

		DateTime currenttime = new DateTime();
		int difference = next_job_time.getMinuteOfDay() - current_time_plus_traveltime.getMinuteOfDay() ;	
		return difference;
		
	}
}
