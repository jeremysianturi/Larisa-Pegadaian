package id.co.pegadaian.diarium.model;

public class EventModel {
    String begin_date;
    String end_date;
    String business_code;
    String personal_number;
    String id;
    String code;
    String event_type;
    String theme;
    String name;
    String description;
    String location;
    String city;
    String phone;
    String link;
    String event_start;
    String event_end;
    String change_date;
    String change_user;
    String image;

    public EventModel(String begin_date, String end_date, String business_code, String personal_number, String id, String code, String event_type, String theme, String name, String description, String location, String city, String phone, String link, String event_start, String event_end, String change_date, String change_user, String image) {
        this.begin_date = begin_date;
        this.end_date = end_date;
        this.business_code = business_code;
        this.personal_number = personal_number;
        this.id = id;
        this.code = code;
        this.event_type = event_type;
        this.theme = theme;
        this.name = name;
        this.description = description;
        this.location = location;
        this.city = city;
        this.phone = phone;
        this.link = link;
        this.event_start = event_start;
        this.event_end = event_end;
        this.change_date = change_date;
        this.change_user = change_user;
        this.image = image;
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getEvent_type() {
        return event_type;
    }

    public void setEvent_type(String event_type) {
        this.event_type = event_type;
    }

    public String getTheme() {
        return theme;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getEvent_start() {
        return event_start;
    }

    public void setEvent_start(String event_start) {
        this.event_start = event_start;
    }

    public String getEvent_end() {
        return event_end;
    }

    public void setEvent_end(String event_end) {
        this.event_end = event_end;
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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
