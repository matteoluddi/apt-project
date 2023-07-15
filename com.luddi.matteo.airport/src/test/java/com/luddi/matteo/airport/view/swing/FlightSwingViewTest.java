package com.luddi.matteo.airport.view.swing;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;

import java.util.Arrays;

import javax.swing.DefaultListModel;

import org.assertj.swing.annotation.GUITest;
import org.assertj.swing.core.matcher.JButtonMatcher;
import org.assertj.swing.core.matcher.JLabelMatcher;
import org.assertj.swing.edt.GuiActionRunner;
import org.assertj.swing.fixture.FrameFixture;
import org.assertj.swing.fixture.JButtonFixture;
import org.assertj.swing.fixture.JTextComponentFixture;
import org.assertj.swing.junit.runner.GUITestRunner;
import org.assertj.swing.junit.testcase.AssertJSwingJUnitTestCase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.luddi.matteo.airport.controller.AirportController;
import com.luddi.matteo.airport.model.Flight;

@RunWith(GUITestRunner.class)
public class FlightSwingViewTest extends AssertJSwingJUnitTestCase {

	private FrameFixture window;
	
	private FlightSwingView flightSwingView;
	
	@Mock
	private AirportController airportController;

	private AutoCloseable closeable;
	
	@Override
	protected void onSetUp() {
		closeable = MockitoAnnotations.openMocks(this);	
		GuiActionRunner.execute(() -> {
			flightSwingView = new FlightSwingView();
			flightSwingView.setAirportController(airportController);
			return flightSwingView;
		});
		window = new FrameFixture(robot(), flightSwingView);
		window.show(); 
	}
	
	@Override
	protected void onTearDown() throws Exception {
		closeable.close();
	}
	
	@Test @GUITest
	public void testControlsInitialStates() {
		window.label(JLabelMatcher.withText("Id"));
		window.label(JLabelMatcher.withText("Destination"));
		window.label(JLabelMatcher.withText("Passengers number"));
		window.textBox("idTextBox").requireEnabled();
		window.textBox("destinationTextBox").requireEnabled();
		window.textBox("passengersNumberTextBox").requireEnabled();
		window.button(JButtonMatcher.withText("Add")).requireDisabled();
		window.list("flightList");
		window.button(JButtonMatcher.withText("Delete Selected")).requireDisabled();
		window.button(JButtonMatcher.withText("Change")).requireDisabled();		
		window.label("errorMessageLabel").requireText(" ");	
	}
	
	@Test
	public void testWhenIdAndDestinationAndPassengersNumberAreNonEmptyThenAddButtonShouldBeEnabled() {
		window.textBox("idTextBox").enterText("1");
		window.textBox("destinationTextBox").enterText("test");
		window.textBox("passengersNumberTextBox").enterText("10");
		window.button(JButtonMatcher.withText("Add")).requireEnabled();
	}

	@Test
	public void testWhenIdOrDestinationOrPassengersNumberAreBlankThenAddButtonShouldBeDisabled() {
		JTextComponentFixture idTextBox = window.textBox("idTextBox");
		JTextComponentFixture destinationTextBox = window.textBox("destinationTextBox");
		JTextComponentFixture passengersNumberTextBox = window.textBox("passengersNumberTextBox");

		idTextBox.enterText("1");
		destinationTextBox.enterText("test");
		passengersNumberTextBox.enterText(" ");
		window.button(JButtonMatcher.withText("Add")).requireDisabled();

		resetTextFields(idTextBox, destinationTextBox, passengersNumberTextBox);

		idTextBox.enterText("1");
		destinationTextBox.enterText(" ");
		passengersNumberTextBox.enterText("10");
		window.button(JButtonMatcher.withText("Add")).requireDisabled();

		resetTextFields(idTextBox, destinationTextBox, passengersNumberTextBox);

		idTextBox.enterText("1");
		destinationTextBox.enterText(" ");
		passengersNumberTextBox.enterText(" ");
		window.button(JButtonMatcher.withText("Add")).requireDisabled();

		resetTextFields(idTextBox, destinationTextBox, passengersNumberTextBox);

		idTextBox.enterText(" ");
		destinationTextBox.enterText("test");
		passengersNumberTextBox.enterText(" ");
		window.button(JButtonMatcher.withText("Add")).requireDisabled();

		resetTextFields(idTextBox, destinationTextBox, passengersNumberTextBox);

		idTextBox.enterText(" ");
		destinationTextBox.enterText(" ");
		passengersNumberTextBox.enterText("10");
		window.button(JButtonMatcher.withText("Add")).requireDisabled();

		resetTextFields(idTextBox, destinationTextBox, passengersNumberTextBox);

		idTextBox.enterText(" ");
		destinationTextBox.enterText("test");
		passengersNumberTextBox.enterText("10");
		window.button(JButtonMatcher.withText("Add")).requireDisabled();
	}
	
