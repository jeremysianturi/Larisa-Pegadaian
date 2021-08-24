package id.co.pegadaian.diarium.model;

public class FamilyDataModel {

    String objectIdentifier;
    String name;
    String value;
    String number;


    public FamilyDataModel(String objectIdentifier, String name, String value, String number) {
        this.objectIdentifier = objectIdentifier;
        this.name = name;
        this.value = value;
        this.number = number;
    }


    public String getObjectIdentifier() {
        return objectIdentifier;
    }

    public void setObjectIdentifier(String objectIdentifier) {
        this.objectIdentifier = objectIdentifier;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }
}
