package me.integrate.socialbank.event;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.lang.Nullable;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
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
    private Integer capacity;
    @ApiModelProperty(readOnly = true)
    private int numberEnrolled;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private List<String> tags;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String exchangeToken;

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

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public String getExchangeToken() {
        return exchangeToken;
    }

    public void setExchangeToken(String exchangeToken) {
        this.exchangeToken = exchangeToken;
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

    @ApiModelProperty(hidden = true, readOnly = true)
    public boolean isClosed() {
        return false;
    }

    public boolean beginsInLessThan24h() {
        if (iniDate == null) return false;
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.add(Calendar.DAY_OF_MONTH, 1);
        return (iniDate.before(cal.getTime()));
    }

    public float getIntervalTime() {
        long difference = 0;
        try {
            if (iniDate != null && endDate != null) difference = endDate.getTime() - iniDate.getTime();
        }
        catch (Exception e) {
            e.printStackTrace(); //should not get here, but just in case
        }
        return difference / (60 * 60 * 1000); //convert from millisecond to hour
    }

    public Integer getCapacity() {
        return capacity;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }

    public Integer getNumberEnrolled() {
        return numberEnrolled;
    }

    public void setNumberEnrolled(Integer numberEnrolled) {
        this.numberEnrolled = numberEnrolled;
    }
}
