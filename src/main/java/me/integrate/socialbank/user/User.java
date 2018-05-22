package me.integrate.socialbank.user;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

public class User {

    public enum Gender {
        FEMALE,
        MALE,
        OTHER
    }

    private String email;
    private String name;
    private String surname;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;
    private Date birthdate;
    private Gender gender;
    private float balance;
    private String description;
    private String image; //base 64
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private boolean enabled;


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

    public Date getBirthdate() {
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

    public String getImage() {
        return image;
    }

    public boolean getEnabled() {
        return enabled;
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

    public void setDescription(String description) {
        this.description = description;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Float.compare(user.balance, balance) == 0 &&
                Objects.equals(email, user.email) &&
                Objects.equals(name, user.name) &&
                Objects.equals(surname, user.surname) &&
                new SimpleDateFormat("yyyyMMdd").format(birthdate).equals(
                        new SimpleDateFormat("yyyyMMdd").format(user.birthdate)) &&
                gender == user.gender &&
                Objects.equals(description, user.description) &&
                Objects.equals(image, user.image);
    }

    @Override
    public int hashCode() {

        return Objects.hash(email, name, surname, birthdate, gender, balance, description);
    }

}
