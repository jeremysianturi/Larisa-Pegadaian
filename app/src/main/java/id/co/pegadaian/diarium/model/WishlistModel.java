package id.co.pegadaian.diarium.model;

public class WishlistModel {

  private String tittle;
  private String price;
  private String bestseller;
  private String rating;
  private String views;
  private String account;
  private String url;

  public WishlistModel(String tittle, String price, String bestseller, String rating, String views, String account, String url) {
    this.tittle = tittle;
    this.price = price;
    this.bestseller = bestseller;
    this.rating = rating;
    this.views = views;
    this.account = account;
    this.url = url;
  }

  public String getTittle() {
    return tittle;
  }

  public void setTittle(String tittle) {
    this.tittle = tittle;
  }

  public String getPrice() {
    return price;
  }

  public void setPrice(String price) {
    this.price = price;
  }

  public String getBestseller() {
    return bestseller;
  }

  public void setBestseller(String bestseller) {
    this.bestseller = bestseller;
  }

  public String getRating() {
    return rating;
  }

  public void setRating(String rating) {
    this.rating = rating;
  }

  public String getViews() {
    return views;
  }

  public void setViews(String views) {
    this.views = views;
  }

  public String getAccount() {
    return account;
  }

  public void setAccount(String account) {
    this.account = account;
  }

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }
}