	private void resetTextFields(JTextComponentFixture idTextBox, JTextComponentFixture destinationTextBox, JTextComponentFixture passengersNumberTextBox) {
		idTextBox.setText("");
		destinationTextBox.setText("");
		passengersNumberTextBox.setText("");
	}
	
	@Test
	public void testDeleteButtonShouldBeEnabledOnlyWhenAFlightIsSelectedFromTheLists() {
		GuiActionRunner.execute(() -> flightSwingView.getListFlightModel().addElement(new Flight("1", "test", 10)));
		window.list("flightList").selectItem(0);
		JButtonFixture deleteButton = window.button(JButtonMatcher.withText("Delete Selected"));
		deleteButton.requireEnabled();
		window.list("flightList").clearSelection();
		deleteButton.requireDisabled();
	}
	
	@Test
	public void testChangeButtonShouldBeEnabledOnlyWhenAFlightIsSelectedFromTheListAndThePassengersNumberFieldIsFilledAndBothIdAndDestinationAreBlank() {
		GuiActionRunner.execute(() -> flightSwingView.getListFlightModel().addElement(new Flight("1", "test", 10)));
		JButtonFixture changeButton = window.button(JButtonMatcher.withText("Change"));
		
		window.textBox("idTextBox").enterText("");
		window.textBox("destinationTextBox").enterText("");
		window.textBox("passengersNumberTextBox").enterText("");	
		changeButton.requireDisabled();
		
		window.textBox("idTextBox").enterText("1");
		changeButton.requireDisabled();
		
		window.textBox("destinationTextBox").enterText("test");
		changeButton.requireDisabled();

		window.textBox("passengersNumberTextBox").enterText("15");	
		changeButton.requireDisabled();

		window.textBox("destinationTextBox").deleteText();
		changeButton.requireDisabled();

		window.textBox("idTextBox").deleteText();
		window.textBox("destinationTextBox").enterText("test");
		window.textBox("passengersNumberTextBox").deleteText();	
		changeButton.requireDisabled();
		
		window.textBox("passengersNumberTextBox").enterText("15");	
		changeButton.requireDisabled();
		
		window.textBox("destinationTextBox").deleteText();
		changeButton.requireDisabled();
		
		window.list("flightList").selectItem(0);
		
		window.textBox("passengersNumberTextBox").deleteText();
		changeButton.requireDisabled();
		
		window.textBox("idTextBox").enterText("1");
		changeButton.requireDisabled();
		
		window.textBox("destinationTextBox").enterText("test");
		changeButton.requireDisabled();

		window.textBox("passengersNumberTextBox").enterText("15");	
		changeButton.requireDisabled();

		window.textBox("destinationTextBox").deleteText();
		changeButton.requireDisabled();

		window.textBox("idTextBox").deleteText();
		window.textBox("passengersNumberTextBox").deleteText();	
		window.textBox("destinationTextBox").enterText("test");
		changeButton.requireDisabled();
		
		window.textBox("passengersNumberTextBox").enterText("15");	
		changeButton.requireDisabled();
		
		window.textBox("destinationTextBox").deleteText();
		changeButton.requireEnabled();
				
	}

	@Test
	public void testShowAllFlightsShouldAddFlightDescriptionsToTheList() {
		Flight flight1 = new Flight("1", "test1", 10);
		Flight flight2 = new Flight("2", "test2", 20);
		GuiActionRunner.execute(
			() -> flightSwingView.showAllFlights(
					Arrays.asList(flight1, flight2))
		);
		String[] listContents = window.list("flightList").contents();
		assertThat(listContents).containsExactly("1 - test1 - 10",
				"2 - test2 - 20");
	}
	
