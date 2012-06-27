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

public class JobReader extends DefaultHandler {
	private static final Logger log = Logger.getLogger(JobReader.class
			.getName());

	private Stack<SMJob> jobStack;
	private ArrayList<SMJob> jobs;
	private String characters;

	public JobReader() throws java.net.SocketTimeoutException {

		SAXParserFactory factory = SAXParserFactory.newInstance();
		try {

			SAXParser saxParser = factory.newSAXParser();
			saxParser.parse(new InputSource(
					"http://andrewscarpetcleaning.com/test.xml"), this);

		} catch (Throwable t) {
System.out.println("could not read test.xml");
			//t.printStackTrace();

		}
			finally {
		}

		

	}

	public ArrayList<SMJob> getJobs(){
		return jobs;
	}
	
	public void startDocument() throws SAXException {
		jobStack = new Stack<SMJob>();
		jobs = new ArrayList<SMJob>();

	}

	public void startElement(String namespaceURI, String localName,
			String qualifiedName, Attributes attributes) throws SAXException {

		if (qualifiedName.equals("smJob")) {
			SMJob job = new SMJob();
			jobStack.push(job);
		}

	}

	public void endElement(String namespaceURI, String simpleName,
			String qualifiedName) throws SAXException {

		if (!jobStack.isEmpty()) {

			if (qualifiedName.equals("smJob")) {

				jobs.add(jobStack.pop());

			} else if (qualifiedName.equals("address")) {

				SMJob job = jobStack.pop();
				 job.setAddress(characters);
				jobStack.push(job);

			} else if (qualifiedName.equals("date")) {

				SMJob job = jobStack.pop();
				 job.setTime(characters);
				jobStack.push(job);

			} else if (qualifiedName.equals("name")) {

				SMJob job = jobStack.pop();
				 job.setName(characters);
				jobStack.push(job);

			} else if (qualifiedName.equals("techs")) {

				SMJob job = jobStack.pop();
				job.setTechs(characters);
				jobStack.push(job);

			}

		}

	}

	public void characters(char buf[], int offset, int len) throws SAXException {

		characters = new String(buf, offset, len);

	}

}