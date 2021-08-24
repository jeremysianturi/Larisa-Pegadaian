package id.co.pegadaian.diarium.model;

public class DetailCornerModel {
    String begin_date;
    String end_date;
    String business_code;
    String empcorner_id;
    String batch_name;
    String kuota;
    String start_batch;
    String end_batch;
    String change_date;
    String change_user;
    String batch_id;
    String empcorner_date;
    String kuota_available;

    public DetailCornerModel(String begin_date, String end_date, String business_code, String empcorner_id, String batch_name, String kuota, String start_batch, String end_batch, String change_date, String change_user, String batch_id, String empcorner_date, String kuota_available) {
        this.begin_date = begin_date;
        this.end_date = end_date;
        this.business_code = business_code;
        this.empcorner_id = empcorner_id;
        this.batch_name = batch_name;
        this.kuota = kuota;
        this.start_batch = start_batch;
        this.end_batch = end_batch;
        this.change_date = change_date;
        this.change_user = change_user;
        this.batch_id = batch_id;
        this.empcorner_date = empcorner_date;
        this.kuota_available = kuota_available;
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

    public String getEmpcorner_id() {
        return empcorner_id;
    }

    public void setEmpcorner_id(String empcorner_id) {
        this.empcorner_id = empcorner_id;
    }

    public String getBatch_name() {
        return batch_name;
    }

    public void setBatch_name(String batch_name) {
        this.batch_name = batch_name;
    }

    public String getKuota() {
        return kuota;
    }

    public void setKuota(String kuota) {
        this.kuota = kuota;
    }

    public String getStart_batch() {
        return start_batch;
    }

    public void setStart_batch(String start_batch) {
        this.start_batch = start_batch;
    }

    public String getEnd_batch() {
        return end_batch;
    }

    public void setEnd_batch(String end_batch) {
        this.end_batch = end_batch;
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

    public String getBatch_id() {
        return batch_id;
    }

    public void setBatch_id(String batch_id) {
        this.batch_id = batch_id;
    }

    public String getEmpcorner_date() {
        return empcorner_date;
    }

    public void setEmpcorner_date(String empcorner_date) {
        this.empcorner_date = empcorner_date;
    }

    public String getKuota_available() {
        return kuota_available;
    }

    public void setKuota_available(String kuota_available) {
        this.kuota_available = kuota_available;
    }
}
