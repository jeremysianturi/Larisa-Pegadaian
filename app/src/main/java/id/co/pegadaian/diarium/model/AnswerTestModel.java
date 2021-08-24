package id.co.pegadaian.diarium.model;

public class AnswerTestModel {
  String answer_id;
  String answer_text;
  boolean selected;
  String answer_squance;

  public AnswerTestModel(String answer_id, String answer_text, boolean selected, String answer_squance) {
    this.answer_id = answer_id;
    this.answer_text = answer_text;
    this.selected = selected;
    this.answer_squance = answer_squance;
  }

  public String getAnswer_id() {
    return answer_id;
  }

  public void setAnswer_id(String answer_id) {
    this.answer_id = answer_id;
  }

  public String getAnswer_text() {
    return answer_text;
  }

  public void setAnswer_text(String answer_text) {
    this.answer_text = answer_text;
  }

  public boolean isSelected() {
    return selected;
  }

  public void setSelected(boolean selected) {
    this.selected = selected;
  }

  public String getAnswer_squance() {
    return answer_squance;
  }

  public void setAnswer_squance(String answer_squance) {
    this.answer_squance = answer_squance;
  }
}
