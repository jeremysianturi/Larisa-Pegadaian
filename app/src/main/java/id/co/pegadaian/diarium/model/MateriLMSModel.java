package id.co.pegadaian.diarium.model;

public class MateriLMSModel {
    String materi_id;
    String materi_name;
    String materi_type_value;
    String materi_address;
    String method_value;
    String competence_value;
    String pl_code_value;

    public MateriLMSModel(String materi_id, String materi_name, String materi_type_value, String materi_address, String method_value, String competence_value, String pl_code_value) {
        this.materi_id = materi_id;
        this.materi_name = materi_name;
        this.materi_type_value = materi_type_value;
        this.materi_address = materi_address;
        this.method_value = method_value;
        this.competence_value = competence_value;
        this.pl_code_value = pl_code_value;
    }

    public String getMateri_id() {
        return materi_id;
    }

    public void setMateri_id(String materi_id) {
        this.materi_id = materi_id;
    }

    public String getMateri_name() {
        return materi_name;
    }

    public void setMateri_name(String materi_name) {
        this.materi_name = materi_name;
    }

    public String getMateri_type_value() {
        return materi_type_value;
    }

    public void setMateri_type_value(String materi_type_value) {
        this.materi_type_value = materi_type_value;
    }

    public String getMateri_address() {
        return materi_address;
    }

    public void setMateri_address(String materi_address) {
        this.materi_address = materi_address;
    }

    public String getMethod_value() {
        return method_value;
    }

    public void setMethod_value(String method_value) {
        this.method_value = method_value;
    }

    public String getCompetence_value() {
        return competence_value;
    }

    public void setCompetence_value(String competence_value) {
        this.competence_value = competence_value;
    }

    public String getPl_code_value() {
        return pl_code_value;
    }

    public void setPl_code_value(String pl_code_value) {
        this.pl_code_value = pl_code_value;
    }
}
