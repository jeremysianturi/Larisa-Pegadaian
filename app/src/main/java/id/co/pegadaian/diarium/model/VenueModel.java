package id.co.pegadaian.diarium.model;

public class VenueModel {
    String begin_date;
    String end_date;
    String business_code;
    String personal_number;
    String event_id;
    String venue_id;
    String title;
    String venue_desc;
    String latitude;
    String longitude;
    String image;
    String change_date;
    String change_user;

    public VenueModel(String begin_date, String end_date, String business_code, String personal_number, String event_id, String venue_id, String title, String venue_desc, String latitude, String longitude, String image, String change_date, String change_user) {
        this.begin_date = begin_date;
        this.end_date = end_date;
        this.business_code = business_code;
        this.personal_number = personal_number;
        this.event_id = event_id;
        this.venue_id = venue_id;
        this.title = title;
        this.venue_desc = venue_desc;
        this.latitude = latitude;
        this.longitude = longitude;
        this.image = image;
        this.change_date = change_date;
        this.change_user = change_user;
    }

    public String getBegin_date() {
        return begin_date;
    }

    public void setBegin_date(String begin_date) {
        this.begin_date = begin_date;
    }

    public String getEnd_date() {
        return end_date;
    }

    public void setEnd_date(String end_date) {
        this.end_date = end_date;
    }

    public String getBusiness_code() {
        return business_code;
    }

    public void setBusiness_code(String business_code) {
        this.business_code = business_code;
    }

    public String getPersonal_number() {
        return personal_number;
    }

    public void setPersonal_number(String personal_number) {
        this.personal_number = personal_number;
    }

    public String getEvent_id() {
        return event_id;
    }

    public void setEvent_id(String event_id) {
        this.event_id = event_id;
    }

    public String getVenue_id() {
        return venue_id;
    }

    public void setVenue_id(String venue_id) {
        this.venue_id = venue_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getVenue_desc() {
        return venue_desc;
    }

    public void setVenue_desc(String venue_desc) {
        this.venue_desc = venue_desc;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getChange_date() {
        return change_date;
    }

    public void setChange_date(String change_date) {
        this.change_date = change_date;
    }

    public String getChange_user() {
        return change_user;
    }

    public void setChange_user(String change_user) {
        this.change_user = change_user;
    }
}
