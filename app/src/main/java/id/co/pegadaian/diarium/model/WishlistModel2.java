package id.co.pegadaian.diarium.model;

public class WishlistModel2 {


  private int id_course;
  private int id_part;

  public WishlistModel2(int id_course, int id_part) {
    this.id_course = id_course;
    this.id_part = id_part;
  }

  public int getId_course() {
    return id_course;
  }

  public void setId_course(int id_course) {
    this.id_course = id_course;
  }

  public int getId_part() {
    return id_part;
  }

  public void setId_part(int id_part) {
    this.id_part = id_part;
  }
}
