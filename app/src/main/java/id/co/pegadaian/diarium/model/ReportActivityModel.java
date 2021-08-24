package id.co.pegadaian.diarium.model;

public class ReportActivityModel {
    String begin_date;
    String end_date;
    String business_code;
    String personal_number;
    String activity_title;
    String activity_start;
    String activity_finish;
    String change_date;
    String change_user;
    String  activity_id;
    String activity_type;
    String approval_status;

    public ReportActivityModel(String begin_date, String end_date, String business_code, String personal_number, String activity_title, String activity_start, String activity_finish, String change_date, String change_user, String activity_id, String activity_type, String approval_status) {
        this.begin_date = begin_date;
        this.end_date = end_date;
        this.business_code = business_code;
        this.personal_number = personal_number;
        this.activity_title = activity_title;
        this.activity_start = activity_start;
        this.activity_finish = activity_finish;
        this.change_date = change_date;
        this.change_user = change_user;
        this.activity_id = activity_id;
        this.activity_type = activity_type;
        this.approval_status = approval_status;
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

    public String getActivity_title() {
        return activity_title;
    }

    public void setActivity_title(String activity_title) {
        this.activity_title = activity_title;
    }

    public String getActivity_start() {
        return activity_start;
    }

    public void setActivity_start(String activity_start) {
        this.activity_start = activity_start;
    }

    public String getActivity_finish() {
        return activity_finish;
    }

    public void setActivity_finish(String activity_finish) {
        this.activity_finish = activity_finish;
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

    public String getActivity_id() {
        return activity_id;
    }

    public void setActivity_id(String activity_id) {
        this.activity_id = activity_id;
    }

    public String getActivity_type() {
        return activity_type;
    }

    public void setActivity_type(String activity_type) {
        this.activity_type = activity_type;
    }

    public String getApproval_status() {
        return approval_status;
    }

    public void setApproval_status(String approval_status) {
        this.approval_status = approval_status;
    }
}
