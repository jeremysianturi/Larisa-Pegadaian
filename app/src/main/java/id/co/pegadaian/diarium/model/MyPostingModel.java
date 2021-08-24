package id.co.pegadaian.diarium.model;

public class MyPostingModel {
    String begin_date;
    String end_date;
    String business_code;
    String change_date;
    String change_user;
    String date;
    String description;
    String posting_id;
    String title;
    String personal_number;
    String time;
    String updateAt;
    String image;
    String name;
    String profile;
    int jmlLoveLike;
    int isLiked;
    int jmlDislike;
    int jmlComment;

    public MyPostingModel(String begin_date, String end_date, String business_code, String change_date, String change_user, String date, String description, String posting_id, String title, String personal_number, String time, String updateAt, String image, String name, String profile, int jmlLoveLike, int isLiked, int jmlDislike, int jmlComment) {
        this.begin_date = begin_date;
        this.end_date = end_date;
        this.business_code = business_code;
        this.change_date = change_date;
        this.change_user = change_user;
        this.date = date;
        this.description = description;
        this.posting_id = posting_id;
        this.title = title;
        this.personal_number = personal_number;
        this.time = time;
        this.updateAt = updateAt;
        this.image = image;
        this.name = name;
        this.profile = profile;
        this.jmlLoveLike = jmlLoveLike;
        this.isLiked = isLiked;
        this.jmlDislike = jmlDislike;
        this.jmlComment = jmlComment;
    }

    public String getBegin_date() {
        return begin_date;
    }

    public void setBegin_date(String begin_date) {
        this.begin_date = begin_date;
    }

    public String getEnd_date() {
        return end_date;
    }

    public void setEnd_date(String end_date) {
        this.end_date = end_date;
    }

    public String getBusiness_code() {
        return business_code;
    }

    public void setBusiness_code(String business_code) {
        this.business_code = business_code;
    }

    public String getChange_date() {
        return change_date;
    }

    public void setChange_date(String change_date) {
        this.change_date = change_date;
    }

    public String getChange_user() {
        return change_user;
    }

    public void setChange_user(String change_user) {
        this.change_user = change_user;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPosting_id() {
        return posting_id;
    }

    public void setPosting_id(String posting_id) {
        this.posting_id = posting_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPersonal_number() {
        return personal_number;
    }

    public void setPersonal_number(String personal_number) {
        this.personal_number = personal_number;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getUpdateAt() {
        return updateAt;
    }

    public void setUpdateAt(String updateAt) {
        this.updateAt = updateAt;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public int getJmlLoveLike() {
        return jmlLoveLike;
    }

    public void setJmlLoveLike(int jmlLoveLike) {
        this.jmlLoveLike = jmlLoveLike;
    }

    public int getIsLiked() {
        return isLiked;
    }

    public void setIsLiked(int isLiked) {
        this.isLiked = isLiked;
    }

    public int getJmlDislike() {
        return jmlDislike;
    }

    public void setJmlDislike(int jmlDislike) {
        this.jmlDislike = jmlDislike;
    }

    public int getJmlComment() {
        return jmlComment;
    }

    public void setJmlComment(int jmlComment) {
        this.jmlComment = jmlComment;
    }
}
