package id.co.pegadaian.diarium.model;

public class ForumCommentModel {
    String owner;
    String begin_date;
    String time;
    String comment;

    public ForumCommentModel(String owner, String begin_date, String time, String comment) {
        this.owner = owner;
        this.begin_date = begin_date;
        this.time = time;
        this.comment = comment;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getBegin_date() {
        return begin_date;
    }

    public void setBegin_date(String begin_date) {
        this.begin_date = begin_date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
