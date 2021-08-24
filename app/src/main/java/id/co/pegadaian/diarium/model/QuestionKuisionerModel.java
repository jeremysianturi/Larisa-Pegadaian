package id.co.pegadaian.diarium.model;

public class QuestionKuisionerModel {
    String question_id;
    String question_text;

    public QuestionKuisionerModel(String question_id, String question_text) {
        this.question_id = question_id;
        this.question_text = question_text;
    }

    public String getQuestion_id() {
        return question_id;
    }

    public void setQuestion_id(String question_id) {
        this.question_id = question_id;
    }

    public String getQuestion_text() {
        return question_text;
    }

    public void setQuestion_text(String question_text) {
        this.question_text = question_text;
    }
}
