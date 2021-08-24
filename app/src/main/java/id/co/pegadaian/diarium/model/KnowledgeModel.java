package id.co.pegadaian.diarium.model;

public class KnowledgeModel {
    String begin_date;
    String end_date;
    String materi_id;
    String materi_name;
    String description;
    String selling_price;
    String purchase_price;
    String change_date;
    String change_user;
    String business_code;
    String company_name;
    String address;

    public KnowledgeModel(String begin_date, String end_date, String materi_id, String materi_name, String description, String selling_price, String purchase_price, String change_date, String change_user, String business_code, String company_name, String address) {
        this.begin_date = begin_date;
        this.end_date = end_date;
        this.materi_id = materi_id;
        this.materi_name = materi_name;
        this.description = description;
        this.selling_price = selling_price;
        this.purchase_price = purchase_price;
        this.change_date = change_date;
        this.change_user = change_user;
        this.business_code = business_code;
        this.company_name = company_name;
        this.address = address;
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

    public String getMateri_id() {
        return materi_id;
    }

    public void setMateri_id(String materi_id) {
        this.materi_id = materi_id;
    }

    public String getMateri_name() {
        return materi_name;
    }

    public void setMateri_name(String materi_name) {
        this.materi_name = materi_name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSelling_price() {
        return selling_price;
    }

    public void setSelling_price(String selling_price) {
        this.selling_price = selling_price;
    }

    public String getPurchase_price() {
        return purchase_price;
    }

    public void setPurchase_price(String purchase_price) {
        this.purchase_price = purchase_price;
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

    public String getBusiness_code() {
        return business_code;
    }

    public void setBusiness_code(String business_code) {
        this.business_code = business_code;
    }

    public String getCompany_name() {
        return company_name;
    }

    public void setCompany_name(String company_name) {
        this.company_name = company_name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
