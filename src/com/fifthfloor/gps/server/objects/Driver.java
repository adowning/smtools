package com.fifthfloor.gps.server.objects;

public class Driver {
	
	private Company company;
	public Driver(String name, String vehicle, Company france){
		this.name = name;
		this.vehicle = vehicle;
		this.company = france;
	}
	
	public Driver(String name, String vehicle){
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
