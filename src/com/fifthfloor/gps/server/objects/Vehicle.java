package com.fifthfloor.gps.server.objects;

import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.vaadin.appfoundation.persistence.data.AbstractPojo;



@Entity
@Table(name = "driver", uniqueConstraints = { @UniqueConstraint(columnNames = { "id" } ) })
public class Vehicle extends AbstractPojo {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String title;
	private String description;
	private String georss;
	private String speed;
	private String direction;
	private String pubdate;
	private String driver;
	private String vin;
	private UUID id;
	
	public Vehicle(){
		UUID idone = UUID.randomUUID();
		this.id = idone;
	}
	public String getVin() {
		return vin;
	}
	public String getDriver() {
		return driver;
	}
	public void setDriver(String driver) {
		this.driver = driver;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		String[] words = title.split(" ");
		this.vin = words[3];
		this.title = title;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		

		this.description = description;
	}
	public String getGeorss() {
		return georss;
	}
	public void setGeorss(String georss) {
		this.georss = georss;
	}
	public String getSpeed() {
		return speed;
	}
	public void setSpeed(String speed) {
		this.speed = speed;
	}
	public String getDirection() {
		return direction;
	}
	public void setDirection(String direction) {
		this.direction = direction;
	}
	public String getPubdate() {
		return pubdate;
	}
	public void setPubdate(String pubdate) {
		this.pubdate = pubdate;
	}

	
}
