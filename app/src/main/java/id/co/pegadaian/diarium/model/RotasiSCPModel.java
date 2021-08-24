package id.co.pegadaian.diarium.model;

public class RotasiSCPModel {

  private String object_identifier ;
  private String personel_number ;
  private String personel_name ;
  private String band_position ;
  private String class_position ;
  private String code_level_movement ;
  private String level_movement ;
  private String position_code ;
  private String position_name ;
  private String score ;
  private int number;

  public RotasiSCPModel(String object_identifier, String personel_number, String personel_name, String band_position, String class_position, String code_level_movement, String level_movement, String position_code, String position_name, String score, int number) {
    this.object_identifier = object_identifier;
    this.personel_number = personel_number;
    this.personel_name = personel_name;
    this.band_position = band_position;
    this.class_position = class_position;
    this.code_level_movement = code_level_movement;
    this.level_movement = level_movement;
    this.position_code = position_code;
    this.position_name = position_name;
    this.score = score;
    this.number = number;
  }

  public String getObject_identifier() {
    return object_identifier;
  }

  public void setObject_identifier(String object_identifier) {
    this.object_identifier = object_identifier;
  }

  public String getPersonel_number() {
    return personel_number;
  }

  public void setPersonel_number(String personel_number) {
    this.personel_number = personel_number;
  }

  public String getPersonel_name() {
    return personel_name;
  }

  public void setPersonel_name(String personel_name) {
    this.personel_name = personel_name;
  }

  public String getBand_position() {
    return band_position;
  }

  public void setBand_position(String band_position) {
    this.band_position = band_position;
  }

  public String getClass_position() {
    return class_position;
  }

  public void setClass_position(String class_position) {
    this.class_position = class_position;
  }

  public String getCode_level_movement() {
    return code_level_movement;
  }

  public void setCode_level_movement(String code_level_movement) {
    this.code_level_movement = code_level_movement;
  }

  public String getLevel_movement() {
    return level_movement;
  }

  public void setLevel_movement(String level_movement) {
    this.level_movement = level_movement;
  }

  public String getPosition_code() {
    return position_code;
  }

  public void setPosition_code(String position_code) {
    this.position_code = position_code;
  }

  public String getPosition_name() {
    return position_name;
  }

  public void setPosition_name(String position_name) {
    this.position_name = position_name;
  }

  public String getScore() {
    return score;
  }

  public void setScore(String score) {
    this.score = score;
  }

  public int getNumber() {
    return number;
  }

  public void setNumber(int number) {
    this.number = number;
  }
}

