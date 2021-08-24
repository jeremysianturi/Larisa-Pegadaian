package id.co.pegadaian.diarium.model;

public class ThemeModel {
    String theme_id;
    String theme_name;

    public ThemeModel(String theme_id, String theme_name) {
        this.theme_id = theme_id;
        this.theme_name = theme_name;
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
}
