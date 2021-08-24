package id.co.pegadaian.diarium.model;

public class PersonalDataModel {
    String name;
    String value;
    String objectIdentifier;
    String type;
    String objectCode;

    public PersonalDataModel(String name, String value, String objectIdentifier, String type, String objectCode) {
        this.name = name;
        this.value = value;
        this.objectIdentifier = objectIdentifier;
        this.type = type;
        this.objectCode = objectCode;
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

    public String getObjectIdentifier() {
        return objectIdentifier;
    }

    public void setObjectIdentifier(String objectIdentifier) {
        this.objectIdentifier = objectIdentifier;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getObjectCode() {
        return objectCode;
    }

    public void setObjectCode(String objectCode) {
        this.objectCode = objectCode;
    }
}
