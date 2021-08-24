package id.co.pegadaian.diarium.model;

public class ChartModel {

  private String
          tittle,
          creator,
          price,
          point,
          url,
          condition;

  private int
          object_identifier,
          partner_id,
          course_id;


  public ChartModel(String tittle, String creator, String price, String point, String url, String condition, int object_identifier, int partner_id, int course_id) {
    this.tittle = tittle;
    this.creator = creator;
    this.price = price;
    this.point = point;
    this.url = url;
    this.condition = condition;
    this.object_identifier = object_identifier;
    this.partner_id = partner_id;
    this.course_id = course_id;
  }

  public String getTittle() {
    return tittle;
  }

  public void setTittle(String tittle) {
    this.tittle = tittle;
  }

  public String getCreator() {
    return creator;
  }

  public void setCreator(String creator) {
    this.creator = creator;
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

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  public String getCondition() {
    return condition;
  }

  public void setCondition(String condition) {
    this.condition = condition;
  }

  public int getObject_identifier() {
    return object_identifier;
  }

  public void setObject_identifier(int object_identifier) {
    this.object_identifier = object_identifier;
  }

  public int getPartner_id() {
    return partner_id;
  }

  public void setPartner_id(int partner_id) {
    this.partner_id = partner_id;
  }

  public int getCourse_id() {
    return course_id;
  }

  public void setCourse_id(int course_id) {
    this.course_id = course_id;
  }
}
