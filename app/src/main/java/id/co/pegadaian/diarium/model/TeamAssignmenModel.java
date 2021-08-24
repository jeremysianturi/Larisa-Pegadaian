package id.co.pegadaian.diarium.model;

public class TeamAssignmenModel {
    String personal_number;
    String name;
    String start_date;
    String end_date;
    String unit;
    String position;
    String relation;
    String status;
    private boolean checked=false;
    String profile;

    public TeamAssignmenModel(String personal_number, String name, String start_date, String end_date, String unit, String position, String relation, String status, boolean checked, String profile) {
        this.personal_number = personal_number;
        this.name = name;
        this.start_date = start_date;
        this.end_date = end_date;
        this.unit = unit;
        this.position = position;
        this.relation = relation;
        this.status = status;
        this.checked = checked;
        this.profile = profile;
    }

    public String getPersonal_number() {
        return personal_number;
    }

    public void setPersonal_number(String personal_number) {
        this.personal_number = personal_number;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStart_date() {
        return start_date;
    }

    public void setStart_date(String start_date) {
        this.start_date = start_date;
    }

    public String getEnd_date() {
        return end_date;
    }

    public void setEnd_date(String end_date) {
        this.end_date = end_date;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getRelation() {
        return relation;
    }

    public void setRelation(String relation) {
        this.relation = relation;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }
}
