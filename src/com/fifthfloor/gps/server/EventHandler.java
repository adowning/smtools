package com.fifthfloor.gps.server;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.joda.time.DateTime;
import org.vaadin.appfoundation.persistence.facade.FacadeFactory;

import com.fifthfloor.gps.helpers.TimeHelper;
import com.fifthfloor.gps.server.objects.Company;
import com.fifthfloor.gps.server.objects.SMJob;
import com.fifthfloor.gps.server.objects.Vehicle;




public class EventHandler {
	static DateTime currenttime = new DateTime();
	
	public static void checkForEvent(Vehicle v,  SMJob job, String traveltime ){
		System.out.println("event handler: hit");
		checkForShortTimeAlert(v, traveltime, job );
	}
	
	
	private static void checkForShortTimeAlert(Vehicle v, String traveltime, SMJob nextjob){
		
		int difference = TimeHelper.figureTardiness(nextjob.getTime() , traveltime);
		if( difference < 5 ){
			System.out.println("eventhandler >> your going to be late by " + difference);
		}else{
			System.out.println("eventhandler >> your going to be ontime by "+difference);

		}
//		DateTime arrivaltime = nextjob.getArrivaltime();
//		DateTime eta = addTravelTimeAndCurrentTime(traveltime);
//		if(arrivaltime.getDayOfYear() != eta.getDayOfYear()){
//			System.out.println("1");
//			System.out.println("ERROR: YOUR ETA AND JOB ARRIVAL TIME DAYS ARE NOT MATCHING UP!!!");
//			return;
//		}
//		if(eta.isAfter(arrivaltime)){
//			System.out.println("2");
//
//			int minuteslate = eta.getMinuteOfDay() - arrivaltime.getMinuteOfDay();
//			System.out.println("truck "+v.getVin()+" is going to be late by "+ minuteslate +" minutes, sending alert");
//			sendAlert(v.getVin(), nextjob, minuteslate);
//
//		}else{
//			System.out.println("3");
//
//			int minutesearly = arrivaltime.getMinuteOfDay() - eta.getMinuteOfDay();
//			System.out.println("truck "+v.getVin()+" is going to be ontime by "+ minutesearly + " minutes");
//
//		}
	}
	
	private static DateTime addTravelTimeAndCurrentTime(String traveltime){
		DateTime eta = null;
		String[] sa = traveltime.split(",");
		eta = currenttime;
		eta = eta.plusHours(Integer.valueOf(sa[0]));
		eta = eta.plusMinutes(Integer.valueOf(sa[1]));
		return eta;
		
	}
	
	private static void sendAlert(String veh, SMJob nextjob, int minuteslate){
		//check if sms has been sent for this job
		if(nextjob.isAlertsent()){
			return;
		}
		//if no sms has been sent, find sms numbers to send to
		Collection<Company> clist = FacadeFactory.getFacade().list(Company.class);
		Iterator it = clist.iterator();
		Company ic = null;
		Company thiscompany = null;
		while(it.hasNext()){
			ic = (Company) it.next();
			if(ic.hasVehicle(veh)){
				thiscompany = ic;
				it.remove();
				break;
			}
		}
		if(thiscompany == null){
			System.out.println("ERROR : COMPANY NOT FOUND");
			return;
		}
		
		LinkedList smslist = thiscompany.getSmsList();
		//send sms
		it = smslist.iterator();
		while(it.hasNext()){
			String smsnumber = (String) it.next();
			String msg = "Vehicle "+ veh + " is going to be late to "+nextjob.getCustomername() + " by " + String.valueOf(minuteslate);
			//CommunicationsHandler.sendSMS(smsnumber, msg);
			
		}
		//flag that sms has been sent
		
		
	}
}
