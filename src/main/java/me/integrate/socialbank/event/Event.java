package me.integrate.socialbank.event;

import java.util.Date;
import java.util.Objects;

public class Event {

    private int id;
    private String creatorEmail;
    private Date iniDate, endDate;
    private int hours;
    private String location;
    private String title;
    private String description;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCreatorEmail() {
        return creatorEmail;
    }

    public void setCreatorEmail(String creatorEmail) {
        this.creatorEmail = creatorEmail;
    }

    public Date getIniDate() {
        return iniDate;
    }

    public void setIniDate(Date iniDate) {
        this.iniDate = iniDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getHours() { return hours; }

    public void setHours(int hours) { this.hours = hours; }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Event event = (Event) o;
        return  id == event.id &&
                Objects.equals(creatorEmail, event.creatorEmail) &&
                Objects.equals(iniDate, event.iniDate) &&
                Objects.equals(endDate, event.endDate) &&
                hours == event.hours &&
                Objects.equals(location, event.location) &&
                Objects.equals(title, event.title) &&
                Objects.equals(description, event.description);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(id, creatorEmail, iniDate, endDate, hours, location, title, description);
    }

}
