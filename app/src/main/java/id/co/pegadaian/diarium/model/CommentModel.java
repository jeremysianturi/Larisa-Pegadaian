package id.co.pegadaian.diarium.model;

public class CommentModel {
    String name;
    String avatar;
    String comment;
    String date;
    String time;

    public CommentModel(String name, String avatar, String comment, String date, String time) {
        this.name = name;
        this.avatar = avatar;
        this.comment = comment;
        this.date = date;
        this.time = time;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
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

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
