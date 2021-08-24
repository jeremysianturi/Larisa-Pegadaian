package id.co.pegadaian.diarium.model;

public class DataAddDataModel {
    String dataName;
    String dataCode;

    public DataAddDataModel(String dataName, String dataCode) {
        this.dataName = dataName;
        this.dataCode = dataCode;
    }

    public String getDataName() {return dataName;}
    public void setDataName(String dataName) {this.dataName = dataName;}

    public String getDataCode() {return dataCode;}
    public void setDataCode(String dataCode) {this.dataCode = dataCode;}
}
