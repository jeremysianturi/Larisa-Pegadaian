package id.co.pegadaian.diarium.model;

public class QuestionSurveyModel {
    String begin_date;
    String end_date;
    String business_code;
    String survey_id;
    String survey_name;
    String survey_content;
    String startdate_survey;
    String enddate_survey;
    String change_date;
    String change_user;
    String personal_number;
    String question_id;
    String survey_type;
    String list_question;
    String pertanyaan_ke;

    public QuestionSurveyModel(String begin_date, String end_date, String business_code, String survey_id, String survey_name, String survey_content, String startdate_survey, String enddate_survey, String change_date, String change_user, String personal_number, String question_id, String survey_type, String list_question, String pertanyaan_ke) {
        this.begin_date = begin_date;
        this.end_date = end_date;
        this.business_code = business_code;
        this.survey_id = survey_id;
        this.survey_name = survey_name;
        this.survey_content = survey_content;
        this.startdate_survey = startdate_survey;
        this.enddate_survey = enddate_survey;
        this.change_date = change_date;
        this.change_user = change_user;
        this.personal_number = personal_number;
        this.question_id = question_id;
        this.survey_type = survey_type;
        this.list_question = list_question;
        this.pertanyaan_ke = pertanyaan_ke;
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

    public String getSurvey_id() {
        return survey_id;
    }

    public void setSurvey_id(String survey_id) {
        this.survey_id = survey_id;
    }

    public String getSurvey_name() {
        return survey_name;
    }

    public void setSurvey_name(String survey_name) {
        this.survey_name = survey_name;
    }

    public String getSurvey_content() {
        return survey_content;
    }

    public void setSurvey_content(String survey_content) {
        this.survey_content = survey_content;
    }

    public String getStartdate_survey() {
        return startdate_survey;
    }

    public void setStartdate_survey(String startdate_survey) {
        this.startdate_survey = startdate_survey;
    }

    public String getEnddate_survey() {
        return enddate_survey;
    }

    public void setEnddate_survey(String enddate_survey) {
        this.enddate_survey = enddate_survey;
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

    public String getPersonal_number() {
        return personal_number;
    }

    public void setPersonal_number(String personal_number) {
        this.personal_number = personal_number;
    }

    public String getQuestion_id() {
        return question_id;
    }

    public void setQuestion_id(String question_id) {
        this.question_id = question_id;
    }

    public String getSurvey_type() {
        return survey_type;
    }

    public void setSurvey_type(String survey_type) {
        this.survey_type = survey_type;
    }

    public String getList_question() {
        return list_question;
    }

    public void setList_question(String list_question) {
        this.list_question = list_question;
    }

    public String getPertanyaan_ke() {
        return pertanyaan_ke;
    }

    public void setPertanyaan_ke(String pertanyaan_ke) {
        this.pertanyaan_ke = pertanyaan_ke;
    }
}
