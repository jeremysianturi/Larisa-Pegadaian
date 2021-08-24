package id.co.pegadaian.diarium.model;

public class TestLMSModel {
  String quesioner_id;
  String quesioner_text;
  String quesioner_title;
  String quesioner_type_value;
  String quesioner_image;

  public TestLMSModel(String quesioner_id, String quesioner_text, String quesioner_title, String quesioner_type_value, String getQuesioner_image) {
    this.quesioner_id = quesioner_id;
    this.quesioner_text = quesioner_text;
    this.quesioner_title = quesioner_title;
    this.quesioner_type_value = quesioner_type_value;
    this.quesioner_image = getQuesioner_image;
  }

  public String getQuesioner_id() {
    return quesioner_id;
  }

  public void setQuesioner_id(String quesioner_id) {
    this.quesioner_id = quesioner_id;
  }

  public String getQuesioner_text() {
    return quesioner_text;
  }

  public void setQuesioner_text(String quesioner_text) {
    this.quesioner_text = quesioner_text;
  }

  public String getQuesioner_title() {
    return quesioner_title;
  }

  public void setQuesioner_title(String quesioner_title) {
    this.quesioner_title = quesioner_title;
  }

  public String getQuesioner_type_value() {
    return quesioner_type_value;
  }

  public void setQuesioner_type_value(String quesioner_type_value) {
    this.quesioner_type_value = quesioner_type_value;
  }

  public String getGetQuesioner_image() {
    return quesioner_image;
  }

  public void setGetQuesioner_image(String getQuesioner_image) {
    this.quesioner_image = getQuesioner_image;
  }
}
