package id.co.pegadaian.diarium.model;

public class EmployeeCareModel {
       String  begin_date;
       String  end_date;
       String  business_code;
       String  personal_number;
       String  ticket_number;
       String  problem_type;
       String  problem_desc;
       String  image;
       String  problem_status;
       String  change_date;
       String  change_user;
       String  name;

    public EmployeeCareModel(String begin_date, String end_date, String business_code, String personal_number, String ticket_number, String problem_type, String problem_desc, String image, String problem_status, String change_date, String change_user, String name) {
        this.begin_date = begin_date;
        this.end_date = end_date;
        this.business_code = business_code;
        this.personal_number = personal_number;
        this.ticket_number = ticket_number;
        this.problem_type = problem_type;
        this.problem_desc = problem_desc;
        this.image = image;
        this.problem_status = problem_status;
        this.change_date = change_date;
        this.change_user = change_user;
        this.name = name;
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

    public String getTicket_number() {
        return ticket_number;
    }

    public void setTicket_number(String ticket_number) {
        this.ticket_number = ticket_number;
    }

    public String getProblem_type() {
        return problem_type;
    }

    public void setProblem_type(String problem_type) {
        this.problem_type = problem_type;
    }

    public String getProblem_desc() {
        return problem_desc;
    }

    public void setProblem_desc(String problem_desc) {
        this.problem_desc = problem_desc;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getProblem_status() {
        return problem_status;
    }

    public void setProblem_status(String problem_status) {
        this.problem_status = problem_status;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
