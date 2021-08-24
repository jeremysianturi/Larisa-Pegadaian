package id.co.pegadaian.diarium.model;

public class ReviewActivityModel {

  private String name , comment, date, url;

  public ReviewActivityModel(String name, String comment, String date, String url) {
    this.name = name;
    this.comment = comment;
    this.date = date;
    this.url = url;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getComment() {
    return comment;
  }

  public void setComment(String comment) {
    this.comment = comment;
  }

  public String getDate() {
    return date;
  }

  public void setDate(String date) {
    this.date = date;
  }

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }
}
