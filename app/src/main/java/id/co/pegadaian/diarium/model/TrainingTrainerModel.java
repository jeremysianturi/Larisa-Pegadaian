package id.co.pegadaian.diarium.model;

public class TrainingTrainerModel {
    String BEGDA;
    String ENDDA;
    String BUSCD;
    String PERNR;
    String TRAID;
    String TRNAM;
    String STTAR;
    String BUSC1;
    String CHGDT;
    String CHUSR;
    String trainer_name;
    String schedule_id;
    String schedule_name;
    String session_id;
    String topic;
    String begin_time;
    String end_time;
    String day_number;
    String batch_name;
    String batch;
    String session_name;
    String event_id;
    String event_name;
    String event_status;
    String situation_code;
    String situation_name;
    String event_stat_id;

    public TrainingTrainerModel(String BEGDA, String ENDDA, String BUSCD, String PERNR, String TRAID, String TRNAM, String STTAR, String BUSC1, String CHGDT, String CHUSR, String trainer_name, String schedule_id, String schedule_name, String session_id, String topic, String begin_time, String end_time, String day_number, String batch_name, String batch, String session_name, String event_id, String event_name, String event_status, String situation_code, String situation_name, String event_stat_id) {
        this.BEGDA = BEGDA;
        this.ENDDA = ENDDA;
        this.BUSCD = BUSCD;
        this.PERNR = PERNR;
        this.TRAID = TRAID;
        this.TRNAM = TRNAM;
        this.STTAR = STTAR;
        this.BUSC1 = BUSC1;
        this.CHGDT = CHGDT;
        this.CHUSR = CHUSR;
        this.trainer_name = trainer_name;
        this.schedule_id = schedule_id;
        this.schedule_name = schedule_name;
        this.session_id = session_id;
        this.topic = topic;
        this.begin_time = begin_time;
        this.end_time = end_time;
        this.day_number = day_number;
        this.batch_name = batch_name;
        this.batch = batch;
        this.session_name = session_name;
        this.event_id = event_id;
        this.event_name = event_name;
        this.event_status = event_status;
        this.situation_code = situation_code;
        this.situation_name = situation_name;
        this.event_stat_id = event_stat_id;
    }

    public String getBEGDA() {
        return BEGDA;
    }

    public void setBEGDA(String BEGDA) {
        this.BEGDA = BEGDA;
    }

    public String getENDDA() {
        return ENDDA;
    }

    public void setENDDA(String ENDDA) {
        this.ENDDA = ENDDA;
    }

    public String getBUSCD() {
        return BUSCD;
    }

    public void setBUSCD(String BUSCD) {
        this.BUSCD = BUSCD;
    }

    public String getPERNR() {
        return PERNR;
    }

    public void setPERNR(String PERNR) {
        this.PERNR = PERNR;
    }

    public String getTRAID() {
        return TRAID;
    }

    public void setTRAID(String TRAID) {
        this.TRAID = TRAID;
    }

    public String getTRNAM() {
        return TRNAM;
    }

    public void setTRNAM(String TRNAM) {
        this.TRNAM = TRNAM;
    }

    public String getSTTAR() {
        return STTAR;
    }

    public void setSTTAR(String STTAR) {
        this.STTAR = STTAR;
    }

    public String getBUSC1() {
        return BUSC1;
    }

    public void setBUSC1(String BUSC1) {
        this.BUSC1 = BUSC1;
    }

    public String getCHGDT() {
        return CHGDT;
    }

    public void setCHGDT(String CHGDT) {
        this.CHGDT = CHGDT;
    }

    public String getCHUSR() {
        return CHUSR;
    }

    public void setCHUSR(String CHUSR) {
        this.CHUSR = CHUSR;
    }

    public String getTrainer_name() {
        return trainer_name;
    }

    public void setTrainer_name(String trainer_name) {
        this.trainer_name = trainer_name;
    }

    public String getSchedule_id() {
        return schedule_id;
    }

    public void setSchedule_id(String schedule_id) {
        this.schedule_id = schedule_id;
    }

    public String getSchedule_name() {
        return schedule_name;
    }

    public void setSchedule_name(String schedule_name) {
        this.schedule_name = schedule_name;
    }

    public String getSession_id() {
        return session_id;
    }

    public void setSession_id(String session_id) {
        this.session_id = session_id;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
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

    public String getDay_number() {
        return day_number;
    }

    public void setDay_number(String day_number) {
        this.day_number = day_number;
    }

    public String getBatch_name() {
        return batch_name;
    }

    public void setBatch_name(String batch_name) {
        this.batch_name = batch_name;
    }

    public String getBatch() {
        return batch;
    }

    public void setBatch(String batch) {
        this.batch = batch;
    }

    public String getSession_name() {
        return session_name;
    }

    public void setSession_name(String session_name) {
        this.session_name = session_name;
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

    public String getEvent_status() {
        return event_status;
    }

    public void setEvent_status(String event_status) {
        this.event_status = event_status;
    }

    public String getSituation_code() {
        return situation_code;
    }

    public void setSituation_code(String situation_code) {
        this.situation_code = situation_code;
    }

    public String getSituation_name() {
        return situation_name;
    }

    public void setSituation_name(String situation_name) {
        this.situation_name = situation_name;
    }

    public String getEvent_stat_id() {
        return event_stat_id;
    }

    public void setEvent_stat_id(String event_stat_id) {
        this.event_stat_id = event_stat_id;
    }
}
