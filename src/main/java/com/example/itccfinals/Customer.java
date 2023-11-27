package com.example.itccfinals;

import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.types.ObjectId;

public class Customer {

    @BsonId
    private ObjectId id;
    private String phoneNumber;
    private String sex;
    private String address;
    private String name;
    private String carType;
    private String plateNumber;
    private String membershipId;

    public Customer(String phoneNumber, String sex, String address, String name, String carType, String plateNumber, String membershipId) {
        this.phoneNumber = phoneNumber;
        this.sex = sex;
        this.address = address;
        this.name = name;
        this.carType = carType;
        this.plateNumber = plateNumber;
        this.membershipId = membershipId;
    }

    // Getters and setters

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCarType() {
        return carType;
    }

    public void setCarType(String carType) {
        this.carType = carType;
    }

    public String getPlateNumber() {
        return plateNumber;
    }

    public void setPlateNumber(String plateNumber) {
        this.plateNumber = plateNumber;
    }

    public String getMembershipId() {
        return membershipId;
    }

    public void setMembershipId(String membershipId) {
        this.membershipId = membershipId;
    }
}
