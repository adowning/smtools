package com.fifthfloor.gps.server.objects;

import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.vaadin.appfoundation.persistence.data.AbstractPojo;


@Entity
@Table(name = "driver", uniqueConstraints = { @UniqueConstraint(columnNames = { "id" } ) })
public class Driver extends AbstractPojo {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Company company;
	private UUID id;
	
	
	public Driver(String name, String vehicle, Company france){
		UUID idone = UUID.randomUUID();
		this.id = idone;
		this.name = name;
		this.vehicle = vehicle;
		this.company = france;
	}
	
	public Driver(String name, String vehicle){
		UUID idone = UUID.randomUUID();
		this.id = idone;
		this.name = name;
		this.vehicle = vehicle;
	}
	
	
	
	private String name;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getVehicle() {
		return vehicle;
	}
	public void setVehicle(String vehicle) {
		this.vehicle = vehicle;
	}
	private String vehicle;
	
}
