package id.co.pegadaian.diarium.model;

public class FeedbackModel {
    private float ratingStar;
    private String feedback_id;
    private String feedback_type;
    private String feedback_question;
    private String event_id;
    private String created_date;

    public FeedbackModel(String feedback_id, String feedback_type, String feedback_question, String event_id, String created_date) {
        this.feedback_id = feedback_id;
        this.feedback_type = feedback_type;
        this.feedback_question = feedback_question;
        this.event_id = event_id;
        this.created_date = created_date;
    }

    public float getRatingStar() {
        return ratingStar;
    }

    public void setRatingStar(float ratingStar) {
        this.ratingStar = ratingStar;
    }

    public String getFeedback_id() {
        return feedback_id;
    }

    public void setFeedback_id(String feedback_id) {
        this.feedback_id = feedback_id;
    }

    public String getFeedback_type() {
        return feedback_type;
    }

    public void setFeedback_type(String feedback_type) {
        this.feedback_type = feedback_type;
    }

    public String getFeedback_question() {
        return feedback_question;
    }

    public void setFeedback_question(String feedback_question) {
        this.feedback_question = feedback_question;
    }

    public String getEvent_id() {
        return event_id;
    }

    public void setEvent_id(String event_id) {
        this.event_id = event_id;
    }

    public String getCreated_date() {
        return created_date;
    }

    public void setCreated_date(String created_date) {
        this.created_date = created_date;
    }
}
