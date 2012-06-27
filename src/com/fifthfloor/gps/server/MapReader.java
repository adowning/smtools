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

import com.fifthfloor.gps.server.objects.SMJob;
import com.fifthfloor.gps.server.objects.Vehicle;

public class MapReader extends DefaultHandler {
	private static final Logger log = Logger.getLogger(JobReader.class
			.getName());
	private String characters;
	private String lat;
	private String longitude;

	public MapReader(String url) {

		SAXParserFactory factory = SAXParserFactory.newInstance();
		try {
			SAXParser saxParser = factory.newSAXParser();
			saxParser.parse(new InputSource(url), this);

		} catch (Throwable t) {

			System.out.println("MAP READER HAD ISSUE GETTING URL :"+url);
		} finally {

		}

	}

	public String getAddress() {
		return lat  + " " + longitude;
	}

	public void startDocument() throws SAXException {

	}

	public void startElement(String namespaceURI, String localName,
			String qualifiedName, Attributes attributes) throws SAXException {

		if (qualifiedName.equals("GeocodeResponse")) {
		}

	}

	public void endElement(String namespaceURI, String simpleName,
			String qualifiedName) throws SAXException {

		if (qualifiedName.equals("lat")) {
			lat = characters;

		}
		if (qualifiedName.equals("lng")) {
			longitude = characters;

		}

	}

	public void characters(char buf[], int offset, int len) throws SAXException {

		characters = new String(buf, offset, len);

	}

}