package id.co.pegadaian.diarium.model;

public class ManajementCPModel {
  private String personnel_number;
  private String complete_name ;
  private String organization_code ;
  private String organization_name ;
  private String job ;
  private String job_name ;
  private String position_code ;
  private String position_name ;

  public ManajementCPModel(String personnel_number, String complete_name, String organization_code, String organization_name, String job, String job_name, String position_code, String position_name) {
    this.personnel_number = personnel_number;
    this.complete_name = complete_name;
    this.organization_code = organization_code;
    this.organization_name = organization_name;
    this.job = job;
    this.job_name = job_name;
    this.position_code = position_code;
    this.position_name = position_name;
  }

  public String getPersonnel_number() {
    return personnel_number;
  }

  public void setPersonnel_number(String personnel_number) {
    this.personnel_number = personnel_number;
  }

  public String getComplete_name() {
    return complete_name;
  }

  public void setComplete_name(String complete_name) {
    this.complete_name = complete_name;
  }

  public String getOrganization_code() {
    return organization_code;
  }

  public void setOrganization_code(String organization_code) {
    this.organization_code = organization_code;
  }

  public String getOrganization_name() {
    return organization_name;
  }

  public void setOrganization_name(String organization_name) {
    this.organization_name = organization_name;
  }

  public String getJob() {
    return job;
  }

  public void setJob(String job) {
    this.job = job;
  }

  public String getJob_name() {
    return job_name;
  }

  public void setJob_name(String job_name) {
    this.job_name = job_name;
  }

  public String getPosition_code() {
    return position_code;
  }

  public void setPosition_code(String position_code) {
    this.position_code = position_code;
  }

  public String getPosition_name() {
    return position_name;
  }

  public void setPosition_name(String position_name) {
    this.position_name = position_name;
  }
}
