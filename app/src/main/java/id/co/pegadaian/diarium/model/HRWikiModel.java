package id.co.pegadaian.diarium.model;

public class HRWikiModel {
    String object_identifier;
    String begin_date;
    String end_date;
    String business_code;
    String id_incident;
    String kasus;
    String solusi;
    String like;
    String dislike;
    String hits;
    String active_flag;
    String application_id;
    String change_date;
    String change_user;

    public HRWikiModel(String object_identifier, String begin_date, String end_date, String business_code, String id_incident, String kasus, String solusi, String like, String dislike, String hits, String active_flag, String application_id, String change_date, String change_user) {
        this.object_identifier = object_identifier;
        this.begin_date = begin_date;
        this.end_date = end_date;
        this.business_code = business_code;
        this.id_incident = id_incident;
        this.kasus = kasus;
        this.solusi = solusi;
        this.like = like;
        this.dislike = dislike;
        this.hits = hits;
        this.active_flag = active_flag;
        this.application_id = application_id;
        this.change_date = change_date;
        this.change_user = change_user;
    }

    public String getObject_identifier() {
        return object_identifier;
    }

    public void setObject_identifier(String object_identifier) {
        this.object_identifier = object_identifier;
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

    public String getId_incident() {
        return id_incident;
    }

    public void setId_incident(String id_incident) {
        this.id_incident = id_incident;
    }

    public String getKasus() {
        return kasus;
    }

    public void setKasus(String kasus) {
        this.kasus = kasus;
    }

    public String getSolusi() {
        return solusi;
    }

    public void setSolusi(String solusi) {
        this.solusi = solusi;
    }

    public String getLike() {
        return like;
    }

    public void setLike(String like) {
        this.like = like;
    }

    public String getDislike() {
        return dislike;
    }

    public void setDislike(String dislike) {
        this.dislike = dislike;
    }

    public String getHits() {
        return hits;
    }

    public void setHits(String hits) {
        this.hits = hits;
    }

    public String getActive_flag() {
        return active_flag;
    }

    public void setActive_flag(String active_flag) {
        this.active_flag = active_flag;
    }

    public String getApplication_id() {
        return application_id;
    }

    public void setApplication_id(String application_id) {
        this.application_id = application_id;
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
