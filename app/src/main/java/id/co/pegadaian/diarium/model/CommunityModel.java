package id.co.pegadaian.diarium.model;

public class CommunityModel {
    String begin_date;
    String end_date;
    String community_id;
    String community_name;
    String community_desc;
    String community_type;
    String community_max_person;
    String community_date;
    String change_date;
    String change_user;
    String business_code;
    String personal_number;
    String role;

    public CommunityModel(String begin_date, String end_date, String community_id, String community_name, String community_desc, String community_type, String community_max_person, String community_date, String change_date, String change_user, String business_code, String personal_number, String role) {
        this.begin_date = begin_date;
        this.end_date = end_date;
        this.community_id = community_id;
        this.community_name = community_name;
        this.community_desc = community_desc;
        this.community_type = community_type;
        this.community_max_person = community_max_person;
        this.community_date = community_date;
        this.change_date = change_date;
        this.change_user = change_user;
        this.business_code = business_code;
        this.personal_number = personal_number;
        this.role = role;
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

    public String getCommunity_id() {
        return community_id;
    }

    public void setCommunity_id(String community_id) {
        this.community_id = community_id;
    }

    public String getCommunity_name() {
        return community_name;
    }

    public void setCommunity_name(String community_name) {
        this.community_name = community_name;
    }

    public String getCommunity_desc() {
        return community_desc;
    }

    public void setCommunity_desc(String community_desc) {
        this.community_desc = community_desc;
    }

    public String getCommunity_type() {
        return community_type;
    }

    public void setCommunity_type(String community_type) {
        this.community_type = community_type;
    }

    public String getCommunity_max_person() {
        return community_max_person;
    }

    public void setCommunity_max_person(String community_max_person) {
        this.community_max_person = community_max_person;
    }

    public String getCommunity_date() {
        return community_date;
    }

    public void setCommunity_date(String community_date) {
        this.community_date = community_date;
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

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
