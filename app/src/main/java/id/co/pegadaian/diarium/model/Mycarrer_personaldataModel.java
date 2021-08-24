package id.co.pegadaian.diarium.model;

public class Mycarrer_personaldataModel {

  String url_foto ;
  String company_career_activity;

  public Mycarrer_personaldataModel(String url_foto, String company_career_activity) {
    this.url_foto = url_foto;
    this.company_career_activity = company_career_activity;
  }

  public String getUrl_foto() {
    return url_foto;
  }

  public void setUrl_foto(String url_foto) {
    this.url_foto = url_foto;
  }

  public String getCompany_career_activity() {
    return company_career_activity;
  }

  public void setCompany_career_activity(String company_career_activity) {
    this.company_career_activity = company_career_activity;
  }
}
