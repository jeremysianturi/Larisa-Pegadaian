package id.co.pegadaian.diarium.model;

public class TesModel {
    String template_name;
    String type;
    String test_code_id;

    public TesModel(String template_name, String type, String test_code_id) {
        this.template_name = template_name;
        this.type = type;
        this.test_code_id = test_code_id;
    }

    public String getTemplate_name() {
        return template_name;
    }

    public void setTemplate_name(String template_name) {
        this.template_name = template_name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTest_code_id() {
        return test_code_id;
    }

    public void setTest_code_id(String test_code_id) {
        this.test_code_id = test_code_id;
    }
}
