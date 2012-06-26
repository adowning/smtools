package com.fifthfloor.gps.server;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import com.fifthfloor.gps.helpers.TimeHelper;
import com.fifthfloor.gps.server.objects.SMJob;
import com.fifthfloor.gps.server.objects.Vehicle;

public class TrackingServer extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int tsloopnumber = 0;
	public static Map vehicleLocations = new HashMap();
	public static ArrayList<SMJob> joblist = new ArrayList();
	public static ArrayList<Vehicle> vehiclelist = new ArrayList();

	public TrackingServer() {

		JobReader jr = null;
		try {
			jr = new JobReader();
		} catch (SocketTimeoutException e1) {
			e1.printStackTrace();
		}
		joblist = jr.getJobs();
		VehicleReader vr = new VehicleReader();
		vehiclelist = vr.getVehicles();
		setupVehicles(vr);

		// THIS IS STARTUP LOOP
		for (Vehicle v : vehiclelist) {
			try {
				SMJob job = SMJobHandler.getNextJob(v);
				System.out.println("tracking server: time of next job = "
						+ job.getTime());
				
				System.out.println("tracking server: travel time to next job "
						+ getTraveltimeToNextJob(v));
				
				//TimeHelper.figureTardiness(job.getTime(), getTraveltimeToNextJob(v));
				EventHandler.checkForEvent(v, job, getTraveltimeToNextJob(v));
			} catch (NullPointerException e) {
				System.out.println("tracking server: "
						+ "NULLLLLLITIOUS");
			}
		}

	}

	private void updateServer() {
		// TODO THIS WILL BE THE UPDATE LOOP
	}

	public static ArrayList getJoblist() {
		return joblist;
	}

	private String getTraveltimeToNextJob(Vehicle v) {
		// https://maps.google.com/maps?q=from+37.771008,+-122.41175+to+37.62345,+-122.41175&output=html
		SMJob job = SMJobHandler.getNextJob(v);

		String toLoc;
		try {
			toLoc = job.getLocation();
		} catch (NullPointerException e) {
			System.out.println("tracking server:  NO LOCATION FOUND FOR JOB");
			return null;
		}
		String[] starta = v.getGeorss().split(" ");
		String[] enda = toLoc.split(" ");
		String url = "https://maps.google.com/maps?q=from%20" + starta[0] + ","
				+ starta[1] + "%20to%20" + enda[0] + "," + enda[1]
				+ "&output=html";
		Document doc = null;
		try {
			doc = Jsoup.connect(url).get();
		} catch (IOException e) {
			System.out.println("ERROR = COULD NOT FIND GOOGLE MAPS URL");

			e.printStackTrace();
		}

		String time = "";
		try {
			time = doc.select("div.dir-altroute-inner").first().child(0)
					.child(1).text();
		} catch (Exception e) {
			System.out.println("ERROR = COULD NOT GENERATE MAP");
			e.printStackTrace();
		}
		int hours = getHours(time);
		int minutes = getMinutes(time);
		String travelTime = (String.valueOf(hours) + "," + String
				.valueOf(minutes));
		String[] sa = travelTime.split(",");
		if (sa[0].equals("0") && Integer.valueOf(sa[1]) < 5) {

			SMJobHandler.setJobHasArrived(job.getName());

		}

		// TODO add me in
		// EventHandler.checkForEvent(v, travelTime);

		return travelTime;
	}

	private int getHours(String time) {
		int i = 0;
		if (time.contains("hours")) {
			String[] sa = time.split("hours");
			i = Integer.parseInt(sa[0].replaceAll("/[^0-9]/g", "").trim());

		}
		return i;

	}

	private int getMinutes(String time) {
		int i = 0;
		if (time.contains("hours")) {
			String[] sa = time.split("hours");
			try {
				i = Integer.parseInt(sa[1].replaceAll("[^0-9]", "").trim());
			} catch (ArrayIndexOutOfBoundsException e) {
				System.out.println("ERROR = COULD PARSE MINUTES");
				i = 0;
			}

		} else {
			i = Integer.parseInt(time.replaceAll("[^0-9]", "").trim());
		}
		return i;

	}

	private void setupVehicles(VehicleReader vr) {
		try {
			for (Vehicle v : vr.getVehicles()) {
				String[] words = v.getTitle().split(" ");
				String vin = words[3];
				// TODO fix me
				if (vin.equals("358696045901207")) {
					vr.setDriver(vin, "James");
				}
			}
		} catch (NullPointerException e) {
			//we know there is a fuckup , uncomment syso later
			//System.out.println("tracking server: not valid vehicle");
		}
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

	// /////////////////////////////////
	// // WEBSHIT
	// /////////////////////////////////

	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		resp.setContentType("text/plain");
		// try {
		// getVehicleLocations();
		//
		// } catch (JAXBException e) {
		// System.out
		// .println("ERROR = JAXB WHEN TRYING TO GET VEHICLE LOCATION");
		// e.printStackTrace();
		// } catch (SAXException e) {
		// System.out
		// .println("ERROR = SAX WHEN TRYING TO GET VEHICLE LOCATION");
		// e.printStackTrace();
		// }

		// This is the main loop that burns through vehicles for each company
		// and get their travel time
		Iterator it = vehicleLocations.entrySet().iterator();
		resp.getWriter().println(
				"There are " + vehicleLocations.size()
						+ " vehicles being tracked.");

		// while (it.hasNext()) {
		//
		// //resp.getWriter().println(tsloopnumber);
		// tsloopnumber++;
		// Map.Entry pairs = (Map.Entry) it.next();
		// resp.getWriter().println(
		// "vehicle " + pairs.getKey() + " is at location "
		// + pairs.getValue());
		// String nextjobloc = getNextJobLoc((String) pairs.getKey());
		// resp.getWriter().println(
		// "traveltime for "
		// + pairs.getKey()
		// + " is "
		// + getTravelTime((String) pairs.getKey(),
		// (String) pairs.getValue()));
		// //check to see if we have an event
		// it.remove();
		// }

	}

}
