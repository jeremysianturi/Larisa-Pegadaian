package id.co.pegadaian.diarium.model;

public class IsiCoaModel {
    String f_item_name;
    String f_description;
    String f_produksi_code;
    String f_manufacture_date;
    String f_expired_date;

    public IsiCoaModel(String f_item_name, String f_description, String f_produksi_code, String f_manufacture_date, String f_expired_date) {
        this.f_item_name = f_item_name;
        this.f_description = f_description;
        this.f_produksi_code = f_produksi_code;
        this.f_manufacture_date = f_manufacture_date;
        this.f_expired_date = f_expired_date;
    }

    public String getF_item_name() {
        return f_item_name;
    }

    public void setF_item_name(String f_item_name) {
        this.f_item_name = f_item_name;
    }

    public String getF_description() {
        return f_description;
    }

    public void setF_description(String f_description) {
        this.f_description = f_description;
    }

    public String getF_produksi_code() {
        return f_produksi_code;
    }

    public void setF_produksi_code(String f_produksi_code) {
        this.f_produksi_code = f_produksi_code;
    }

    public String getF_manufacture_date() {
        return f_manufacture_date;
    }

    public void setF_manufacture_date(String f_manufacture_date) {
        this.f_manufacture_date = f_manufacture_date;
    }

    public String getF_expired_date() {
        return f_expired_date;
    }

    public void setF_expired_date(String f_expired_date) {
        this.f_expired_date = f_expired_date;
    }
}
