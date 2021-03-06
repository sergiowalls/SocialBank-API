package me.integrate.socialbank.user;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;
import java.util.Set;

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
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private boolean verified;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private boolean enabled;
    private Set<Award> awards;


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

    public Set<Award> getAwards() {
        return awards;
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

    public boolean getVerified() {
        return verified;
    }

    // TODO: encapsulate boolean attribute
    public void setVerified(boolean verified) {
        this.verified = verified;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public void setAwards(Set<Award> awards) {
        this.awards = awards;
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
                Objects.equals(image, user.image) &&
                Objects.equals(verified, user.verified) &&
                Objects.equals(awards, user.awards);
    }

    @Override
    public int hashCode() {
        return Objects.hash(email, name, surname, birthdate, gender, balance, description, verified);
    }

}
