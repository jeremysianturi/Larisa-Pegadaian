package id.co.pegadaian.diarium.model;

public class MyCourseModel {

  private String course_name, course_code, price, point, partner_name, partner_code, img_url;

  public MyCourseModel(String course_name, String course_code, String price, String point, String partner_name, String partner_code, String img_url) {
    this.course_name = course_name;
    this.course_code = course_code;
    this.price = price;
    this.point = point;
    this.partner_name = partner_name;
    this.partner_code = partner_code;
    this.img_url = img_url;
  }

  public String getCourse_name() {
    return course_name;
  }

  public void setCourse_name(String course_name) {
    this.course_name = course_name;
  }

  public String getCourse_code() {
    return course_code;
  }

  public void setCourse_code(String course_code) {
    this.course_code = course_code;
  }

  public String getPrice() {
    return price;
  }

  public void setPrice(String price) {
    this.price = price;
  }

  public String getPoint() {
    return point;
  }

  public void setPoint(String point) {
    this.point = point;
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

  public String getImg_url() {
    return img_url;
  }

  public void setImg_url(String img_url) {
    this.img_url = img_url;
  }
}
