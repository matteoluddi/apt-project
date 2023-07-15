package com.luddi.matteo.airport.repository;

import java.util.List;

import com.luddi.matteo.airport.model.Flight;


public interface FlightRepository {
	
	public List<Flight> findAll();

	public Flight findById(String id);

	public void save(Flight flight);

	public void delete(String id);
	
	public void change(Flight flight);
	
}
