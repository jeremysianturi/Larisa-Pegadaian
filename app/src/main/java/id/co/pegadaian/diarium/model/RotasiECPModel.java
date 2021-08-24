package id.co.pegadaian.diarium.model;

public class RotasiECPModel {

  String aplicans ;
  String band;
  String rank;
  String score;
  String tittle;
  int number;

  public RotasiECPModel(String aplicans, String band, String rank, String score, String tittle, int number) {
    this.aplicans = aplicans;
    this.band = band;
    this.rank = rank;
    this.score = score;
    this.tittle = tittle;
    this.number = number;
  }

  public String getAplicans() {
    return aplicans;
  }

  public void setAplicans(String aplicans) {
    this.aplicans = aplicans;
  }

  public String getBand() {
    return band;
  }

  public void setBand(String band) {
    this.band = band;
  }

  public String getRank() {
    return rank;
  }

  public void setRank(String rank) {
    this.rank = rank;
  }

  public String getScore() {
    return score;
  }

  public void setScore(String score) {
    this.score = score;
  }

  public String getTittle() {
    return tittle;
  }

  public void setTittle(String tittle) {
    this.tittle = tittle;
  }

  public int getNumber() {
    return number;
  }

  public void setNumber(int number) {
    this.number = number;
  }
}
