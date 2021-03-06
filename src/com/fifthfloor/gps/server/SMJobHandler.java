package com.fifthfloor.gps.server;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;


import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.Credentials;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.params.AuthPolicy;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.joda.time.DateTime;
import org.vaadin.appfoundation.persistence.facade.FacadeFactory;
import org.xml.sax.SAXException;

import com.fifthfloor.gps.server.objects.Company;
import com.fifthfloor.gps.server.objects.SMJob;
import com.fifthfloor.gps.server.objects.Vehicle;
import com.gargoylesoftware.htmlunit.WebClient;



public class SMJobHandler {
	private static final Logger log = Logger.getLogger(SMJobHandler.class
			.getName());

	static DateTime currenttime = new DateTime();
	static int i = 0;
	
	
	public static SMJob getNextJob(Vehicle veh) {
	
		ArrayList<SMJob> joblist = TrackingServer.getJoblist();
		ArrayList<SMJob> activejobs = new ArrayList<SMJob>();
		Iterator<SMJob> it = joblist.iterator();
		//TODO add time check
		while (it.hasNext()) {
			SMJob smjob = it.next();
			if(veh.getDescription() == null){
				return null;
			}
			if (!smjob.isHasarrived() && smjob.getTechs().toLowerCase().contains(veh.getDriver().toLowerCase())) {
				return smjob;
			}else{
				if(smjob.isHasarrived()){
				System.out.println(">>>>>>>>> found arrived job");
				}
				if(!smjob.getTechs().toLowerCase().contains(veh.getDriver().toLowerCase())){
					System.out.println("technician for this job is not on record: "+ veh.getDriver().toLowerCase());
					System.out.println("for job with techs: "+ smjob.getTechs().toLowerCase());
					smjob.setTechs("none");
				}
			}
		}
		SMJob nextjob = null;
		if(activejobs.size() > 0){
			 nextjob = activejobs.get(0);

		}else{
			System.out.println("THERE ARE NO JOBS TO DO TODAY!?!");
		}

		return nextjob;

	}

	private static Company getCompany(String veh) {
		Collection<Company> clist = FacadeFactory.getFacade().list(
				Company.class);
		Iterator<Company> it = clist.iterator();
		Company thiscompany = null;
		while (it.hasNext()) {
			Company nc = it.next();
			if (nc.hasVehicle("358696045901207")) {
				thiscompany = nc;
			}

		}
		if (thiscompany == null) {
			
			log.severe("VEHICLE NOT FOUND");
		}
		return thiscompany;

	}

	

	private static SMJob getJob(String n) {

		ArrayList<SMJob> jlist = TrackingServer.getJoblist();
		Iterator<SMJob> it = jlist.iterator();
		SMJob nc = null;
		while (it.hasNext()) {
			 nc = it.next();
			if (nc.getName().equals(n)) {
				return nc;
			}

		}
		if (nc == null) {
			log.severe("JOB with name "+ n+" NOT FOUND");
		}

		return nc;
	}

	public static void setJobHasArrived(String n) {
		SMJob job = getJob(n);
		job.setHasarrived(true);
		if (job == null) {
			System.out.println("in smjobhandler : JOB NOT FOUND WHEN TRYING TO SET JOBHAS ARRIVED");
		}
	}
	
	public static void persistJobs(){
		
		ArrayList<SMJob> joblist = TrackingServer.getJoblist();
		if(joblist.size() == 0 || joblist == null){
			System.out.println("smjobhandler: joblist in tracklist is null or empty");
			return;
			
		}
		
		ArrayList<SMJob> storedjobs = (ArrayList) FacadeFactory.getFacade().list(SMJob.class);
		
		//first clear out old jobs from the day(s) before
		for(SMJob storedjob : storedjobs){
			DateTime currenttime = new DateTime();
			if(storedjob.getArrivaltime().getDayOfYear() != currenttime.getDayOfYear())	{
				FacadeFactory.getFacade().delete(storedjob);
				System.out.println("removed an oldjob from storage"+storedjob.getCustomername());
			}
		}
		//update jobs or add new ones
		boolean exists = false;
		for(SMJob newjob : joblist){
			//see if the job exists
			for(SMJob storedjob : storedjobs){
				if(storedjob.getCustomername().toLowerCase().equals(newjob.getCustomername().toLowerCase())){
					exists = true;
				}
				//see if anything changed
				if(exists){
					if(!storedjob.getAddress().toLowerCase().equals(newjob.getAddress().toLowerCase())){
						FacadeFactory.getFacade().delete(storedjob);
						FacadeFactory.getFacade().store(newjob);

					}
				}
				//add a new job in
				else{
					//before storing a new job make sure it has some techs listed, if not its probbaly a task
					if(!newjob.getTechs().equals("none")){
						FacadeFactory.getFacade().store(newjob);

					}
				}
			}
		}
	}

}
