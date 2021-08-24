package id.co.pegadaian.diarium.model;

public class TrainerModel {
    String name;
    String status;
    String company;

    public TrainerModel(String name, String status, String company) {
        this.name = name;
        this.status = status;
        this.company = company;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }
}
