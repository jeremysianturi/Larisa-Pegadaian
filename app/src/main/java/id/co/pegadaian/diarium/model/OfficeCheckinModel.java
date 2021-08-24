package id.co.pegadaian.diarium.model;

public class OfficeCheckinModel {

    private String mspinnerList;
    private String officeId;

    public OfficeCheckinModel(String mspinnerList, String officeId) {
        this.mspinnerList = mspinnerList;
        this.officeId = officeId;
    }

    public String getMspinnerList() {
        return mspinnerList;
    }

    public void setMspinnerList(String mspinnerList) {
        this.mspinnerList = mspinnerList;
    }

    public String getOfficeId() {
        return officeId;
    }

    public void setOfficeId(String officeId) {
        this.officeId = officeId;
    }
}
