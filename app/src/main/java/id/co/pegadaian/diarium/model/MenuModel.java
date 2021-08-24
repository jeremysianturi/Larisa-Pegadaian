package id.co.pegadaian.diarium.model;

public class MenuModel {
    String menuCode;
    String menuName;
    String menuIcon;

    public MenuModel(String menuCode, String menuName, String menuIcon) {
        this.menuCode = menuCode;
        this.menuName = menuName;
        this.menuIcon = menuIcon;
    }

    public String getMenuCode() {return menuCode;}
    public void setMenuCode(String menuCode) {this.menuCode = menuCode;}

    public String getMenuName() {return menuName;}
    public void setMenuName(String menuName) {this.menuName = menuName;}

    public String getMenuIcon() {return menuIcon;}
    public void setMenuIcon(String menuIcon) {this.menuIcon = menuIcon;}
}
