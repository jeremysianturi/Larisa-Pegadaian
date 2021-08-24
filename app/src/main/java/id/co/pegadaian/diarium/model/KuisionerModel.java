package id.co.pegadaian.diarium.model;

public class KuisionerModel {
    String begin_date;
    String end_date;
    String business_code;
    String personal_number;
    String pertanyaan;
    String type;

    public KuisionerModel(String begin_date, String end_date, String business_code, String personal_number, String pertanyaan, String type) {
        this.begin_date = begin_date;
        this.end_date = end_date;
        this.business_code = business_code;
        this.personal_number = personal_number;
        this.pertanyaan = pertanyaan;
        this.type = type;
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

    public String getPertanyaan() {
        return pertanyaan;
    }

    public void setPertanyaan(String pertanyaan) {
        this.pertanyaan = pertanyaan;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
