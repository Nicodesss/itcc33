package com.example.itccfinals;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;

public class ParkingLotReservationApp extends Application {

    private GridPane gridPane;
    private MongoCollection<Customer> customerCollection;
    private MongoCollection<Transaction> transactionCollection;

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

        // Initialize GUI
        gridPane = createParkingGrid();
        Scene scene = new Scene(gridPane, 400, 400);

        primaryStage.setTitle("Car Parking Reservation System");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private GridPane createParkingGrid() {
        GridPane gridPane = new GridPane();
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setHgap(10);
        gridPane.setVgap(10);

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

                // Update the UI or perform other actions as needed
                showAlert(Alert.AlertType.INFORMATION, "Reservation Successful", "Parking slot reserved successfully!");

                // Update button style
                updateButtonStyle(parkingSlotNumber, true);
            } else {
                showAlert(Alert.AlertType.ERROR, "Transaction Error", "Transaction details are incomplete.");
            }
        } else {
            showAlert(Alert.AlertType.ERROR, "Customer Error", "Customer details are incomplete.");
        }
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

    private void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
