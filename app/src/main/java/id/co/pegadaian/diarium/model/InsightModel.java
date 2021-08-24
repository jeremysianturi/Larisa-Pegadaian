package id.co.pegadaian.diarium.model;

public class InsightModel {
    String forum_id;
    String forum_title;
    String forum_text;
    String forum_image;
    String owner;
    String forum_time;

    public InsightModel(String forum_id, String forum_title, String forum_text, String forum_image, String owner, String forum_time) {
        this.forum_id = forum_id;
        this.forum_title = forum_title;
        this.forum_text = forum_text;
        this.forum_image = forum_image;
        this.owner = owner;
        this.forum_time = forum_time;
    }

    public String getForum_id() {
        return forum_id;
    }

    public void setForum_id(String forum_id) {
        this.forum_id = forum_id;
    }

    public String getForum_title() {
        return forum_title;
    }

    public void setForum_title(String forum_title) {
        this.forum_title = forum_title;
    }

    public String getForum_text() {
        return forum_text;
    }

    public void setForum_text(String forum_text) {
        this.forum_text = forum_text;
    }

    public String getForum_image() {
        return forum_image;
    }

    public void setForum_image(String forum_image) {
        this.forum_image = forum_image;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getForum_time() {
        return forum_time;
    }

    public void setForum_time(String forum_time) {
        this.forum_time = forum_time;
    }
}
