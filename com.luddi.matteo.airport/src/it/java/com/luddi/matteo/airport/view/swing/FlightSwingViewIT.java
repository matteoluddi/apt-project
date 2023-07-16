package com.luddi.matteo.airport.view.swing;

import static org.assertj.core.api.Assertions.assertThat;
import java.net.InetSocketAddress;

import org.assertj.swing.annotation.GUITest;
import org.assertj.swing.core.matcher.JButtonMatcher;
import org.assertj.swing.edt.GuiActionRunner;
import org.assertj.swing.fixture.FrameFixture;
import org.assertj.swing.junit.runner.GUITestRunner;
import org.assertj.swing.junit.testcase.AssertJSwingJUnitTestCase;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import com.luddi.matteo.airport.controller.AirportController;
import com.luddi.matteo.airport.model.Flight;
import com.luddi.matteo.airport.repository.mongo.FlightMongoRepository;
import com.mongodb.MongoClient;
import com.mongodb.ServerAddress;
import de.bwaldvogel.mongo.MongoServer;
import de.bwaldvogel.mongo.backend.memory.MemoryBackend;


@RunWith(GUITestRunner.class)
public class FlightSwingViewIT extends AssertJSwingJUnitTestCase{


	private static MongoServer server;
	private static InetSocketAddress serverAddress;

	private MongoClient mongoClient;

	private FrameFixture window;
	private FlightSwingView flightSwingView;
	private AirportController airportController;
	private FlightMongoRepository flightRepository;

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

	@Override
	protected void onSetUp() {
		mongoClient = new MongoClient(new ServerAddress(serverAddress));
		flightRepository =
			new FlightMongoRepository(mongoClient, AIRPORT_DB_NAME, FLIGHT_COLLECTION_NAME);
		for (Flight flight : flightRepository.findAll()) {
			flightRepository.delete(flight.getId());
		}

		GuiActionRunner.execute(() -> {
			flightSwingView = new FlightSwingView();
			airportController = new AirportController(flightRepository, flightSwingView);
			flightSwingView.setAirportController(airportController);
			return flightSwingView;
		});
		window = new FrameFixture(robot(), flightSwingView);
		window.show();
	}

	@Override
	protected void onTearDown() {
		mongoClient.close();
	}
	
	@Test @GUITest
	public void testAllFlights() {
		Flight flight1 = new Flight("1", "test1", 10);
		Flight flight2 = new Flight("2", "test2", 15);
		flightRepository.save(flight1);
		flightRepository.save(flight2);
		GuiActionRunner.execute(
			() -> airportController.allFlights());
		assertThat(window.list("flightList").contents())
			.containsExactly("1 - test1 - 10", "2 - test2 - 15");
	}
	
	@Test @GUITest
	public void testAddButtonSuccess() {
		window.textBox("idTextBox").enterText("1");
		window.textBox("destinationTextBox").enterText("test");
		window.textBox("passengersNumberTextBox").enterText("10");
		window.button(JButtonMatcher.withText("Add")).click();
		assertThat(window.list("flightList").contents())
			.containsExactly("1 - test - 10");
	}
	
	@Test @GUITest
	public void testAddButtonError() {
		Flight existingFlight = new Flight("1", "existing", 10);
		flightRepository.save(existingFlight);
		window.textBox("idTextBox").enterText("1");
		window.textBox("destinationTextBox").enterText("test");
		window.textBox("passengersNumberTextBox").enterText("15");
		window.button(JButtonMatcher.withText("Add")).click();
		assertThat(window.list("flightList").contents())
			.isEmpty();
		window.label("errorMessageLabel")
			.requireText("There is already a flight with id 1: "
					+ "1 - existing - 10");
	}
	
	@Test @GUITest
	public void testChangeButtonSuccess() {
		GuiActionRunner.execute(
				() -> airportController.newFlight(new Flight("1", "toChange", 10)));
		window.textBox("passengersNumberTextBox").enterText("15");
		window.list("flightList").selectItem(0);
		window.button(JButtonMatcher.withText("Change")).click();
		assertThat(window.list("flightList").contents())
			.containsExactly("1 - toChange - 15");
	}
	
	@Test @GUITest
	public void testChangeButtonError() {
		Flight flight = new Flight("1", "notExistent", 10);
		GuiActionRunner.execute(
				() -> flightSwingView.getListFlightModel().addElement(flight));
		window.textBox("passengersNumberTextBox").enterText("15");
		window.list("flightList").selectItem(0);
		window.button(JButtonMatcher.withText("Change")).click();
		assertThat(window.list("flightList").contents())
		.isEmpty();
		window.label("errorMessageLabel")
		.requireText("No existing flight with id 1: 1 - notExistent - 10");
	}
	
	@Test @GUITest
	public void testDeleteButtonSuccess() {
		GuiActionRunner.execute(
			() -> airportController.newFlight(new Flight("1", "toRemove", 10)));
		window.list("flightList").selectItem(0);
		window.button(JButtonMatcher.withText("Delete Selected")).click();
		assertThat(window.list("flightList").contents())
			.isEmpty();
	}
	
	@Test @GUITest
	public void testDeleteButtonError() {
		Flight flight = new Flight("1", "notExistent", 10);
		GuiActionRunner.execute(
			() -> flightSwingView.getListFlightModel().addElement(flight));
		window.list("flightList").selectItem(0);
		window.button(JButtonMatcher.withText("Delete Selected")).click();
		assertThat(window.list("flightList").contents())
			.isEmpty();
		window.label("errorMessageLabel")
			.requireText("No existing flight with id 1: 1 - notExistent - 10");
	}
	
}
