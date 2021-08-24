package id.co.pegadaian.diarium.model;

public class MessageContentModel {
    String begin_date;
    String end_date;
    String business_code;
    String personal_number;
    String community_id;
    String content_type;
    String content_field;
    String change_date;
    String change_user;
    String content_id;
    String content_title;
    String content_desc;
    String profile;
    String nama;

    public MessageContentModel(String begin_date, String end_date, String business_code, String personal_number, String community_id, String content_type, String content_field, String change_date, String change_user, String content_id, String content_title, String content_desc, String profile, String nama) {
        this.begin_date = begin_date;
        this.end_date = end_date;
        this.business_code = business_code;
        this.personal_number = personal_number;
        this.community_id = community_id;
        this.content_type = content_type;
        this.content_field = content_field;
        this.change_date = change_date;
        this.change_user = change_user;
        this.content_id = content_id;
        this.content_title = content_title;
        this.content_desc = content_desc;
        this.profile = profile;
        this.nama = nama;
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

    public String getContent_type() {
        return content_type;
    }

    public void setContent_type(String content_type) {
        this.content_type = content_type;
    }

    public String getContent_field() {
        return content_field;
    }

    public void setContent_field(String content_field) {
        this.content_field = content_field;
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

    public String getContent_id() {
        return content_id;
    }

    public void setContent_id(String content_id) {
        this.content_id = content_id;
    }

    public String getContent_title() {
        return content_title;
    }

    public void setContent_title(String content_title) {
        this.content_title = content_title;
    }

    public String getContent_desc() {
        return content_desc;
    }

    public void setContent_desc(String content_desc) {
        this.content_desc = content_desc;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }
}
