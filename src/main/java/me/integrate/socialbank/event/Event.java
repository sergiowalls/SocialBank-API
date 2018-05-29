package me.integrate.socialbank.event;

import org.springframework.lang.Nullable;

import java.util.Calendar;
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
    private Double latitude;
    private Double longitude;
    private Category category;

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

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
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
                Objects.equals(isDemand, event.isDemand) &&
                Objects.equals(latitude, event.latitude) &&
                Objects.equals(longitude, event.longitude) &&
                Objects.equals(category, event.category);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(id, creatorEmail, iniDate, endDate, /*hours,*/ location, title, description, category);
    }

    public boolean isClosed() {
        return false;
    }

    public boolean beginsInLessThan24h() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.add(Calendar.DAY_OF_MONTH, 1);
        return (iniDate.before(cal.getTime()));
    }

    public float getIntervalTime() {
        return endDate.getTime() - iniDate.getTime() / (60 * 60 * 1000);
    }
}
