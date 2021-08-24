package id.co.pegadaian.diarium.model;

public class ScheduleModel {
    String schedule_id;
    String begin_date;
    String end_date;
    String begin_time;
    String end_time;
    String schedule_name;
    String topic;
    String day_num;

    public ScheduleModel(String schedule_id, String begin_date, String end_date, String begin_time, String end_time, String schedule_name, String topic, String day_num) {
        this.schedule_id = schedule_id;
        this.begin_date = begin_date;
        this.end_date = end_date;
        this.begin_time = begin_time;
        this.end_time = end_time;
        this.schedule_name = schedule_name;
        this.topic = topic;
        this.day_num = day_num;
    }

    public String getSchedule_id() {
        return schedule_id;
    }

    public void setSchedule_id(String schedule_id) {
        this.schedule_id = schedule_id;
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

    public String getBegin_time() {
        return begin_time;
    }

    public void setBegin_time(String begin_time) {
        this.begin_time = begin_time;
    }

    public String getEnd_time() {
        return end_time;
    }

    public void setEnd_time(String end_time) {
        this.end_time = end_time;
    }

    public String getSchedule_name() {
        return schedule_name;
    }

    public void setSchedule_name(String schedule_name) {
        this.schedule_name = schedule_name;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getDay_num() {
        return day_num;
    }

    public void setDay_num(String day_num) {
        this.day_num = day_num;
    }
}
