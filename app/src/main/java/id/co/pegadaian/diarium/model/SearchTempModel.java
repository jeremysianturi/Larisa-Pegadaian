package id.co.pegadaian.diarium.model;

public class SearchTempModel {
    String personal_number;
    String full_name;
    String job;
    String unit;
    String posisi;
    String profile;

    public SearchTempModel(String personal_number, String full_name, String job, String unit, String posisi, String profile) {
        this.personal_number = personal_number;
        this.full_name = full_name;
        this.job = job;
        this.unit = unit;
        this.posisi = posisi;
        this.profile = profile;
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

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getPosisi() {
        return posisi;
    }

    public void setPosisi(String posisi) {
        this.posisi = posisi;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }
}