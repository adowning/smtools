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
		System.out.println("string time of next job is "+ timeofnextjob);
		int h = 8;
		String dateString = "2012-06-25 "+String.valueOf(h)+":41:33";

		// parse the string
		DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
		DateTime dt = formatter.parseDateTime(dateString);

		String[] sa_tt = traveltime.split(",");
		if(!sa_tt[0].equals("0")){
			hours_add += Integer.valueOf(sa_tt[0]);
			dt = dt.plusHours(hours_add); // easier than mucking about with Calendar and constants

		}
		if(!sa_tt[1].equals("0")){
			minutes_add += Integer.valueOf(sa_tt[1]);
			dt = dt.plusMinutes(minutes_add); // easier than mucking about with Calendar and constants

		}
		System.out.println(dt);

		DateTime currenttime = new DateTime();
		int difference =  dt.getMinuteOfDay() - currenttime.getMinuteOfDay() ;	
		return difference;
		
	}
}
