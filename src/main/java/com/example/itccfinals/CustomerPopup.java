package com.example.itccfinals;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class CustomerPopup extends Stage {

    private TextField phoneNumberField = new TextField();
    private TextField sexField = new TextField();
    private TextField addressField = new TextField();
    private TextField nameField = new TextField();
    private TextField carTypeField = new TextField();
    private TextField plateNumberField = new TextField();
    private TextField membershipIdField = new TextField();

    public CustomerPopup() {
        initializeUI();
    }

    private void initializeUI() {
        GridPane gridPane = new GridPane();
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setHgap(10);
        gridPane.setVgap(10);

        gridPane.add(new Label("Phone Number:"), 0, 0);
        gridPane.add(phoneNumberField, 1, 0);
        gridPane.add(new Label("Sex:"), 0, 1);
        gridPane.add(sexField, 1, 1);
        gridPane.add(new Label("Address:"), 0, 2);
        gridPane.add(addressField, 1, 2);
        gridPane.add(new Label("Name:"), 0, 3);
        gridPane.add(nameField, 1, 3);
        gridPane.add(new Label("Car Type:"), 0, 4);
        gridPane.add(carTypeField, 1, 4);
        gridPane.add(new Label("Plate Number:"), 0, 5);
        gridPane.add(plateNumberField, 1, 5);
        gridPane.add(new Label("Membership ID:"), 0, 6);
        gridPane.add(membershipIdField, 1, 6);

        Button saveButton = new Button("Save");
        saveButton.setOnAction(e -> handleSave());
        gridPane.add(saveButton, 1, 7);

        Scene scene = new Scene(gridPane, 300, 300);
        setScene(scene);
        setTitle("Customer Details");
        initModality(Modality.APPLICATION_MODAL);
        initStyle(StageStyle.UTILITY);
    }

    private void handleSave() {
        // Validate and save data if needed
        if (phoneNumberField.getText().isEmpty() || nameField.getText().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "Please fill in required fields.");
        } else {
            close();
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }

    public Customer showAndReturnCustomer() {
        showAndWait();
        // Create and return a Customer object based on the entered data
        return new Customer(
                phoneNumberField.getText(),
                sexField.getText(),
                addressField.getText(),
                nameField.getText(),
                carTypeField.getText(),
                plateNumberField.getText(),
                membershipIdField.getText()
        );
    }
    }
