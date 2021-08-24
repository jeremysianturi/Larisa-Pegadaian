package id.co.pegadaian.diarium.model;

public class TrainingModel {
    String  bussines_code;
    String  batch;
    String  batch_name;
    String  event_id;
    String  event_name;
    String  begin_date;
    String  end_date;
    String  event_curr_stat;
    String  evnt_curr_statid;
    String  event_status;
    String  event_stat_id;
    String  location_id;
    String  location;
    String  cur_id;
    String  curriculum;
    String  event_type;
    String  participant_id;
    String  partcipant_name;
    String  parti_nicknm;
    String  company_name;

    public TrainingModel(String bussines_code, String batch, String batch_name, String event_id, String event_name, String begin_date, String end_date, String event_curr_stat, String evnt_curr_statid, String event_status, String event_stat_id, String location_id, String location, String cur_id, String curriculum, String event_type, String participant_id, String partcipant_name, String parti_nicknm, String company_name) {
        this.bussines_code = bussines_code;
        this.batch = batch;
        this.batch_name = batch_name;
        this.event_id = event_id;
        this.event_name = event_name;
        this.begin_date = begin_date;
        this.end_date = end_date;
        this.event_curr_stat = event_curr_stat;
        this.evnt_curr_statid = evnt_curr_statid;
        this.event_status = event_status;
        this.event_stat_id = event_stat_id;
        this.location_id = location_id;
        this.location = location;
        this.cur_id = cur_id;
        this.curriculum = curriculum;
        this.event_type = event_type;
        this.participant_id = participant_id;
        this.partcipant_name = partcipant_name;
        this.parti_nicknm = parti_nicknm;
        this.company_name = company_name;
    }

    public String getBussines_code() {
        return bussines_code;
    }

    public void setBussines_code(String bussines_code) {
        this.bussines_code = bussines_code;
    }

    public String getBatch() {
        return batch;
    }

    public void setBatch(String batch) {
        this.batch = batch;
    }

    public String getBatch_name() {
        return batch_name;
    }

    public void setBatch_name(String batch_name) {
        this.batch_name = batch_name;
    }

    public String getEvent_id() {
        return event_id;
    }

    public void setEvent_id(String event_id) {
        this.event_id = event_id;
    }

    public String getEvent_name() {
        return event_name;
    }

    public void setEvent_name(String event_name) {
        this.event_name = event_name;
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

    public String getEvent_curr_stat() {
        return event_curr_stat;
    }

    public void setEvent_curr_stat(String event_curr_stat) {
        this.event_curr_stat = event_curr_stat;
    }

    public String getEvnt_curr_statid() {
        return evnt_curr_statid;
    }

    public void setEvnt_curr_statid(String evnt_curr_statid) {
        this.evnt_curr_statid = evnt_curr_statid;
    }

    public String getEvent_status() {
        return event_status;
    }

    public void setEvent_status(String event_status) {
        this.event_status = event_status;
    }

    public String getEvent_stat_id() {
        return event_stat_id;
    }

    public void setEvent_stat_id(String event_stat_id) {
        this.event_stat_id = event_stat_id;
    }

    public String getLocation_id() {
        return location_id;
    }

    public void setLocation_id(String location_id) {
        this.location_id = location_id;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getCur_id() {
        return cur_id;
    }

    public void setCur_id(String cur_id) {
        this.cur_id = cur_id;
    }

    public String getCurriculum() {
        return curriculum;
    }

    public void setCurriculum(String curriculum) {
        this.curriculum = curriculum;
    }

    public String getEvent_type() {
        return event_type;
    }

    public void setEvent_type(String event_type) {
        this.event_type = event_type;
    }

    public String getParticipant_id() {
        return participant_id;
    }

    public void setParticipant_id(String participant_id) {
        this.participant_id = participant_id;
    }

    public String getPartcipant_name() {
        return partcipant_name;
    }

    public void setPartcipant_name(String partcipant_name) {
        this.partcipant_name = partcipant_name;
    }

    public String getParti_nicknm() {
        return parti_nicknm;
    }

    public void setParti_nicknm(String parti_nicknm) {
        this.parti_nicknm = parti_nicknm;
    }

    public String getCompany_name() {
        return company_name;
    }

    public void setCompany_name(String company_name) {
        this.company_name = company_name;
    }
}
