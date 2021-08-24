package id.co.pegadaian.diarium.model;

public class SearchModel {
//    String end_date;
//    String business_code;
    String personal_number;
    String full_name;
    String profile;
    String job;
//    String unit;
//    String gender;
//    String religion;
//    String language;
//    String national;
//    String tribe;
//    String blood_type;
//    String rhesus;
//    String marital_status;
//    String marital_date;
//    String personal_number_reference;
//    String change_date;
//    String change_user;
//    String begin_date;


    public SearchModel(String personal_number, String full_name, String profile, String job) {
        this.personal_number = personal_number;
        this.full_name = full_name;
        this.profile = profile;
        this.job = job;
    }

    public String getPersonal_number() {
        return personal_number;
    }

    public void setPersonal_number(String personal_number) {
        this.personal_number = personal_number;
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

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }
}
