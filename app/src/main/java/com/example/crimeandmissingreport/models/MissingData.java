package com.example.crimeandmissingreport.models;

public class MissingData {
    private String missID,userID,name,age,gender,lastSeen,details,dateTime,status,image,zipCode;

    public MissingData() {
    }

    public MissingData(String missID, String userID, String name, String age, String gender, String
            lastSeen, String details, String dateTime, String status, String image, String zipCode) {
        this.missID = missID;
        this.userID = userID;
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.lastSeen = lastSeen;
        this.details = details;
        this.dateTime = dateTime;
        this.status = status;
        this.image = image;
        this.zipCode = zipCode;

    }

    public String getMissID() {
        return missID;
    }

    public void setMissID(String missID) {
        this.missID = missID;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getLastSeen() {
        return lastSeen;
    }

    public void setLastSeen(String lastSeen) {
        this.lastSeen = lastSeen;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
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

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }
}
