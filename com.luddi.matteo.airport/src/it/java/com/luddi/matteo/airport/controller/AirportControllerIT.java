package com.luddi.matteo.airport.controller;

import static org.mockito.Mockito.verify;

import org.junit.After;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import static java.util.Arrays.asList;


import com.luddi.matteo.airport.model.Flight;
import com.luddi.matteo.airport.repository.FlightRepository;
import com.luddi.matteo.airport.repository.mongo.FlightMongoRepository;
import com.luddi.matteo.airport.view.FlightView;
import com.mongodb.MongoClient;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoDatabase;
import org.testcontainers.containers.MongoDBContainer;

@RunWith(MockitoJUnitRunner.class)
public class AirportControllerIT {

	@Mock
	private FlightView flightView;

	private FlightRepository flightRepository;

	private AirportController airportController;

	private MongoClient client;

	private static final String AIRPORT_DB_NAME = "airport";
	private static final String FLIGHT_COLLECTION_NAME = "flight";
	
	@ClassRule
	public static final MongoDBContainer mongo = new MongoDBContainer("mongo:4.4.3");

	@Before
	public void setup() {
		client = new MongoClient(new ServerAddress(mongo.getHost(), mongo.getMappedPort(27017)));
		flightRepository = new FlightMongoRepository(client, AIRPORT_DB_NAME, FLIGHT_COLLECTION_NAME);
		MongoDatabase database = client.getDatabase(AIRPORT_DB_NAME);
		database.drop();
		airportController = new AirportController(flightRepository, flightView);
	}

	@After
	public void tearDown() {
		client.close();
	}

	
	@Test
	public void testAllFlights() {
		Flight flight = new Flight("1", "test", 10);
		flightRepository.save(flight);
		airportController.allFlights();
		verify(flightView).showAllFlights(asList(flight));
	}
	
	@Test
	public void testNewFlight() {
		Flight flight = new Flight("1", "test", 10);
		airportController.newFlight(flight);
		verify(flightView).flightAdded(flight);
	}
	
	@Test
	public void testDeleteFlight() {
		Flight flight = new Flight("1", "test", 10);
		flightRepository.save(flight);
		airportController.deleteFlight(flight);
		verify(flightView).flightRemoved(flight);
	}
	
	@Test
	public void testChangePassengersNumber() {
		Flight flight = new Flight("1", "test", 10);
		int newPassengersNumber = 15;
		flightRepository.save(flight);
		airportController.changePassengersNumber(flight, newPassengersNumber);
		verify(flightView).flightChanged(flight, newPassengersNumber);
	}

}
