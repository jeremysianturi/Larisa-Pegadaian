package id.co.pegadaian.diarium.model;

public class EventSessionModel {
    String activity_id;
    String session_name;
    String activity_name;
    String begin_date;
    String end_date;

    public EventSessionModel(String activity_id, String session_name, String activity_name, String begin_date, String end_date) {
        this.activity_id = activity_id;
        this.session_name = session_name;
        this.activity_name = activity_name;
        this.begin_date = begin_date;
        this.end_date = end_date;
    }

    public String getActivity_id() {
        return activity_id;
    }

    public void setActivity_id(String activity_id) {
        this.activity_id = activity_id;
    }

    public String getSession_name() {
        return session_name;
    }

    public void setSession_name(String session_name) {
        this.session_name = session_name;
    }

    public String getActivity_name() {
        return activity_name;
    }

    public void setActivity_name(String activity_name) {
        this.activity_name = activity_name;
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
}
