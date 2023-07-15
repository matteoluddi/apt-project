package com.luddi.matteo.airport.model;

import java.util.Objects;

public class Flight {

	private String id;
	private String destination;
	private int passengersNumber;
	
	public Flight() {
		
	}
	
	public Flight(String id, String destination, int passengersNumber) {
		super();
		this.id = id;
		this.destination = destination;
		this.passengersNumber = passengersNumber;
	}

	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	

	public int getPassengersNumber() {
		return passengersNumber;
	}

	public void setPassengersNumber(int passengersNumber) {
		this.passengersNumber = passengersNumber;
	}

	public String getDestination() {
		return destination;
	}

	public void setDestination(String destination) {
		this.destination = destination;
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(id, destination, passengersNumber);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		Flight other = (Flight) obj;
		return Objects.equals(id, other.id) && Objects.equals(destination, other.destination) && Objects.equals(passengersNumber, other.passengersNumber);
	}

	@Override
	public String toString() {
		return "Flight [id=" + id + ", destination=" + destination + ", passengersNumber=" + passengersNumber + "]";
	}
	
	
}
