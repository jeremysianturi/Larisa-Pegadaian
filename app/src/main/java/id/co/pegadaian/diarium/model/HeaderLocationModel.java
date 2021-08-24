package id.co.pegadaian.diarium.model;

public class HeaderLocationModel {
    String begin_date;
    String end_date;
    String business_code;
    String empcorner_id;
    String empcorner_name;
    String theme_id;
    String empcorner_desc;
    String build_code;
    String change_date;
    String change_user;
    String empcorner_loc;

    public HeaderLocationModel(String begin_date, String end_date, String business_code, String empcorner_id, String empcorner_name, String theme_id, String empcorner_desc, String build_code, String change_date, String change_user, String empcorner_loc) {
        this.begin_date = begin_date;
        this.end_date = end_date;
        this.business_code = business_code;
        this.empcorner_id = empcorner_id;
        this.empcorner_name = empcorner_name;
        this.theme_id = theme_id;
        this.empcorner_desc = empcorner_desc;
        this.build_code = build_code;
        this.change_date = change_date;
        this.change_user = change_user;
        this.empcorner_loc = empcorner_loc;
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

    public String getEmpcorner_id() {
        return empcorner_id;
    }

    public void setEmpcorner_id(String empcorner_id) {
        this.empcorner_id = empcorner_id;
    }

    public String getEmpcorner_name() {
        return empcorner_name;
    }

    public void setEmpcorner_name(String empcorner_name) {
        this.empcorner_name = empcorner_name;
    }

    public String getTheme_id() {
        return theme_id;
    }

    public void setTheme_id(String theme_id) {
        this.theme_id = theme_id;
    }

    public String getEmpcorner_desc() {
        return empcorner_desc;
    }

    public void setEmpcorner_desc(String empcorner_desc) {
        this.empcorner_desc = empcorner_desc;
    }

    public String getBuild_code() {
        return build_code;
    }

    public void setBuild_code(String build_code) {
        this.build_code = build_code;
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

    public String getEmpcorner_loc() {
        return empcorner_loc;
    }

    public void setEmpcorner_loc(String empcorner_loc) {
        this.empcorner_loc = empcorner_loc;
    }
    @Override
    public String toString() {
        return empcorner_loc;
    }
}
