package id.co.pegadaian.diarium.model;

public class TrendingModel {

  private String
          course_id,
          course_name,
          course_description,
          partner_id,
          partner_name,
          partner_code,
          count_click,
          image_course,
          image_partner
  ;

  public TrendingModel(String course_id, String course_name, String course_description, String partner_id, String partner_name, String partner_code, String count_click, String image_course, String image_partner) {
    this.course_id = course_id;
    this.course_name = course_name;
    this.course_description = course_description;
    this.partner_id = partner_id;
    this.partner_name = partner_name;
    this.partner_code = partner_code;
    this.count_click = count_click;
    this.image_course = image_course;
    this.image_partner = image_partner;
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

  public String getPartner_id() {
    return partner_id;
  }

  public void setPartner_id(String partner_id) {
    this.partner_id = partner_id;
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

  public String getCount_click() {
    return count_click;
  }

  public void setCount_click(String count_click) {
    this.count_click = count_click;
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
