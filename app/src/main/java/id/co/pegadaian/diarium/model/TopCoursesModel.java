package id.co.pegadaian.diarium.model;

public class TopCoursesModel {

  private String bussines_code;
  private String course_id;
  private String course_name;
  private String course_description;
  private String partner_name;
  private String partner_code;
  private String partner_id;
  private String count_buy;
  private String image_course;
  private String image_partner;

  public TopCoursesModel(String bussines_code, String course_id, String course_name, String course_description, String partner_name, String partner_code, String partner_id, String count_buy, String image_course, String image_partner) {
    this.bussines_code = bussines_code;
    this.course_id = course_id;
    this.course_name = course_name;
    this.course_description = course_description;
    this.partner_name = partner_name;
    this.partner_code = partner_code;
    this.partner_id = partner_id;
    this.count_buy = count_buy;
    this.image_course = image_course;
    this.image_partner = image_partner;
  }

  public String getBussines_code() {
    return bussines_code;
  }

  public void setBussines_code(String bussines_code) {
    this.bussines_code = bussines_code;
  }

  public String getCourse_id() {
    return course_id;
  }

  public void setCourse_id(String course_id) {
    this.course_id = course_id;
  }

  public String getCourse_name() {
    return course_name;
  }

  public void setCourse_name(String course_name) {
    this.course_name = course_name;
  }

  public String getCourse_description() {
    return course_description;
  }

  public void setCourse_description(String course_description) {
    this.course_description = course_description;
  }

  public String getPartner_name() {
    return partner_name;
  }

  public void setPartner_name(String partner_name) {
    this.partner_name = partner_name;
  }

  public String getPartner_code() {
    return partner_code;
  }

  public void setPartner_code(String partner_code) {
    this.partner_code = partner_code;
  }

  public String getPartner_id() {
    return partner_id;
  }

  public void setPartner_id(String partner_id) {
    this.partner_id = partner_id;
  }

  public String getCount_buy() {
    return count_buy;
  }

  public void setCount_buy(String count_buy) {
    this.count_buy = count_buy;
  }

  public String getImage_course() {
    return image_course;
  }

  public void setImage_course(String image_course) {
    this.image_course = image_course;
  }

  public String getImage_partner() {
    return image_partner;
  }

  public void setImage_partner(String image_partner) {
    this.image_partner = image_partner;
  }
}


