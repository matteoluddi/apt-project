package com.luddi.matteo.airport.controller;

import static java.util.Arrays.asList;
import static org.mockito.Mockito.ignoreStubs;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.luddi.matteo.airport.model.Flight;
import com.luddi.matteo.airport.repository.FlightRepository;
import com.luddi.matteo.airport.view.FlightView;

public class AirportControllerTest {

	@Mock
	private FlightRepository flightRepository;

	@Mock
	private FlightView flightView;

	@InjectMocks
	private AirportController airportController;

	private AutoCloseable closeable;

	@Before
	public void setup() {
		closeable = MockitoAnnotations.openMocks(this);
	}

	@After
	public void releaseMocks() throws Exception {
		closeable.close();
	}
	
	@Test
	public void testAllFlights() {
		List<Flight> flights = asList(new Flight());
		when(flightRepository.findAll())
			.thenReturn(flights);
		airportController.allFlights();
		verify(flightView)
			.showAllFlights(flights);
	}
	
	@Test
	public void testNewFlightWhenFlightDoesNotAleardyExist() {
		Flight flight = new Flight("1", "test", 10);
		when(flightRepository.findById("1")).thenReturn(null);
		airportController.newFlight(flight);
		InOrder inOrder = inOrder(flightRepository, flightView);
		inOrder.verify(flightRepository).save(flight);
		inOrder.verify(flightView).flightAdded(flight);
	}
	
	@Test
	public void testNewFlightWhenFlightAlreadyExist() {
		Flight existingflight = new Flight("1", "test", 10);
		Flight flightToAdd = new Flight("1", "destination", 15);
		when(flightRepository.findById("1")).thenReturn(existingflight);
		airportController.newFlight(flightToAdd);
		verify(flightView).showError("There is already a flight with id 1", existingflight);
	}
	
	@Test
	public void testDeleteFlightWhenFlightExists() {
		Flight flightToDelete = new Flight("1", "test", 10);
		when(flightRepository.findById("1")).
			thenReturn(flightToDelete);
		airportController.deleteFlight(flightToDelete);
		InOrder inOrder = inOrder(flightRepository, flightView);
		inOrder.verify(flightRepository).delete("1");
		inOrder.verify(flightView).flightRemoved(flightToDelete);
	}

	@Test
	public void testDeleteFlightWhenFlightDoesNotExist() {
		Flight flight = new Flight("1", "test", 10);
		when(flightRepository.findById("1")).
			thenReturn(null);
		airportController.deleteFlight(flight);
		verify(flightView)
			.showErrorFlightNotFound("No existing flight with id 1",
					flight);
		verifyNoMoreInteractions(ignoreStubs(flightRepository));
	}
	
	@Test
	public void testChangeFlightWhenFlightExist() {
		Flight existingFlight = new Flight("1", "test", 10);
		Flight modifiedFlight = new Flight("1", "test", 15);
		when(flightRepository.findById("1")).
			thenReturn(existingFlight);
		airportController.changePassengersNumber(existingFlight, modifiedFlight.getPassengersNumber());
		InOrder inOrder = inOrder(flightRepository, flightView);
		inOrder.verify(flightRepository).change(modifiedFlight);
		inOrder.verify(flightView).flightChanged(existingFlight, modifiedFlight.getPassengersNumber());
	}
	
	@Test
	public void testChangeFlightWhenFlightDoesNotExist() {
		Flight flight = new Flight("1", "test", 10);
		when(flightRepository.findById("1")).
			thenReturn(null);
		airportController.changePassengersNumber(flight, 15);
		verify(flightView)
			.showErrorFlightNotFound("No existing flight with id 1",
					flight);
		verifyNoMoreInteractions(ignoreStubs(flightRepository));
	}
	
	
	
}
