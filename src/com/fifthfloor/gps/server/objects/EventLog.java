package com.fifthfloor.gps.server.objects;

import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.joda.time.DateTime;
import org.vaadin.appfoundation.persistence.data.AbstractPojo;


@Entity
@Table(name = "eventlog", uniqueConstraints = { @UniqueConstraint(columnNames = { "id" } ) })
public class EventLog extends AbstractPojo {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	String driver;
	SMJob job;
	Company company;
	Vehicle vehicle;
	String type;
	DateTime time;
	UUID id;
	
	public EventLog(){
		UUID idone = UUID.randomUUID();
		this.id = idone;
	}
	public EventLog(Vehicle v, SMJob j, String t, DateTime dt){
		UUID idone = UUID.randomUUID();
		this.id = idone;
		
		this.driver = v.getDriver();
		this.job = j;
		this.time = dt;
		this.type = t;
		
	}
	
	
	public String getDriver() {
		return driver;
	}

	public void setDriver(String driver) {
		this.driver = driver;
	}

	public SMJob getJob() {
		return job;
	}

	public void setJob(SMJob job) {
		this.job = job;
	}

	public Company getCompany() {
		return company;
	}

	public void setCompany(Company company) {
		this.company = company;
	}

	public Vehicle getVehicle() {
		return vehicle;
	}

	public void setVehicle(Vehicle vehicle) {
		this.vehicle = vehicle;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public DateTime getTime() {
		return time;
	}

	public void setTime(DateTime time) {
		this.time = time;
	}

	

	public void setId(UUID id) {
		this.id = id;
	}


}
