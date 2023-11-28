package com.example.itccfinals;

import com.mongodb.client.MongoCollection;
import org.bson.Document;
import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.types.ObjectId;

public class Transaction {
    private static MongoCollection<Transaction> transactionCollection;

    public static void setTransactionCollection(MongoCollection<Transaction> collection) {
        transactionCollection = collection;
    }

    @BsonId
    private ObjectId id;
    private String transactionId;
    private String paymentId;
    private String totalAmount;
    private String modeOfPayment;
    private int parkingSlotNumber;
    private long timeConsumption; // Changed to long for time in seconds
    private String transactionDate;
    private String employeeId;
    private String membershipId;

    // Public default (no-argument) constructor
    public Transaction() {
    }

    public Transaction(String transactionId, String totalAmount, String modeOfPayment, int parkingSlotNumber,
                       long timeConsumption, String transactionDate, String employeeId, String membershipId) {
        // Validate inputs before assigning them to fields
        if (transactionId == null || totalAmount == null || modeOfPayment == null ||
                transactionDate == null || employeeId == null || membershipId == null) {
            throw new IllegalArgumentException("Input values cannot be null");
        }

        // Validate other conditions if needed

        // Assign values to fields
        this.transactionId = transactionId;
        this.paymentId = generatePaymentId();
        this.totalAmount = totalAmount;
        this.modeOfPayment = modeOfPayment;
        this.parkingSlotNumber = parkingSlotNumber;
        this.timeConsumption = timeConsumption;
        this.transactionDate = transactionDate;
        this.employeeId = employeeId;
        this.membershipId = membershipId;
    }

    public static Transaction findTransactionByPaymentId(String paymentId) {
        // Check if transactionCollection is not null
        if (transactionCollection != null) {
            Document query = new Document("paymentId", paymentId);
            return transactionCollection.find(query, Transaction.class).first();
        } else {
            System.err.println("Transaction collection is not initialized.");
            return null;
        }
    }

    // Getters and setters

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(String paymentId) {
        this.paymentId = paymentId;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getModeOfPayment() {
        return modeOfPayment;
    }

    public void setModeOfPayment(String modeOfPayment) {
        this.modeOfPayment = modeOfPayment;
    }

    public int getParkingSlotNumber() {
        return parkingSlotNumber;
    }

    public void setParkingSlotNumber(int parkingSlotNumber) {
        this.parkingSlotNumber = parkingSlotNumber;
    }

    public long getTimeConsumption() {
        return timeConsumption;
    }

    public void setTimeConsumption(long timeConsumption) {
        this.timeConsumption = timeConsumption;
    }

    public String getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(String transactionDate) {
        this.transactionDate = transactionDate;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public String getMembershipId() {
        return membershipId;
    }

    public void setMembershipId(String membershipId) {
        this.membershipId = membershipId;
    }

    private String generatePaymentId() {
        // Generate a random 3-digit number
        int randomNum = (int) (Math.random() * 900) + 100;

        // Concatenate "PAY" with the random number
        return "PAY" + randomNum;
    }
}
