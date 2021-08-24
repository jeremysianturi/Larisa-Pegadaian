package id.co.pegadaian.diarium.model;

public class CommunityParticipantModel {
    String begin_date;
    String end_date;
    String business_code;
    String personal_number;
    String community_id;
    String community_role;
    String aprov;
    String change_date;
    String change_user;
    String full_name;
    String profile;

    public CommunityParticipantModel(String begin_date, String end_date, String business_code, String personal_number, String community_id, String community_role, String aprov, String change_date, String change_user, String full_name, String profile) {
        this.begin_date = begin_date;
        this.end_date = end_date;
        this.business_code = business_code;
        this.personal_number = personal_number;
        this.community_id = community_id;
        this.community_role = community_role;
        this.aprov = aprov;
        this.change_date = change_date;
        this.change_user = change_user;
        this.full_name = full_name;
        this.profile = profile;
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

    public String getCommunity_id() {
        return community_id;
    }

    public void setCommunity_id(String community_id) {
        this.community_id = community_id;
    }

    public String getCommunity_role() {
        return community_role;
    }

    public void setCommunity_role(String community_role) {
        this.community_role = community_role;
    }

    public String getAprov() {
        return aprov;
    }

    public void setAprov(String aprov) {
        this.aprov = aprov;
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

    public String getFull_name() {
        return full_name;
    }

    public void setFull_name(String full_name) {
        this.full_name = full_name;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }
}
