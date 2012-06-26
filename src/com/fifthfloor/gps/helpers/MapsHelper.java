package com.fifthfloor.gps.helpers;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import com.fifthfloor.gps.server.MapReader;

public class MapsHelper {

	public  String addressToLoc(String a){
		String[] sa = a.split(" ");
		//http://maps.google.com/maps/api/geocode/json?address=BOULEVARD+JEAN+JAUR%C3%88S+PARIS&sensor=false
		String url = "http://maps.google.com/maps/api/geocode/xml?address="+sa[0]+"+"+sa[1]+"+"+sa[2]+"+"+sa[3]+"&sensor=false";
		MapReader mr = new MapReader(url);
		mr.getAddress();
		return mr.getAddress();
	}
}
