package com.luddi.matteo.airport.repository.mongo;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.bson.Document;

import com.luddi.matteo.airport.model.Flight;
import com.luddi.matteo.airport.repository.FlightRepository;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;

public class FlightMongoRepository implements FlightRepository{

	private static final String ID = "id";
	private static final String DESTINATION = "destination";
	private static final String PASSENGERSNUMBER = "passengersNumber";
	
	private MongoCollection<Document> flightCollection;

	public FlightMongoRepository(MongoClient client, String databaseName, String collectionName) {
		flightCollection = client
			.getDatabase(databaseName)
			.getCollection(collectionName);
	}
	
	@Override
	public List<Flight> findAll() {
		return StreamSupport.
				stream(flightCollection.find().spliterator(), false)
				.map(this::fromDocumentToFlight)
				.collect(Collectors.toList());
	}

	@Override
	public Flight findById(String id) {
		Document d = flightCollection.find(Filters.eq(ID, id)).first();
		if (d != null)
			return fromDocumentToFlight(d);
		return null;
	}

	@Override
	public void save(Flight flight) {
		flightCollection.insertOne(
				new Document()
					.append(ID, flight.getId())
					.append(DESTINATION, flight.getDestination())
					.append(PASSENGERSNUMBER, flight.getPassengersNumber()));		
	}

	@Override
	public void delete(String id) {
		flightCollection.deleteOne(Filters.eq(ID, id));
		
	}

	private Flight fromDocumentToFlight(Document d) {
		return new Flight(""+d.get(ID), ""+d.get(DESTINATION), d.getInteger(PASSENGERSNUMBER));
	}

	@Override
	public void change(Flight flight) {
		flightCollection.updateOne(Filters.eq(ID, flight.getId()),
				Updates.set(PASSENGERSNUMBER, flight.getPassengersNumber()));	
	}

}
