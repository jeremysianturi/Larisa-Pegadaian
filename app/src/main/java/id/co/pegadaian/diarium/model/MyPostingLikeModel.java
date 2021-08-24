package id.co.pegadaian.diarium.model;

public class MyPostingLikeModel {
    String posting_id;
    String personal_number;
    boolean status_like;

    public MyPostingLikeModel(String posting_id, String personal_number, boolean status_like) {
        this.posting_id = posting_id;
        this.personal_number = personal_number;
        this.status_like = status_like;
    }

    public String getPosting_id() {
        return posting_id;
    }

    public void setPosting_id(String posting_id) {
        this.posting_id = posting_id;
    }

    public String getPersonal_number() {
        return personal_number;
    }

    public void setPersonal_number(String personal_number) {
        this.personal_number = personal_number;
    }

    public boolean isStatus_like() {
        return status_like;
    }

    public void setStatus_like(boolean status_like) {
        this.status_like = status_like;
    }
}
