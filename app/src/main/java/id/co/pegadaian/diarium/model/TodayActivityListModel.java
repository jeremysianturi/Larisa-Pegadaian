package id.co.pegadaian.diarium.model;

public class TodayActivityListModel {
    String full_name;
    String personal_number;
    String activity_id;
    String activity_type;
    String activity_title;
    String activity_start;
    String activity_finish;
    String approval_status;

    public TodayActivityListModel(String full_name, String personal_number, String activity_id, String activity_type, String activity_title, String activity_start, String activity_finish, String approval_status) {
        this.full_name = full_name;
        this.personal_number = personal_number;
        this.activity_id = activity_id;
        this.activity_type = activity_type;
        this.activity_title = activity_title;
        this.activity_start = activity_start;
        this.activity_finish = activity_finish;
        this.approval_status = approval_status;
    }

    public String getFull_name() {
        return full_name;
    }

    public void setFull_name(String full_name) {
        this.full_name = full_name;
    }

    public String getPersonal_number() {
        return personal_number;
    }

    public void setPersonal_number(String personal_number) {
        this.personal_number = personal_number;
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

    public String getApproval_status() {
        return approval_status;
    }

    public void setApproval_status(String approval_status) {
        this.approval_status = approval_status;
    }
}
