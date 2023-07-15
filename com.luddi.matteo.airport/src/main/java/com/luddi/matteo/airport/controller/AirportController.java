package com.luddi.matteo.airport.controller;

import com.luddi.matteo.airport.model.Flight;
import com.luddi.matteo.airport.repository.FlightRepository;
import com.luddi.matteo.airport.view.FlightView;

public class AirportController {

	private FlightRepository flightRepository;
	private FlightView flightView;
	
	public AirportController(FlightRepository flightRepository, FlightView flightView) {
		this.flightRepository = flightRepository;
		this.flightView = flightView;
	}
	
	public void allFlights() {
		flightView.showAllFlights(flightRepository.findAll());
	}
	
	public void newFlight(Flight flight) {
		Flight existingFlight = flightRepository.findById(flight.getId());
		if (existingFlight != null) {
			flightView.showError("There is already a flight with id " + flight.getId(), existingFlight);
			return;
		} 
		flightRepository.save(flight);
		flightView.flightAdded(flight);
	}
	
	public void deleteFlight(Flight flight) {
		Flight existingFlight = flightRepository.findById(flight.getId());
		if(existingFlight == null) {
			flightView.showErrorFlightNotFound("No existing flight with id " + flight.getId(), flight);
			return;
		}
		flightRepository.delete(flight.getId());
		flightView.flightRemoved(flight);
	}
	
	public void changePassengersNumber(Flight flightToChange, int newPassengersNumber) {
		Flight existingFlight = flightRepository.findById(flightToChange.getId());
		if (existingFlight == null) {
			flightView.showErrorFlightNotFound("No existing flight with id " + flightToChange.getId(), flightToChange);
			return;
		} 
		Flight modifiedFlight = new Flight(flightToChange.getId(), flightToChange.getDestination(), newPassengersNumber);
		flightRepository.change(modifiedFlight);
		flightView.flightChanged(flightToChange, newPassengersNumber);
	}
}
