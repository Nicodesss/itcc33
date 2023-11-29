package com.example.itccfinals;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.bson.types.ObjectId;

import java.text.ParseException;
import java.time.LocalDate;
import java.util.Date;

public class ParkingLotReservationApp extends Application {
    private Button deleteMembershipButton;
    private GridPane gridPane;
    private MongoCollection<Customer> customerCollection;
    private MongoCollection<Transaction> transactionCollection;
    private MongoCollection<Membership> membershipCollection;

    private TextField membershipIdTextField = new TextField();
    TextField paymentIdTextField = new TextField();
    private Button updateExpiryDateButton;
    private TextField updateMembershipIdField;
    private TextField newExpiryDateField;



    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {

        // Initialize MongoDB
        MongoClient mongoClient = new MongoClient("localhost", 27017);
        CodecRegistry pojoCodecRegistry = CodecRegistries.fromProviders(
                PojoCodecProvider.builder().automatic(true).build());

        // Add the POJO codec registry to the existing codec registry
        CodecRegistry codecRegistry = CodecRegistries.fromRegistries(
                MongoClient.getDefaultCodecRegistry(), pojoCodecRegistry);

        // Connect to the database using the new codec registry
        MongoDatabase database = mongoClient.getDatabase("Reservation")
                .withCodecRegistry(codecRegistry);

        customerCollection = database.getCollection("customer", Customer.class);
        transactionCollection = database.getCollection("transaction", Transaction.class);
        membershipCollection = database.getCollection("membership", Membership.class);

        Membership.setMembershipCollection(membershipCollection);
        Transaction.setTransactionCollection(transactionCollection);



        // Initialize GUI
        gridPane = createParkingGrid();
        Scene scene = new Scene(gridPane, 400, 400);

        primaryStage.setTitle("Car Parking Reservation System");
        primaryStage.setScene(scene);
        primaryStage.show();

        gridPane.add(new Label("Payment ID:"), 0, 8);

        gridPane.add(paymentIdTextField, 1, 8);



        gridPane.add(new Label("Membership ID:"), 0, 7);
        gridPane.add(membershipIdTextField, 1, 7);



        deleteMembershipButton = new Button("Delete Membership");
        deleteMembershipButton.setOnAction(e -> handleDeleteMembership());
        gridPane.add(deleteMembershipButton, 0, 7);

        Button searchTransactionButton = new Button("Search Transaction");
        searchTransactionButton.setOnAction(e -> handleSearchTransaction());
        gridPane.add(searchTransactionButton, 0, 8);


        updateExpiryDateButton = new Button("Update Expiry Date");
        updateExpiryDateButton.setOnAction(e -> handleUpdateExpiryDate());
        gridPane.add(updateExpiryDateButton, 0, 9);

        updateMembershipIdField = new TextField();
        gridPane.add(new Label("Membership ID to Update:"), 0, 10);
        gridPane.add(updateMembershipIdField, 1, 10);

        newExpiryDateField = new TextField();
        gridPane.add(new Label("New Expiry Date:"), 0, 11);
        gridPane.add(newExpiryDateField, 1, 11);
    }
    private void handleUpdateExpiryDate() {
        String membershipIdToUpdate = updateMembershipIdField.getText();
        String newExpiryDate = newExpiryDateField.getText();

        if (!membershipIdToUpdate.isEmpty() && !newExpiryDate.isEmpty()) {
            // Call the update method
            Membership.updateExpiryDate(membershipIdToUpdate, newExpiryDate);

            showAlert(Alert.AlertType.INFORMATION, "Update Successful", "Expiry date updated successfully!");
        } else {
            showAlert(Alert.AlertType.ERROR, "Input Error", "Membership ID and new expiry date cannot be empty.");
        }
    }
    private void showAlert(Alert.AlertType alertType, String title, String content) {
        showAlert(alertType, title, content, null);
    }


    private String getPaymentIdFromUserInput() {
        return paymentIdTextField.getText();
    }

    private void handleSearchTransaction() {
        String paymentId = getPaymentIdFromUserInput();

        // Check if the input is not empty
        if (!paymentId.isEmpty()) {
            // Perform the search operation
            Transaction foundTransaction = Transaction.findTransactionByPaymentId(paymentId);

            // Check if the transaction is found
            if (foundTransaction != null) {
                // Display the transaction details in a new popup
                showTransactionDetailsPopup(foundTransaction);
            } else {
                showAlert(Alert.AlertType.INFORMATION, "Search Result", "No transaction found with Payment ID: " + paymentId);
            }
        } else {
            showAlert(Alert.AlertType.ERROR, "Input Error", "Payment ID cannot be empty.");
        }
    }

