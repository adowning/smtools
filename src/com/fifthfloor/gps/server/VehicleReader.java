package com.fifthfloor.gps.server;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Stack;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.jdo.PersistenceManager;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.fifthfloor.gps.server.objects.Vehicle;

public class VehicleReader extends DefaultHandler {
	private static final Logger log = Logger.getLogger(VehicleReader.class
			.getName());


	private Stack<Vehicle> vehicleStack;
	private ArrayList<Vehicle> vehicles;
	private String characters;

	public VehicleReader() {

		SAXParserFactory factory = SAXParserFactory.newInstance();
		try {

			SAXParser saxParser = factory.newSAXParser();
			saxParser.parse(new InputSource(
					"http://adowning.intouchmvc.com/readings/public"), this);

		} catch (Throwable t) {

			t.printStackTrace();

		} finally {

		}

	}	
	public ArrayList<Vehicle> getVehicles(){
		return vehicles;
	}

	public void startDocument() throws SAXException {
		vehicleStack = new Stack<Vehicle>();
		vehicles = new ArrayList<Vehicle>();

	}

	public void startElement(String namespaceURI, String localName,
			String qualifiedName, Attributes attributes) throws SAXException {

		if (qualifiedName.equals("item")) {
			Vehicle vehicle = new Vehicle();
			vehicleStack.push(vehicle);
		}

	}

	public void endElement(String namespaceURI, String simpleName,
			String qualifiedName) throws SAXException {

		if (!vehicleStack.isEmpty()) {

			if (qualifiedName.equals("item")) {

				vehicles.add(vehicleStack.pop());

			} else if (qualifiedName.equals("title")) {

				Vehicle vehicle = vehicleStack.pop();
				vehicle.setTitle(characters);
				vehicleStack.push(vehicle);

			} else if (qualifiedName.equals("description")) {

				Vehicle vehicle = vehicleStack.pop();
				vehicle.setDescription(characters);
				vehicleStack.push(vehicle);

			} else if (qualifiedName.equals("georss:point")) {

				Vehicle vehicle = vehicleStack.pop();
				vehicle.setGeorss(characters);
				vehicleStack.push(vehicle);

			} else if (qualifiedName.equals("speed")) {

				Vehicle vehicle = vehicleStack.pop();
				vehicle.setSpeed(characters);
				vehicleStack.push(vehicle);

			}
		} else if (qualifiedName.equals("direction")) {

			Vehicle vehicle = vehicleStack.pop();
			vehicle.setDirection(characters);
			vehicleStack.push(vehicle);

		} else if (qualifiedName.equals("pubDate")) {

			Vehicle vehicle = vehicleStack.pop();
			vehicle.setPubdate(characters);
			vehicleStack.push(vehicle);

		}

	}

	public void characters(char buf[], int offset, int len) throws SAXException {

		characters = new String(buf, offset, len);

	}
	
	public void setDriver(String vin, String name){
		for(Vehicle v : vehicles){
			if(v.getVin().equals(vin)){
				v.setDriver(name);
			}
		
		}
	}

}