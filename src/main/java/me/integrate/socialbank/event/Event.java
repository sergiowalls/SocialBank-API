package me.integrate.socialbank.event;

import org.springframework.lang.Nullable;

import java.util.Date;
import java.util.Objects;

public class Event {

    private int id;
    private String creatorEmail;
    @Nullable
    private Date iniDate, endDate;
    private String location;
    private String title;
    private String description;
    private String image;
    private Boolean isDemand;

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
                Objects.equals(location, event.location) &&
                Objects.equals(title, event.title) &&
                Objects.equals(description, event.description) &&
                Objects.equals(image, event.image) &&
                Objects.equals(isDemand, event.isDemand);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(id, creatorEmail, iniDate, endDate, /*hours,*/ location, title, description);
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Boolean isDemand() {
        return isDemand;
    }

    public void setDemand(Boolean demand) {
        isDemand = demand;
    }
}
