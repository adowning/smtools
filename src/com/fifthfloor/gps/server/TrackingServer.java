package com.fifthfloor.gps.server;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.JAXBException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.vaadin.appfoundation.authentication.data.User;
import org.vaadin.appfoundation.persistence.facade.FacadeFactory;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLReaderFactory;

import com.fifthfloor.gps.server.objects.Company;
import com.fifthfloor.gps.server.objects.SMJob;


public class TrackingServer extends HttpServlet {
	private int tsloopnumber  = 0;
	public static Map vehicleLocations = new HashMap();
	
	public TrackingServer(){
		System.out.println("hai2u");
		WebClientTest.readShit();
	}
	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		resp.setContentType("text/plain");
		try {
			getVehicleLocations();

		} catch (JAXBException e) {
			System.out
			.println("ERROR = JAXB WHEN TRYING TO GET VEHICLE LOCATION");
			e.printStackTrace();
		} catch (SAXException e) {
			System.out
			.println("ERROR = SAX WHEN TRYING TO GET VEHICLE LOCATION");
			e.printStackTrace();
		}

		//This is the main loop that burns through vehicles for each company and get their travel time
		Iterator it = vehicleLocations.entrySet().iterator();
		System.out.println("big it ="+ vehicleLocations.size());
		resp.getWriter().println(
				"There are " + vehicleLocations.size()
				+ " vehicles being tracked.");
		
		while (it.hasNext()) {
			
			//resp.getWriter().println(tsloopnumber);
			tsloopnumber++;
			Map.Entry pairs = (Map.Entry) it.next();
			resp.getWriter().println(
					"vehicle " + pairs.getKey() + " is at location "
							+ pairs.getValue());
			String nextjobloc = getNextJobLoc((String) pairs.getKey());
			resp.getWriter().println(
					"traveltime for "
							+ pairs.getKey()
							+ " is "
							+ getTravelTime((String) pairs.getKey(),
									(String) pairs.getValue()));
			//check to see if we have an event
			it.remove();
		}


	}

	private void getVehicleLocations() throws IOException, JAXBException,
	SAXException {
		String uri = "http://adowning.intouchmvc.com/readings/public";
		URL url = new URL(uri);
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.setRequestMethod("GET");
		connection.setRequestProperty("Accept", "application/xml");
		XMLReader myReader = XMLReaderFactory.createXMLReader();
		myReader.setContentHandler(new HowToHandler());
		myReader.parse(new InputSource(new URL(uri).openStream()));
	}

	class HowToHandler extends DefaultHandler implements ContentHandler {

		boolean title = false;
		boolean georss = false;
		boolean url = false;
		String spoint = "";
		String vehicle = "";

		@Override
		public void startElement(String nsURI, String strippedName,
				String tagName, Attributes attributes) throws SAXException {

			if (tagName.equalsIgnoreCase("title")) {
				title = true;
			}
			if (tagName.equalsIgnoreCase("georss:point")) {
				georss = true;

			}
			if (tagName.equalsIgnoreCase("url")) {
				url = true;

			}
		}

		@Override
		public void characters(char[] ch, int start, int length) {
			if (title) {
				String line = new String(ch, start, length);
				String[] words = line.split(" ");
				if (words[0].equalsIgnoreCase("location")) {
					vehicle = words[3];
				}
			}
			if (georss) {
				spoint = new String(ch, start, length);
				setVL(spoint, vehicle);
				georss = false;
			} else if (url) {
				url = false;
			}
		}

		public void setVL(String sp, String veh) {
			if (!TrackingServer.vehicleLocations.containsKey(veh)) {
				System.out.println("setting a veh loc for veh "+veh);
				TrackingServer.vehicleLocations.put(veh, sp);
			}
		}
	}

	private String getTravelTime(String vehicle, String startLoc) {
		// https://maps.google.com/maps?q=from+37.771008,+-122.41175+to+37.62345,+-122.41175&output=html
		String toLoc = getNextJobLoc(vehicle);
		System.out.println("tsln ="+tsloopnumber);
		String[] starta = startLoc.split(" ");
		String[] enda = toLoc.split(" ");
		String url = "https://maps.google.com/maps?q=from%20" + starta[0] + ","
				+ starta[1] + "%20to%20"+ enda[0]+","+enda[1] + "&output=html";
		System.out.println(url);
		Document doc = null;
		try {
			doc = Jsoup.connect(url).get();
		} catch (IOException e) {
			System.out.println("ERROR = COULD NOT FIND GOOGLE MAPS URL");

			e.printStackTrace();
		}

		String time ="";
		try {
			time = doc.select("div.dir-altroute-inner").first().child(0).child(1).text();
		} catch (Exception e) {
			System.out.println("ERROR = COULD NOT GENERATE MAP");
			e.printStackTrace();
		}
		int hours = getHours(time);
		int minutes = getMinutes(time);
		String travelTime = (String.valueOf(hours) + "," + String.valueOf(minutes));
		String[]sa = travelTime.split(",");
		if(sa[0].equals("0") && Integer.valueOf(sa[1]) < 5){
			//SMJob job = SMJobHandler.getJob(vehicle);
			//SMJobHandler.setJobHasArrived(job.getCustomername());
		}
		
		EventHandler.checkForEvent(vehicle, travelTime);

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
				i = Integer.parseInt(sa[1].replaceAll("[^0-9]","").trim());
			} catch (ArrayIndexOutOfBoundsException e) {
				System.out.println("ERROR = COULD PARSE MINUTES");
				i = 0;
			}

		} else {
			i = Integer.parseInt(time.replaceAll("[^0-9]","").trim());
		}
		return i;

	}

	private String getNextJobLoc(String veh) {
		System.out.println("getting next job");
		SMJob job = SMJobHandler.getNextJob(veh);
		return job.getLocation();
	}

}