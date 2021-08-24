package id.co.pegadaian.diarium.model;

public class DetailCourseModel  {

  private String object_identifier;
  private String course_name;
  private String partner_name;
  private String course_description;
  private int course_id;
  private int partner_id;
  private String img_url;
  private int price_course;
  private int point_course;
  private int id_mapping;
  private String object_code;

  public DetailCourseModel(String object_identifier, String course_name, String partner_name, String course_description, int course_id, int partner_id, String img_url, int price_course, int point_course, int id_mapping, String object_code) {
    this.object_identifier = object_identifier;
    this.course_name = course_name;
    this.partner_name = partner_name;
    this.course_description = course_description;
    this.course_id = course_id;
    this.partner_id = partner_id;
    this.img_url = img_url;
    this.price_course = price_course;
    this.point_course = point_course;
    this.id_mapping = id_mapping;
    this.object_code = object_code;
  }

  public String getObject_identifier() {
    return object_identifier;
  }

  public void setObject_identifier(String object_identifier) {
    this.object_identifier = object_identifier;
  }

  public String getCourse_name() {
    return course_name;
  }

  public void setCourse_name(String course_name) {
    this.course_name = course_name;
  }

  public String getPartner_name() {
    return partner_name;
  }

  public void setPartner_name(String partner_name) {
    this.partner_name = partner_name;
  }

  public String getCourse_description() {
    return course_description;
  }

  public void setCourse_description(String course_description) {
    this.course_description = course_description;
  }

  public int getCourse_id() {
    return course_id;
  }

  public void setCourse_id(int course_id) {
    this.course_id = course_id;
  }

  public int getPartner_id() {
    return partner_id;
  }

  public void setPartner_id(int partner_id) {
    this.partner_id = partner_id;
  }

  public String getImg_url() {
    return img_url;
  }

  public void setImg_url(String img_url) {
    this.img_url = img_url;
  }

  public int getPrice_course() {
    return price_course;
  }

  public void setPrice_course(int price_course) {
    this.price_course = price_course;
  }

  public int getPoint_course() {
    return point_course;
  }

  public void setPoint_course(int point_course) {
    this.point_course = point_course;
  }

  public int getId_mapping() {
    return id_mapping;
  }

  public void setId_mapping(int id_mapping) {
    this.id_mapping = id_mapping;
  }

  public String getObject_code() {
    return object_code;
  }

  public void setObject_code(String object_code) {
    this.object_code = object_code;
  }
}
