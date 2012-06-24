package com.fifthfloor.gps.server.objects;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.joda.time.DateTime;
import org.vaadin.appfoundation.persistence.data.AbstractPojo;

@Entity
@Table(name = "appSMJob", uniqueConstraints = { @UniqueConstraint(columnNames = { "customername" }) })
public class SMJob extends AbstractPojo {
	private DateTime arrivaltime = null;
	private String customername = "";
	private boolean hasarrived = false;
	private String location = "";
	private String vehicle = "";
	
	public String getVehicle() {
		return vehicle;
	}

	public void setVehicle(String vehicle) {
		this.vehicle = vehicle;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public boolean isHasarrived() {
		return hasarrived;
	}

	public void setHasarrived(boolean hasarrived) {
		this.hasarrived = hasarrived;
	}

	public String getCustomername() {
		return customername;
	}

	public void setCustomername(String customername) {
		this.customername = customername;
	}

	public DateTime getArrivaltime() {
		return arrivaltime;
	}

	public void setArrivaltime(DateTime arrivaltime) {
		this.arrivaltime = arrivaltime;
	}

	private boolean alertsent = false;

	public boolean isAlertsent() {
		return alertsent;
	}

	public void setAlertsent(boolean alertsent) {
		this.alertsent = alertsent;
	}

	public SMJob(DateTime at) {
		arrivaltime = at;
	}

}
