package id.co.pegadaian.diarium.model;

public class PaymentModel {

  private String image_url;
  private String tittle;
  private String amount;

  public PaymentModel(String image_url, String tittle, String amount) {
    this.image_url = image_url;
    this.tittle = tittle;
    this.amount = amount;
  }

  public String getImage_url() {
    return image_url;
  }

  public void setImage_url(String image_url) {
    this.image_url = image_url;
  }

  public String getTittle() {
    return tittle;
  }

  public void setTittle(String tittle) {
    this.tittle = tittle;
  }

  public String getAmount() {
    return amount;
  }

  public void setAmount(String amount) {
    this.amount = amount;
  }
}
