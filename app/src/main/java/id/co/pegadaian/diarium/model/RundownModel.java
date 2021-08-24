package id.co.pegadaian.diarium.model;

public class RundownModel {

    private String begin_date;
    private String end_date;
    private String business_code;
    private String personal_number;
    private String event_id;
    private String agenda_id;
    private String rundown_id;
    private String name;
    private String description;
    private String rundown_begin;
    private String rundown_end;
    private String place;
    private String layout;
    private String change_date;
    private String change_user;

    public RundownModel(String begin_date, String end_date, String business_code, String personal_number, String event_id, String agenda_id, String rundown_id, String name, String description, String rundown_begin, String rundown_end, String place, String layout, String change_date, String change_user) {
        this.begin_date = begin_date;
        this.end_date = end_date;
        this.business_code = business_code;
        this.personal_number = personal_number;
        this.event_id = event_id;
        this.agenda_id = agenda_id;
        this.rundown_id = rundown_id;
        this.name = name;
        this.description = description;
        this.rundown_begin = rundown_begin;
        this.rundown_end = rundown_end;
        this.place = place;
        this.layout = layout;
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

    public String getAgenda_id() {
        return agenda_id;
    }

    public void setAgenda_id(String agenda_id) {
        this.agenda_id = agenda_id;
    }

    public String getRundown_id() {
        return rundown_id;
    }

    public void setRundown_id(String rundown_id) {
        this.rundown_id = rundown_id;
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

    public String getRundown_begin() {
        return rundown_begin;
    }

    public void setRundown_begin(String rundown_begin) {
        this.rundown_begin = rundown_begin;
    }

    public String getRundown_end() {
        return rundown_end;
    }

    public void setRundown_end(String rundown_end) {
        this.rundown_end = rundown_end;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getLayout() {
        return layout;
    }

    public void setLayout(String layout) {
        this.layout = layout;
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
