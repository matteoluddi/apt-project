package com.luddi.matteo.airport.repository.mongo;

import static org.assertj.core.api.Assertions.*;

import java.net.InetSocketAddress;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.bson.Document;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.luddi.matteo.airport.model.Flight;
import com.mongodb.MongoClient;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import de.bwaldvogel.mongo.MongoServer;
import de.bwaldvogel.mongo.backend.memory.MemoryBackend;

public class FlightMongoRepositoryTest {

	private static MongoServer server;
	private static InetSocketAddress serverAddress;

	private MongoClient client;
	private FlightMongoRepository flightRepository;
	private MongoCollection<Document> flightCollection;

	private static final String AIRPORT_DB_NAME = "airport";
	private static final String FLIGHT_COLLECTION_NAME = "flight";	

	@BeforeClass
	public static void setupServer() {
		server = new MongoServer(new MemoryBackend());
		serverAddress = server.bind();
	}

	@AfterClass
	public static void shutdownServer() {
		server.shutdown();
	}

	@Before
	public void setup() {
		client = new MongoClient(new ServerAddress(serverAddress));
		flightRepository = new FlightMongoRepository(client, AIRPORT_DB_NAME, FLIGHT_COLLECTION_NAME);
		MongoDatabase database = client.getDatabase(AIRPORT_DB_NAME);
		database.drop();
		flightCollection = database.getCollection(FLIGHT_COLLECTION_NAME);
	}

	@After
	public void tearDown() {
		client.close();
	}
	
	@Test
	public void testFindAllWhenDatabaseIsEmpty() {
		assertThat(flightRepository.findAll()).isEmpty();
	}
	
	@Test
	public void testFindAllWhenDatabaseIsNotEmpty() {
		addTestFlightToDatabase("1", "test1", 10);
		addTestFlightToDatabase("2", "test2", 15);
		assertThat(flightRepository.findAll())
			.containsExactly(
				new Flight("1", "test1", 10),
				new Flight("2", "test2", 15));
	}
	
	@Test
	public void testFindByIdNotFound() {
		assertThat(flightRepository.findById("1"))
			.isNull();
	}

	@Test
	public void testFindByIdFound() {
		addTestFlightToDatabase("1", "test1", 10);
		addTestFlightToDatabase("2", "test2", 15);
		addTestFlightToDatabase("3", "tet3", 17);
		assertThat(flightRepository.findById("1"))
			.isEqualTo(new Flight("1", "test1", 10));
	}
	
	@Test
	public void testSave() {
		Flight flight = new Flight("1", "test", 10);
		flightRepository.save(flight);
		assertThat(readAllFlightsFromDatabase())
			.containsExactly(flight);
	}
	
	@Test
	public void testDelete() {
		addTestFlightToDatabase("1", "test", 10);
		flightRepository.delete("1");
		assertThat(readAllFlightsFromDatabase())
			.isEmpty();
	}
	
	@Test
	public void testChange() {
		addTestFlightToDatabase("1", "test", 10);
		Flight modifiedFlight = new Flight("1", "test", 15);
		flightRepository.change(modifiedFlight);
		assertThat(readAllFlightsFromDatabase()).containsExactly(modifiedFlight);
	}	
	
	private void addTestFlightToDatabase(String id, String destination, int passengersNumber) {
		flightCollection.insertOne(
				new Document()
					.append("id", id)
					.append("destination", destination)
					.append("passengersNumber", passengersNumber));
	}
	
	private List<Flight> readAllFlightsFromDatabase() {
		return StreamSupport.
			stream(flightCollection.find().spliterator(), false)
				.map(d -> new Flight(""+d.get("id"), ""+d.get("destination"), d.getInteger("passengersNumber")))
				.collect(Collectors.toList());
	}
	
}
