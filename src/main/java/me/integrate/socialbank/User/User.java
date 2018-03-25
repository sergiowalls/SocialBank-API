package me.integrate.socialbank.User;

import java.util.Date;

public class User {

    public enum Gender {
        FEMALE,
        MALE,
        OTHER
    }

    private String email;
    private String name;
    private String surname;
    private String password;
    private Date birthdate;
    private Gender gender;
    private float balance;
    private String description;


    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public String getPassword() {
        return password;
    }

    public Date getDate() {
        return birthdate;
    }

    public Gender getGender() {
        return gender;
    }

    public float getBalance() {
        return balance;
    }

    public String getDescription() {
        return description;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setBirthdate(Date birthdate) {
        this.birthdate = birthdate;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public void setBalance(float balance) {
        this.balance = balance;
    }

    public void setString(String description) {
        this.description = description;
    }
}
