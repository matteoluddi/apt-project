package com.luddi.matteo.airport.app.swing;

import java.awt.EventQueue;
import java.util.concurrent.Callable;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.luddi.matteo.airport.controller.AirportController;
import com.luddi.matteo.airport.repository.mongo.FlightMongoRepository;
import com.luddi.matteo.airport.view.swing.FlightSwingView;
import com.mongodb.MongoClient;
import com.mongodb.ServerAddress;

import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;


@Command(mixinStandardHelpOptions = true)
public class AirportSwingApp implements Callable<Void> {

		@Option(names = { "--mongo-host" }, description = "MongoDB host address")
		private String mongoHost = "localhost";

		@Option(names = { "--mongo-port" }, description = "MongoDB host port")
		private int mongoPort = 27017;

		@Option(names = { "--db-name" }, description = "Database name")
		private String databaseName = "airport";

		@Option(names = { "--db-collection" }, description = "Collection name")
		private String collectionName = "flight";

		public static void main(String[] args) {
			new CommandLine(new AirportSwingApp()).execute(args);
		}

		@Override
		public Void call() throws Exception {
			EventQueue.invokeLater(() -> {
				try {
					FlightMongoRepository flightRepository = new FlightMongoRepository(
							new MongoClient(new ServerAddress(mongoHost, mongoPort)), databaseName, collectionName);
					FlightSwingView flightView = new FlightSwingView();
					AirportController airportController = new AirportController(flightRepository, flightView);
					flightView.setAirportController(airportController);
					flightView.setVisible(true); 
					airportController.allFlights();
				} catch (Exception e) {
					Logger.getLogger(getClass().getName())
						.log(Level.SEVERE, "Exception", e);
				}
			});
			return null;
		}

	}

	

