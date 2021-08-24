package id.co.pegadaian.diarium.model;

public class NotifModel {
    String title;
    String description;
    String change_date;

    public NotifModel(String title, String description, String change_date) {
        this.title = title;
        this.description = description;
        this.change_date = change_date;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getChange_date() {
        return change_date;
    }

    public void setChange_date(String change_date) {
        this.change_date = change_date;
    }
}
