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
import com.gargoylesoftware.htmlunit.WebClient;



public class SMJobHandler {
	private static final Logger log = Logger.getLogger(SMJobHandler.class
			.getName());

	static DateTime currenttime = new DateTime();
	static int i = 0;
	public static SMJob getNextJob(String veh) {
		i++;
		System.out.println(i);
		LinkedList joblist = getListForToday(veh);
		LinkedList activejobs = new LinkedList();
		Iterator it = joblist.iterator();
		while (it.hasNext()) {
			SMJob smjob = (SMJob) it.next();
			if (!smjob.isHasarrived()) {
				activejobs.add(smjob);
			}else{
				System.out.println(">>>>>>>>> found arrived job");
			}
		}
		SMJob nextjob = (SMJob) activejobs.getFirst();

		return nextjob;

	}

	private static Company getCompany(String veh) {
		Collection<Company> clist = FacadeFactory.getFacade().list(
				Company.class);
		Iterator it = clist.iterator();
		System.out.println(clist.size());
		Company thiscompany = null;
		while (it.hasNext()) {
			Company nc = (Company) it.next();
			if (nc.hasVehicle("358696045901207")) {
				thiscompany = nc;
			}

		}
		if (thiscompany == null) {
			
			log.severe("VEHICLE NOT FOUND");
		}
		return thiscompany;

	}

	private static LinkedList getListForToday(String veh) {
		// TODO fix for SMAPI
		//Company comp = getCompany(veh);
		
		LinkedList l = new LinkedList();
		SMJob a = new SMJob(currenttime.plusMinutes(47));
		a.setCustomername("a");
		a.setLocation("32.307322 -95.264832");
		a.setVehicle(veh);
		l.add(a);
		SMJob b = new SMJob(currenttime.plusHours(3));
		b.setCustomername("b");
		b.setLocation("32.318892 -95.246658");
		b.setVehicle(veh);

		l.add(b);
		SMJob c = new SMJob(currenttime.plusHours(6));
		c.setCustomername("c");
		c.setLocation("32.226821 -95.225504");
		c.setVehicle(veh);

		l.add(c);
		SMJob d = new SMJob(currenttime.plusHours(9));
		d.setCustomername("d");
		d.setLocation("32.515697 -95.409400");
		d.setVehicle(veh);

		l.add(d);
		return l;

	}

	private static SMJob getJob(String veh) {

		Collection<SMJob> jlist = getListForToday(veh);
		Iterator it = jlist.iterator();
		SMJob job = null;
		while (it.hasNext()) {
			SMJob nc = (SMJob) it.next();
			if (nc.getVehicle().equals(veh)) {
				job = nc;
				break;
			}

		}
		if (job == null) {
			log.severe("JOB NOT FOUND");
		}

		return job;
	}

	public static void setJobHasArrived(String veh) {
		SMJob job = getJob(veh);
		job.setHasarrived(true);
		if (job == null) {
			System.out.println("JOB NOT FOUND");
			log.severe("JOB NOT FOUND");
		}
	}
	

}
