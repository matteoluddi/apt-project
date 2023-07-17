package com.luddi.matteo.airport.view.swing;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import com.luddi.matteo.airport.controller.AirportController;
import com.luddi.matteo.airport.model.Flight;
import com.luddi.matteo.airport.view.FlightView;

import java.awt.GridBagLayout;
import javax.swing.JLabel;

import java.awt.Component;
import java.awt.GridBagConstraints;
import javax.swing.JTextField;
import java.awt.Insets;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.List;

import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.ListSelectionModel;
import javax.swing.WindowConstants;
import javax.swing.JScrollPane;

public class FlightSwingView extends JFrame implements FlightView {

	private static final long serialVersionUID = 1L;
	
	private transient AirportController airportController;
	private JPanel contentPane;
	private JTextField idTextField;
	private JTextField destinationTextField;
	private JTextField passengersNumberTextField;
	private JLabel lblId;
	private JLabel lblDestination; 
	private JLabel lblPassengersNumber;
	private JButton btnAdd;
	private JButton btnChange;
	private JScrollPane scrollPane;
	private JButton btnDeleteSelected;
	private JLabel lblErrorMessage;
	private JList<Flight> listFlights;
	private DefaultListModel<Flight> listFlighstModel;

	DefaultListModel<Flight> getListFlightModel() {
		return listFlighstModel;
	}
	
	public void setAirportController(AirportController airportController) {
		this.airportController = airportController;		
	}
	
