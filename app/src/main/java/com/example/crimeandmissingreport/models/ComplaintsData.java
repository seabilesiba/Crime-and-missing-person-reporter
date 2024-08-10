package com.example.crimeandmissingreport.models;

public class ComplaintsData {
    private String compID,userID,address,city,zipCode,subject,complain,date,status;

    public ComplaintsData() {
    }

    public ComplaintsData(String compID, String userID, String address, String city, String zipCode,
                          String subject, String complain, String date, String status) {
        this.compID = compID;
        this.userID = userID;
        this.address = address;
        this.city = city;
        this.zipCode = zipCode;
        this.subject = subject;
        this.complain = complain;
        this.date = date;
        this.status = status;
    }

    public String getCompID() {
        return compID;
    }

    public void setCompID(String compID) {
        this.compID = compID;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getComplain() {
        return complain;
    }

    public void setComplain(String complain) {
        this.complain = complain;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
