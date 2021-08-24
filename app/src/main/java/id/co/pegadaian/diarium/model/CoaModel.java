package id.co.pegadaian.diarium.model;

public class CoaModel {
    String coa_number;
    String file;

    public CoaModel(String coa_number, String file) {
        this.coa_number = coa_number;
        this.file = file;
    }

    public String getCoa_number() {
        return coa_number;
    }

    public void setCoa_number(String coa_number) {
        this.coa_number = coa_number;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }
}
