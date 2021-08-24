package id.co.pegadaian.diarium.model;

public class AnswerMultipleChoiceModel {
    String answer_id;
    String question_id;
    String answer;

    public AnswerMultipleChoiceModel(String answer_id, String question_id, String answer) {
        this.answer_id = answer_id;
        this.question_id = question_id;
        this.answer = answer;
    }

    public String getAnswer_id() {
        return answer_id;
    }

    public void setAnswer_id(String answer_id) {
        this.answer_id = answer_id;
    }

    public String getQuestion_id() {
        return question_id;
    }

    public void setQuestion_id(String question_id) {
        this.question_id = question_id;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }
}
