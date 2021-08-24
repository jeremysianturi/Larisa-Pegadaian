package id.co.pegadaian.diarium.model;

public class ForumModel {
    String owner;
    String change_date;
    String forum_image;
    String forum_title;
    String batch_name;

    public ForumModel(String owner, String change_date, String forum_image, String forum_title, String batch_name) {
        this.owner = owner;
        this.change_date = change_date;
        this.forum_image = forum_image;
        this.forum_title = forum_title;
        this.batch_name = batch_name;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getChange_date() {
        return change_date;
    }

    public void setChange_date(String change_date) {
        this.change_date = change_date;
    }

    public String getForum_image() {
        return forum_image;
    }

    public void setForum_image(String forum_image) {
        this.forum_image = forum_image;
    }

    public String getForum_title() {
        return forum_title;
    }

    public void setForum_title(String forum_title) {
        this.forum_title = forum_title;
    }

    public String getBatch_name() {
        return batch_name;
    }

    public void setBatch_name(String batch_name) {
        this.batch_name = batch_name;
    }
}
