package com.luddi.matteo.airport.bdd.steps;

import org.bson.Document;

import com.luddi.matteo.airport.bdd.AirportSwingAppBDD;
import com.mongodb.MongoClient;
import com.mongodb.client.model.Filters;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;

public class DatabaseSteps {

	static final String DB_NAME = "test-db";
	static final String COLLECTION_NAME = "test-collection";

	static final String FLIGHT_FIXTURE_1_ID = "1";
	static final String FLIGHT_FIXTURE_1_DESTINATION = "flight1";
	static final int FLIGHT_FIXTURE_1_PASSENGERSNUMBER = 10;
	static final String FLIGHT_FIXTURE_2_ID = "2";
	static final String FLIGHT_FIXTURE_2_DESTINATION = "flight2";
	static final int FLIGHT_FIXTURE_2_PASSENGERSNUMBER = 20;

	private MongoClient mongoClient;

	@Before
	public void setUp() {
		mongoClient = new MongoClient("localhost", AirportSwingAppBDD.mongoPort);
		mongoClient.getDatabase(DB_NAME).drop();
	}

	@After
	public void tearDown() {
		mongoClient.close();
	}

	@Given("The database contains a few flights")
	public void the_database_contains_a_few_flights() {
		addTestFlightToDatabase(FLIGHT_FIXTURE_1_ID, FLIGHT_FIXTURE_1_DESTINATION, FLIGHT_FIXTURE_1_PASSENGERSNUMBER);
		addTestFlightToDatabase(FLIGHT_FIXTURE_2_ID, FLIGHT_FIXTURE_2_DESTINATION, FLIGHT_FIXTURE_2_PASSENGERSNUMBER);
	}

	@Given("The flight is in the meantime removed from the database")
	public void the_flight_is_in_the_meantime_removed_from_the_database() {
		mongoClient
			.getDatabase(DB_NAME)
			.getCollection(COLLECTION_NAME)
			.deleteOne(Filters.eq("id", FLIGHT_FIXTURE_1_ID));
	}

	private void addTestFlightToDatabase(String id, String destination, int passengersNumber) {
		mongoClient
			.getDatabase(DB_NAME)
			.getCollection(COLLECTION_NAME)
			.insertOne(
				new Document()
					.append("id", id)
					.append("destination", destination)
					.append("passengersNumber", passengersNumber));
	}
	
}

