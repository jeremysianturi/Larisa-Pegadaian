package id.co.pegadaian.diarium.model;

public class SurveyModel {
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

    public SurveyModel(String begin_date, String end_date, String business_code, String survey_id, String survey_name, String survey_content, String startdate_survey, String enddate_survey, String change_date, String change_user, String personal_number) {
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
}
