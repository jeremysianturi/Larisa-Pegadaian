package id.co.pegadaian.diarium.model;

public class TemplateKuisionerModel {
    String template_id;
    String template_name;
    String template_type;

    public TemplateKuisionerModel(String template_id, String template_name, String template_type) {
        this.template_id = template_id;
        this.template_name = template_name;
        this.template_type = template_type;
    }

    public String getTemplate_id() {
        return template_id;
    }

    public void setTemplate_id(String template_id) {
        this.template_id = template_id;
    }

    public String getTemplate_name() {
        return template_name;
    }

    public void setTemplate_name(String template_name) {
        this.template_name = template_name;
    }

    public String getTemplate_type() {
        return template_type;
    }

    public void setTemplate_type(String template_type) {
        this.template_type = template_type;
    }
}
