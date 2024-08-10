package com.example.crimeandmissingreport.models;

public class CrimeData {
    private String crimeID,userID,street,city,zipCode,crimeDetails,date,status,image;

    public CrimeData() {
    }

    public CrimeData(String crimeID, String userID, String street, String city, String zipCode,
                     String crimeDetails, String date, String status, String image) {
        this.crimeID = crimeID;
        this.userID = userID;
        this.street = street;
        this.city = city;
        this.zipCode = zipCode;
        this.crimeDetails = crimeDetails;
        this.date = date;
        this.status = status;
        this.image = image;
    }

    public String getCrimeID() {
        return crimeID;
    }

    public void setCrimeID(String crimeID) {
        this.crimeID = crimeID;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
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

    public String getCrimeDetails() {
        return crimeDetails;
    }

    public void setCrimeDetails(String crimeDetails) {
        this.crimeDetails = crimeDetails;
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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