    private void showTransactionDetailsPopup(Transaction transaction) {
        TransactionPopup transactionPopup = new TransactionPopup(transaction.getParkingSlotNumber());
        transactionPopup.fillFieldsWithTransaction(transaction);
        transactionPopup.showAndWait();
    }
    private void handleAddMembership() {
        MembershipPopup membershipPopup = new MembershipPopup(membershipCollection);
        Membership newMembership = membershipPopup.showAndReturnMembership();
        if (newMembership != null) {
            // Additional logic if needed
            System.out.println("New Membership: " + newMembership);
        }
    }

    private GridPane createParkingGrid() {
        GridPane gridPane = new GridPane();
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        Button addMembershipButton = new Button("Add Membership");
        addMembershipButton.setOnAction(e -> handleAddMembership());
        gridPane.add(addMembershipButton, 0, 6);

        // Create 10 clickable green boxes for parking slots
        for (int i = 1; i <= 10; i++) {
            Button parkingSlot = createParkingSlotButton(i);
            gridPane.add(parkingSlot, (i - 1) % 5, (i - 1) / 5);
        }

        return gridPane;
    }

    private Button createParkingSlotButton(int slotNumber) {
        Button button = new Button("Slot " + slotNumber);
        button.setStyle("-fx-background-color: green");
        button.setOnAction(e -> handleParkingSlotClick(slotNumber));
        return button;
    }

    private void handleParkingSlotClick(int parkingSlotNumber) {
        // Show customer details popup
        CustomerPopup customerPopup = new CustomerPopup();
        Customer customer = customerPopup.showAndReturnCustomer();
        if (customer != null) {
            // Show transaction popup
            TransactionPopup transactionPopup = new TransactionPopup(parkingSlotNumber);
            Transaction transaction = transactionPopup.showAndWait(customer);
            if (transaction != null) {
                // Save data to MongoDB
                customerCollection.insertOne(customer);
                transactionCollection.insertOne(transaction);

                // Show the reservation success message with the PaymentId
                showAlert(Alert.AlertType.INFORMATION, "Reservation Successful", "Parking slot reserved successfully!", transaction.getPaymentId());

                updateButtonStyle(parkingSlotNumber, true);
            } else {
                showAlert(Alert.AlertType.ERROR, "Transaction Error", "Transaction details are incomplete.", null);
            }
        } else {
            showAlert(Alert.AlertType.ERROR, "Customer Error", "Customer details are incomplete.", null);
        }
    }

    private void handleDeleteMembership() {
        String membershipIdToDelete = getMembershipIdFromUserInput();

        // Check if the input is not empty
        if (!membershipIdToDelete.isEmpty()) {
            // Perform the delete operation
            Membership.deleteMembershipById(membershipIdToDelete);

            showAlert(Alert.AlertType.INFORMATION, "Delete Successful", "Membership deleted successfully!");
        } else {
            showAlert(Alert.AlertType.ERROR, "Input Error", "Membership ID cannot be empty.");
        }
    }
    private boolean isValidObjectId(String input) {
        try {
            new ObjectId(input);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    // Replace this method with the actual logic to get membership ID from the user input
    private String getMembershipIdFromUserInput() {
        return membershipIdTextField.getText();
    }
    private void updateButtonStyle(int parkingSlotNumber, boolean reserved) {
        Button button = getButtonByParkingSlotNumber(parkingSlotNumber);
        if (button != null) {
            button.setStyle(reserved ? "-fx-background-color: red" : "-fx-background-color: green");
        }
    }

    private Button getButtonByParkingSlotNumber(int parkingSlotNumber) {
        for (int i = 0; i < gridPane.getChildren().size(); i++) {
            if (gridPane.getChildren().get(i) instanceof Button) {
                Button button = (Button) gridPane.getChildren().get(i);
                int slot = Integer.parseInt(button.getText().split(" ")[1]);
                if (slot == parkingSlotNumber) {
                    return button;
                }
            }
        }
        return null;
    }

    private void showAlert(Alert.AlertType alertType, String title, String content, String additionalInfo) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(content);

        if (additionalInfo != null) {
            Label label = new Label("Additional Information:");
            TextArea textArea = new TextArea(additionalInfo);
            textArea.setEditable(false);
            VBox vbox = new VBox(label, textArea);
            alert.getDialogPane().setExpandableContent(vbox);
        }

        alert.showAndWait();
    }
}
