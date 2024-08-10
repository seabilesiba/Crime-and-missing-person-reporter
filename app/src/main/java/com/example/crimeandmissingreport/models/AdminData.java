package com.example.crimeandmissingreport.models;

public class AdminData {
    private String  image, surname,location, name, email, phone, pass;

    public AdminData() {
    }

    public AdminData( String image, String surname,
                     String location, String name, String email, String phone, String pass) {

        this.image = image;
        this.surname = surname;
        this.location = location;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.pass = pass;
    }



    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }
}
