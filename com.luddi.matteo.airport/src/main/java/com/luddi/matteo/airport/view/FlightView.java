package com.luddi.matteo.airport.view;

import java.util.List;

import com.luddi.matteo.airport.model.Flight;

public interface FlightView {
	
	void showAllFlights(List<Flight> flights);

	void showError(String message, Flight flight);

	void flightAdded(Flight flight);

	void flightRemoved(Flight flight);
	
	void flightChanged(Flight flightToChange, int newPassengersNumber); 
	
	void showErrorFlightNotFound(String message, Flight flight);

}
