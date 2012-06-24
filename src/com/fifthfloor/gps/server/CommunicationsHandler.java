package com.fifthfloor.gps.server;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;

import org.joda.time.DateTime;
import org.vaadin.appfoundation.persistence.facade.FacadeFactory;


import com.fifthfloor.gps.server.objects.Company;
import  com.fifthfloor.gps.server.objects.SMJob;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServlet;
import java.util.logging.Logger;



public class CommunicationsHandler extends HttpServlet {
	static DateTime currenttime = new DateTime();
	private static final Logger log = Logger.getLogger(CommunicationsHandler.class.getName());

	public static void sendSMS(String number, String msgBody){
		
		System.out.println("comhandler activated");
		log.severe("can you see me?");
		Properties props = new Properties();
        Session session = Session.getDefaultInstance(props, null);

        try {
            Message msg = new MimeMessage(session);
            String n = number + "@txt.att.net";
            msg.setFrom(new InternetAddress("ashdowning@gmail.com", "Admin"));
            msg.addRecipient(Message.RecipientType.TO,
                             new InternetAddress(n, "Mr. User"));
            msg.setSubject("Your Example.com account has been activated");
            msg.setText(msgBody);
            Transport.send(msg);

        } catch (AddressException e) {
           System.out.println("ERROR SENDING EMAIL AE");
           log.severe("ERROR SENDING EMAIL AE?");
        } catch (MessagingException e) {
            System.out.println("ERROR SENDING EMAIL ME");
            log.severe("ERROR SENDING EMAIL ME");
        } catch (UnsupportedEncodingException e) {
            System.out.println("ERROR SENDING EMAIL UEE");
            log.severe("ERROR SENDING EMAIL UEE");
		}
        
        
	}
}
