package id.co.pegadaian.diarium.model;

public class EmployeeCareReplyModel {
    String object_identifier;
    String begin_date;
    String end_date;
    String business_code;
    String personal_number;
    String id_tiket;
    String reply_text;
    String content_field;
    String change_date;
    String change_user;
    String name;

    public EmployeeCareReplyModel(String object_identifier, String begin_date, String end_date, String business_code, String personal_number, String id_tiket, String reply_text, String content_field, String change_date, String change_user, String name) {
        this.object_identifier = object_identifier;
        this.begin_date = begin_date;
        this.end_date = end_date;
        this.business_code = business_code;
        this.personal_number = personal_number;
        this.id_tiket = id_tiket;
        this.reply_text = reply_text;
        this.content_field = content_field;
        this.change_date = change_date;
        this.change_user = change_user;
        this.name = name;
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

    public String getPersonal_number() {
        return personal_number;
    }

    public void setPersonal_number(String personal_number) {
        this.personal_number = personal_number;
    }

    public String getId_tiket() {
        return id_tiket;
    }

    public void setId_tiket(String id_tiket) {
        this.id_tiket = id_tiket;
    }

    public String getReply_text() {
        return reply_text;
    }

    public void setReply_text(String reply_text) {
        this.reply_text = reply_text;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
