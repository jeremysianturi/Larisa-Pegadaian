package id.co.pegadaian.diarium.model;

public class EmployeeCornerThemeModel {
    String begin_date;
    String end_date;
    String business_code;
    String theme_id;
    String theme_name;
    String image;
    String change_date;
    String change_user;

    public EmployeeCornerThemeModel(String begin_date, String end_date, String business_code, String theme_id, String theme_name, String image, String change_date, String change_user) {
        this.begin_date = begin_date;
        this.end_date = end_date;
        this.business_code = business_code;
        this.theme_id = theme_id;
        this.theme_name = theme_name;
        this.image = image;
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

    public String getTheme_id() {
        return theme_id;
    }

    public void setTheme_id(String theme_id) {
        this.theme_id = theme_id;
    }

    public String getTheme_name() {
        return theme_name;
    }

    public void setTheme_name(String theme_name) {
        this.theme_name = theme_name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
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
}
