package id.co.pegadaian.diarium.model;

public class HistoryPaymentModel {

  private String status;
  private String payment;
  private String name_payment;
  private String id_1;
  private String id_2;
  private String date;
  private String time;
  private String price;

  public HistoryPaymentModel(String status, String payment, String name_payment, String id_1, String id_2, String date, String time, String price) {
    this.status = status;
    this.payment = payment;
    this.name_payment = name_payment;
    this.id_1 = id_1;
    this.id_2 = id_2;
    this.date = date;
    this.time = time;
    this.price = price;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public String getPayment() {
    return payment;
  }

  public void setPayment(String payment) {
    this.payment = payment;
  }

  public String getName_payment() {
    return name_payment;
  }

  public void setName_payment(String name_payment) {
    this.name_payment = name_payment;
  }

  public String getId_1() {
    return id_1;
  }

  public void setId_1(String id_1) {
    this.id_1 = id_1;
  }

  public String getId_2() {
    return id_2;
  }

  public void setId_2(String id_2) {
    this.id_2 = id_2;
  }

  public String getDate() {
    return date;
  }

  public void setDate(String date) {
    this.date = date;
  }

  public String getTime() {
    return time;
  }

  public void setTime(String time) {
    this.time = time;
  }

  public String getPrice() {
    return price;
  }

  public void setPrice(String price) {
    this.price = price;
  }
}
