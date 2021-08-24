package id.co.pegadaian.diarium.model;

public class SubKategoriModel {
    String begin_date;
    String end_date;
    String business_code;
    String plan_version;
    String otype_code;
    String object_code;
    String object_name;
    String change_date;
    String change_user;

    public SubKategoriModel(String begin_date, String end_date, String business_code, String plan_version, String otype_code, String object_code, String object_name, String change_date, String change_user) {
        this.begin_date = begin_date;
        this.end_date = end_date;
        this.business_code = business_code;
        this.plan_version = plan_version;
        this.otype_code = otype_code;
        this.object_code = object_code;
        this.object_name = object_name;
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

    public String getPlan_version() {
        return plan_version;
    }

    public void setPlan_version(String plan_version) {
        this.plan_version = plan_version;
    }

    public String getOtype_code() {
        return otype_code;
    }

    public void setOtype_code(String otype_code) {
        this.otype_code = otype_code;
    }

    public String getObject_code() {
        return object_code;
    }

    public void setObject_code(String object_code) {
        this.object_code = object_code;
    }

    public String getObject_name() {
        return object_name;
    }

    public void setObject_name(String object_name) {
        this.object_name = object_name;
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
    @Override
    public String toString() {
        return object_name;
    }
}