	@Test
	public void testShowErrorShouldShowTheMessageInTheErrorLabel() {
		Flight flight = new Flight("1", "test", 10);
		GuiActionRunner.execute(
			() -> flightSwingView.showError("error message", flight)
		);
		window.label("errorMessageLabel")
			.requireText("error message: 1 - test - 10");
	}
	
	@Test
	public void testFlightAddedShouldAddTheFlightToTheListAndResetTheErrorLabel() {
		GuiActionRunner.execute(
				() ->
				flightSwingView.flightAdded(new Flight("1", "test", 10))
				);
		String[] listContents = window.list("flightList").contents();
		assertThat(listContents).containsExactly("1 - test - 10");
		window.label("errorMessageLabel").requireText(" ");
	}
	
	@Test
	public void testFlightRemovedShouldRemoveTheFlightFromTheListsAndResetTheErrorLabel() {
		Flight flight1 = new Flight("1", "test1", 10);
		Flight flight2 = new Flight("2", "test2", 20);
		GuiActionRunner.execute(
				() -> {
					DefaultListModel<Flight> listFlightModel = flightSwingView.getListFlightModel();
					listFlightModel.removeAllElements();
					listFlightModel.addElement(flight1);
					listFlightModel.addElement(flight2);		
				}
		);	
		GuiActionRunner.execute(
			() -> flightSwingView.flightRemoved(flight1)
		);

		String[] listFlightContents = window.list("flightList").contents();
		assertThat(listFlightContents).containsExactly("2 - test2 - 20");
		window.label("errorMessageLabel").requireText(" ");
	}
	
	@Test
	public void testFlightChangedShouldChangeTheFlightOnTheListsAndResetTheErrorLabel() {
		Flight flight1 = new Flight("1", "test", 10);
		GuiActionRunner.execute(
				() -> {
					DefaultListModel<Flight> listFlightModel = flightSwingView.getListFlightModel();
					listFlightModel.removeAllElements();
					listFlightModel.addElement(flight1);		
				}
		);	
		GuiActionRunner.execute(
			() -> flightSwingView.flightChanged(flight1, 15)
		);

		String[] listFlightContents = window.list("flightList").contents();
		assertThat(listFlightContents).containsExactly("1 - test - 15");
		window.label("errorMessageLabel").requireText(" ");
	}
	
	@Test
	public void testShowErrorFlightNotFoundShouldShowTheMessageInTheErrorLabel() {
		Flight flightToFind = new Flight("1", "test1", 10);
		GuiActionRunner.execute(
			() -> flightSwingView.showErrorFlightNotFound("error message", flightToFind)
		);
		window.label("errorMessageLabel")
			.requireText("error message: 1 - test1 - 10");
	}
	
	@Test
	public void testAddButtonShouldDelegateToAirportControllerNewFlight() {
		window.textBox("idTextBox").enterText("1");
		window.textBox("destinationTextBox").enterText("test");
		window.textBox("passengersNumberTextBox").enterText("10");
		window.button(JButtonMatcher.withText("Add")).click();
		verify(airportController).newFlight(new Flight("1", "test", 10));
	}
	
	@Test
	public void testChangeButtonShouldDelegateToAirportControllerChangePassengersNumber() {
		Flight flight = new Flight("1", "test", 10);
		GuiActionRunner.execute(
				() -> {
					DefaultListModel<Flight> listFlightModel = flightSwingView.getListFlightModel();
					listFlightModel.removeAllElements();
					listFlightModel.addElement(flight);
				}
		);	
		window.list("flightList").selectItem(0);	
		window.textBox("passengersNumberTextBox").enterText("15");
		window.button(JButtonMatcher.withText("Change")).click();
		verify(airportController).changePassengersNumber(flight, 15);
	}
	
	@Test
	public void testDeleteButtonWhenAFlightIsSelectedShouldDelegateToAirportControllerDeleteFlight() {
		Flight flight1 = new Flight("1", "test1", 10);
		Flight flight2 = new Flight("2", "test2", 20);
		GuiActionRunner.execute(
				() -> {
					DefaultListModel<Flight> listFlightModel = flightSwingView.getListFlightModel();
					listFlightModel.removeAllElements();
					listFlightModel.addElement(flight1);
					listFlightModel.addElement(flight2);		
				}
		);	
		window.list("flightList").selectItem(1);
		window.button(JButtonMatcher.withText("Delete Selected")).click();
		verify(airportController).deleteFlight(flight2);
	}
	
}
