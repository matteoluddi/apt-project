package com.luddi.matteo.airport.bdd.steps;

import static com.luddi.matteo.airport.bdd.steps.DatabaseSteps.COLLECTION_NAME;
import static com.luddi.matteo.airport.bdd.steps.DatabaseSteps.DB_NAME;
import com.luddi.matteo.airport.bdd.AirportSwingAppBDD;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.swing.launcher.ApplicationLauncher.application;
import java.util.regex.Pattern;

import javax.swing.JFrame;

import org.assertj.swing.core.BasicRobot;
import org.assertj.swing.core.GenericTypeMatcher;
import org.assertj.swing.core.matcher.JButtonMatcher;
import org.assertj.swing.finder.WindowFinder;
import org.assertj.swing.fixture.FrameFixture;

import io.cucumber.java.After;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class FlightSwingViewTest {

	private FrameFixture window;

	@After
	public void tearDown() {
		if (window != null)
			window.cleanUp();
	}

	@When("The Flight View is shown")
	public void the_Flight_View_is_shown() {
		application("com.luddi.matteo.airport.app.swing.AirportSwingApp")
			.withArgs(
				"--mongo-port=" + AirportSwingAppBDD.mongoPort,
				"--db-name=" + DB_NAME,
				"--db-collection=" + COLLECTION_NAME
			)
			.start();
		window = WindowFinder.findFrame(new GenericTypeMatcher<JFrame>(JFrame.class) {
			@Override
			protected boolean isMatching(JFrame frame) {
				return "Flight View".equals(frame.getTitle()) && frame.isShowing();
			}
		}).using(BasicRobot.robotWithCurrentAwtHierarchy());
	}
	
	@When("The user clicks the {string} button")
	public void the_user_clicks_the_button(String buttonText) {
		window.button(JButtonMatcher.withText(buttonText)).click();
	}
	
	@Given("The user provides flight data in the text fields")
	public void the_user_provides_flight_data_in_the_text_fields() {
		window.textBox("idTextBox").enterText("3");
		window.textBox("destinationTextBox").enterText("new flight");
		window.textBox("passengersNumberTextBox").enterText("10");
	}
	
	@Then("The list contains the new flight")
	public void the_list_contains_the_new_flight() {
		assertThat(window.list("flightList").contents())
			.anySatisfy(e -> assertThat(e).contains("3", "new flight", "10"));
	}
	
	@Given("The user provides flight data in the text fields, specifying an existing id")
	public void the_user_provides_flight_data_in_the_text_fields_specifying_an_existing_id() {
		window.textBox("idTextBox").enterText(DatabaseSteps.FLIGHT_FIXTURE_1_ID);
		window.textBox("destinationTextBox").enterText("new flight");
		window.textBox("passengersNumberTextBox").enterText("10");
	}
	
	@Then("An error is shown containing the id of the existing flight")
	public void an_error_is_shown_containing_the_id_of_the_existing_flight() {
		assertThat(window.label("errorMessageLabel").text())
			.contains(DatabaseSteps.FLIGHT_FIXTURE_1_ID);
	}
	
	@Given("The user selects a flight from the list")
	public void the_user_selects_a_flight_from_the_list() {
		window.list("flightList")
			.selectItem(Pattern.compile(".*" + DatabaseSteps.FLIGHT_FIXTURE_1_ID + ".*"));
	}
	
	@Then("The flight is removed from the list")
	public void the_flight_is_removed_from_the_list() {
		assertThat(window.list("flightList").contents())
			.noneMatch(e -> e.contains(DatabaseSteps.FLIGHT_FIXTURE_1_ID));
	}
	
	@Then("An error is shown containing the id of the selected flight")
	public void an_error_is_shown_containing_the_id_of_the_selected_flight() {
		assertThat(window.label("errorMessageLabel").text())
			.contains(DatabaseSteps.FLIGHT_FIXTURE_1_ID);
	}
	
	@Given("The user provides a passengers number in the passengers number field")
	public void the_user_provides_a_passengers_number_in_the_passengers_number_fiel() {
		window.textBox("passengersNumberTextBox").enterText("15");
	}
	
	@Then("The list contains the flight with the new passengers number value")
	public void the_list_contains_the_flight_with_the_new_passengers_number_value() {
		assertThat(window.list("flightList").contents())
			.anySatisfy(e -> assertThat(e).contains("1", "flight1", "15"));
	}
	
}
