package id.co.pegadaian.diarium.model;

public class InboxModel {
    String begin_date;
    String end_date;
    String business_code;
    String personal_number;
    String inbox_id;
    String title;
    String description;
    String change_date;
    String change_user;

    public InboxModel(String begin_date, String end_date, String business_code, String personal_number, String inbox_id, String title, String description, String change_date, String change_user) {
        this.begin_date = begin_date;
        this.end_date = end_date;
        this.business_code = business_code;
        this.personal_number = personal_number;
        this.inbox_id = inbox_id;
        this.title = title;
        this.description = description;
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

    public String getInbox_id() {
        return inbox_id;
    }

    public void setInbox_id(String inbox_id) {
        this.inbox_id = inbox_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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