	public FlightSwingView() {
		setTitle("Flight View");
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		setBounds(100, 100, 440, 311);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		GridBagLayout gbl_contentPane = new GridBagLayout();
		gbl_contentPane.columnWidths = new int[]{0, 0, 0};
		gbl_contentPane.rowHeights = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0};
		gbl_contentPane.columnWeights = new double[]{0.0, 1.0, Double.MIN_VALUE};
		gbl_contentPane.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, Double.MIN_VALUE};
		contentPane.setLayout(gbl_contentPane);
		
		KeyAdapter btnAddEnabler = new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				btnAdd.setEnabled(
						!idTextField.getText().trim().isEmpty() &&
						!destinationTextField.getText().trim().isEmpty() &&
						!passengersNumberTextField.getText().trim().isEmpty()	
				);
			}
		};
		
		KeyAdapter btnChangeEnabler = new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				enebleButtonChange();
			}
		};
		
		lblId = new JLabel("Id");
		lblId.setName("id");
		GridBagConstraints gbc_lblId = new GridBagConstraints();
		gbc_lblId.insets = new Insets(0, 0, 5, 5);
		gbc_lblId.anchor = GridBagConstraints.EAST;
		gbc_lblId.gridx = 0;
		gbc_lblId.gridy = 0;
		contentPane.add(lblId, gbc_lblId);
		
		idTextField = new JTextField();
		idTextField.addKeyListener(btnAddEnabler);
		idTextField.addKeyListener(btnChangeEnabler);
		idTextField.setName("idTextBox");
		GridBagConstraints gbc_textField = new GridBagConstraints();
		gbc_textField.insets = new Insets(0, 0, 5, 0);
		gbc_textField.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField.gridx = 1;
		gbc_textField.gridy = 0;
		contentPane.add(idTextField, gbc_textField);
		idTextField.setColumns(10);
		
		lblDestination = new JLabel("Destination");
		lblDestination.setName("destination");
		GridBagConstraints gbc_lblDestination = new GridBagConstraints();
		gbc_lblDestination.anchor = GridBagConstraints.EAST;
		gbc_lblDestination.insets = new Insets(0, 0, 5, 5);
		gbc_lblDestination.gridx = 0;
		gbc_lblDestination.gridy = 1;
		contentPane.add(lblDestination, gbc_lblDestination);
		
		destinationTextField = new JTextField();
		destinationTextField.addKeyListener(btnAddEnabler);
		destinationTextField.addKeyListener(btnChangeEnabler);
		destinationTextField.setName("destinationTextBox");
		GridBagConstraints gbc_textField_1 = new GridBagConstraints();
		gbc_textField_1.insets = new Insets(0, 0, 5, 0);
		gbc_textField_1.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField_1.gridx = 1;
		gbc_textField_1.gridy = 1;
		contentPane.add(destinationTextField, gbc_textField_1);
		destinationTextField.setColumns(10);
		
		lblPassengersNumber = new JLabel("Passengers number");
		lblPassengersNumber.setName("passengersNumber");
		GridBagConstraints gbc_lblPassengersNumber = new GridBagConstraints();
		gbc_lblPassengersNumber.insets = new Insets(0, 0, 5, 5);
		gbc_lblPassengersNumber.anchor = GridBagConstraints.EAST;
		gbc_lblPassengersNumber.gridx = 0;
		gbc_lblPassengersNumber.gridy = 2;
		contentPane.add(lblPassengersNumber, gbc_lblPassengersNumber);
		
		passengersNumberTextField = new JTextField();
		passengersNumberTextField.addKeyListener(new KeyAdapter() {
			@Override
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                if (!Character.isDigit(c)) {
                	e.consume();
                }
            }		
		});
		
		passengersNumberTextField.addKeyListener(btnAddEnabler);
		passengersNumberTextField.addKeyListener(btnChangeEnabler);
		passengersNumberTextField.setName("passengersNumberTextBox");
		GridBagConstraints gbc_textField_2 = new GridBagConstraints();
		gbc_textField_2.insets = new Insets(0, 0, 5, 0);
		gbc_textField_2.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField_2.gridx = 1;
		gbc_textField_2.gridy = 2;
		contentPane.add(passengersNumberTextField, gbc_textField_2);
		passengersNumberTextField.setColumns(10);
		
		btnAdd = new JButton("Add");
		btnAdd.setEnabled(false);
		GridBagConstraints gbc_btnAdd = new GridBagConstraints();
		gbc_btnAdd.insets = new Insets(0, 0, 5, 0);
		gbc_btnAdd.gridx = 1;
		gbc_btnAdd.gridy = 3;
		btnAdd.addActionListener(
				e -> airportController.newFlight(new Flight(idTextField.getText(), 
								destinationTextField.getText(), 
								Integer.parseInt(passengersNumberTextField.getText()))));	
					
		contentPane.add(btnAdd, gbc_btnAdd);
				
		btnChange = new JButton("Change");
		btnChange.setEnabled(false);
		btnChange.addActionListener(
				e -> airportController.changePassengersNumber(listFlights.getSelectedValue(), 
						Integer.parseInt(passengersNumberTextField.getText())));
		GridBagConstraints gbc_btnChange = new GridBagConstraints();
		gbc_btnChange.insets = new Insets(0, 0, 5, 0);
		gbc_btnChange.gridx = 1;
		gbc_btnChange.gridy = 4;
		contentPane.add(btnChange, gbc_btnChange);
		
		scrollPane = new JScrollPane();
		GridBagConstraints gbc_scrollPane = new GridBagConstraints();
		gbc_scrollPane.insets = new Insets(0, 0, 5, 0);
		gbc_scrollPane.fill = GridBagConstraints.BOTH;
		gbc_scrollPane.gridx = 1;
		gbc_scrollPane.gridy = 5;
		contentPane.add(scrollPane, gbc_scrollPane);

		listFlighstModel = new DefaultListModel<>();
		listFlights = new JList<>(listFlighstModel);
		
		listFlights.setCellRenderer(new DefaultListCellRenderer() {
			private static final long serialVersionUID = 1L;

			@Override
			public Component getListCellRendererComponent(JList<?> list, Object value, int index,
					boolean isSelected, boolean cellHasFocus) {
				Flight flight = (Flight) value;
				return super.getListCellRendererComponent(list,
					getDisplayString(flight),
					index, isSelected, cellHasFocus);
			}
		});		
		
		listFlights.addListSelectionListener(
				e -> {
					btnDeleteSelected.setEnabled(listFlights.getSelectedIndex() != -1);
					enebleButtonChange();
				});	
		
		scrollPane.setViewportView(listFlights);
		listFlights.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		listFlights.setName("flightList");
		
		btnDeleteSelected = new JButton("Delete Selected");
		btnDeleteSelected.setEnabled(false);
		btnDeleteSelected.addActionListener(e -> airportController.deleteFlight(listFlights.getSelectedValue()));
		
		GridBagConstraints gbc_btnDeleteSelected = new GridBagConstraints();
		gbc_btnDeleteSelected.insets = new Insets(0, 0, 5, 0);
		gbc_btnDeleteSelected.gridx = 1;
		gbc_btnDeleteSelected.gridy = 6;
		contentPane.add(btnDeleteSelected, gbc_btnDeleteSelected);
		
		lblErrorMessage = new JLabel(" ");
		lblErrorMessage.setName("errorMessageLabel");
		GridBagConstraints gbc_lblErrorMessage = new GridBagConstraints();
		gbc_lblErrorMessage.gridwidth = 2;
		gbc_lblErrorMessage.gridx = 0;
		gbc_lblErrorMessage.gridy = 7;
		contentPane.add(lblErrorMessage, gbc_lblErrorMessage);
	}

	private void enebleButtonChange() {
		btnChange.setEnabled(idTextField.getText().trim().isEmpty() &&
					destinationTextField.getText().trim().isEmpty() && 
					!passengersNumberTextField.getText().trim().isEmpty() && 
					!listFlights.isSelectionEmpty());
	}

	@Override
	public void showAllFlights(List<Flight> flights) {
		flights.stream().forEach(listFlighstModel::addElement);
	}

	@Override
	public void showError(String message, Flight flight) {
		lblErrorMessage.setText(message + ": " + getDisplayString(flight));
	}

	@Override
	public void flightAdded(Flight flight) {
		listFlighstModel.addElement(flight);
		resetErrorLabel();
	}

	@Override
	public void flightRemoved(Flight flight) {
		listFlighstModel.removeElement(flight);
		resetErrorLabel();
	}

	@Override
	public void flightChanged(Flight flightToChange, int newPassengersNumber) {
		int index = listFlighstModel.indexOf(flightToChange);
		listFlighstModel.setElementAt(new Flight(flightToChange.getId(), flightToChange.getDestination(), newPassengersNumber), index);
		resetErrorLabel();
	}
	
	private void resetErrorLabel() {
		lblErrorMessage.setText(" ");
	}
	
	private String getDisplayString(Flight flight) {
		return flight.getId() + " - " + flight.getDestination() + " - " + flight.getPassengersNumber();
	}

	@Override
	public void showErrorFlightNotFound(String message, Flight flight) {
		lblErrorMessage.setText(message + ": " + getDisplayString(flight));
		listFlighstModel.removeElement(flight);		
	}

}
