package di5.data.model;

import java.util.Date;

public class Event extends BaseEntity {
    private String title;
    private String description;
    private Date eventDate;
    private Date eventTime;
    private String eventLocationLatitude;
    private String eventLocationLongitude;

    public Event() {
        title = null;
        description = null;
        eventDate = new Date();
        eventTime = new Date();
        eventLocationLatitude = null;
        eventLocationLongitude = null;
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

    public Date getEventDate() {
        return eventDate;
    }

    public void setEventDate(Date eventDate) {
        this.eventDate = eventDate;
    }

    public Date getEventTime() {
        return eventTime;
    }

    public void setEventTime(Date eventTime) {
        this.eventTime = eventTime;
    }

    public String getEventLocationLatitude() {
        return eventLocationLatitude;
    }

    public void setEventLocationLatitude(String eventLocationLatitude) {
        this.eventLocationLatitude = eventLocationLatitude;
    }

    public String getEventLocationLongitude() {
        return eventLocationLongitude;
    }

    public void setEventLocationLongitude(String eventLocationLongitude) {
        this.eventLocationLongitude = eventLocationLongitude;
    }
}
