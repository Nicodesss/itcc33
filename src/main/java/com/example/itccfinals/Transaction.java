package com.example.itccfinals;

import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.types.ObjectId;

public class Transaction {

    @BsonId
    private ObjectId id;
    private String transactionId;
    private String paymentId;
    private String totalAmount;
    private String modeOfPayment;
    private int parkingSlotNumber;
    private int timeConsumption;
    private String transactionDate;
    private String employeeId;
    private String membershipId;

    public Transaction(String transactionId, String totalAmount, String modeOfPayment, int parkingSlotNumber,
                       int timeConsumption, String transactionDate, String employeeId, String membershipId) {
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

    public int getTimeConsumption() {
        return timeConsumption;
    }

    public void setTimeConsumption(int timeConsumption) {
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
        return "PAY" + System.currentTimeMillis();
    }
}
