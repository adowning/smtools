package com.fifthfloor.gps.server;


import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class SMJob {
	String date = "";
	String name = "";
	String techs = "";
	String address = "";
	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTechs() {
		return techs;
	}

	public void setTechs(String techs) {
		this.techs = techs;
	}

	
	public SMJob(){
		
	}
	
	public SMJob(String s){
		String[] sa = s.split(";");
		this.date = sa[0];
		this.name = sa[1];
		this.address = sa[2];
		this.techs = sa[3];
		
	}
	
	
}
