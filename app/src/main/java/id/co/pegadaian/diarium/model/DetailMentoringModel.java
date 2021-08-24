package id.co.pegadaian.diarium.model;

public class DetailMentoringModel {
    String begin_date;
    String char_text;
    String sender_name;

    public DetailMentoringModel(String begin_date, String char_text, String sender_name) {
        this.begin_date = begin_date;
        this.char_text = char_text;
        this.sender_name = sender_name;
    }

    public String getBegin_date() {
        return begin_date;
    }

    public void setBegin_date(String begin_date) {
        this.begin_date = begin_date;
    }

    public String getChar_text() {
        return char_text;
    }

    public void setChar_text(String char_text) {
        this.char_text = char_text;
    }

    public String getSender_name() {
        return sender_name;
    }

    public void setSender_name(String sender_name) {
        this.sender_name = sender_name;
    }
}
