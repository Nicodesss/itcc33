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

public class TransactionPopup extends Stage {

    private TextField totalAmountField = new TextField();
    private TextField modeOfPaymentField = new TextField();
    private TextField timeConsumptionField = new TextField();
    private final int parkingSlotNumber;

    public TransactionPopup(int parkingSlotNumber) {
        this.parkingSlotNumber = parkingSlotNumber;
        initializeUI();
    }

    private void initializeUI() {
        GridPane gridPane = new GridPane();
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setHgap(10);
        gridPane.setVgap(10);

        gridPane.add(new Label("Total Amount:"), 0, 1);
        gridPane.add(totalAmountField, 1, 1);
        gridPane.add(new Label("Mode of Payment:"), 0, 2);
        gridPane.add(modeOfPaymentField, 1, 2);
        gridPane.add(new Label("Time in Hours:"), 0, 3);
        gridPane.add(timeConsumptionField, 1, 3);

        Button saveButton = new Button("Save");
        saveButton.setOnAction(e -> handleSave());
        gridPane.add(saveButton, 1, 4);

        Scene scene = new Scene(gridPane, 300, 250);
        setScene(scene);
        setTitle("Transaction Details");
        initModality(Modality.APPLICATION_MODAL);
        initStyle(StageStyle.UTILITY);
    }

    private void handleSave() {
        if (totalAmountField.getText().isEmpty() || modeOfPaymentField.getText().isEmpty() || timeConsumptionField.getText().isEmpty()) {
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

    public void fillFieldsWithTransaction(Transaction transaction) {
        totalAmountField.setText(transaction.getTotalAmount());
        modeOfPaymentField.setText(transaction.getModeOfPayment());
        // You can add more lines to set other fields based on your Transaction class
    }

    public Transaction showAndWait(Customer customer) {
        showAndWait();
        return new Transaction(
                generateTransactionID(),
                totalAmountField.getText(),
                modeOfPaymentField.getText(),
                parkingSlotNumber,
                Integer.parseInt(timeConsumptionField.getText()), // Assuming timeConsumption is an integer
                generateTransactionDate(),
                generateEmployeeID(),
                customer.getMembershipId()
        );
    }

    private String generateTransactionID() {
        return "TRN" + System.currentTimeMillis();
    }

    private String generateTransactionDate() {
        return "2023-11-27"; // Placeholder value, replace with actual implementation
    }

    private String generateEmployeeID() {
        return "EMP001"; // Placeholder value, replace with actual implementation
    }
}
