package id.co.pegadaian.diarium.model;

public class MyCareerActivityModel {
  String namaLengkap;
  String job;
  String job_posisi;
  String band;
  String lokasi_kantor;
  String nama_perusahaan;
  String NIK;

  public MyCareerActivityModel(String namaLengkap, String job, String job_posisi, String band, String lokasi_kantor, String nama_perusahaan,String NIK) {
    this.namaLengkap = namaLengkap;
    this.job = job;
    this.job_posisi = job_posisi;
    this.band = band;
    this.lokasi_kantor = lokasi_kantor;
    this.nama_perusahaan = nama_perusahaan;
    this.NIK = NIK;
  }

  public String getNIK() {
    return NIK;
  }

  public void setNIK(String NIK) {
    this.NIK = NIK;
  }

  public String getNamaLengkap() {
    return namaLengkap;
  }

  public void setNamaLengkap(String namaLengkap) {
    this.namaLengkap = namaLengkap;
  }

  public String getJob() {
    return job;
  }

  public void setJob(String job) {
    this.job = job;
  }

  public String getJob_posisi() {
    return job_posisi;
  }

  public void setJob_posisi(String job_posisi) {
    this.job_posisi = job_posisi;
  }

  public String getBand() {
    return band;
  }

  public void setBand(String band) {
    this.band = band;
  }

  public String getLokasi_kantor() {
    return lokasi_kantor;
  }

  public void setLokasi_kantor(String lokasi_kantor) {
    this.lokasi_kantor = lokasi_kantor;
  }

  public String getNama_perusahaan() {
    return nama_perusahaan;
  }

  public void setNama_perusahaan(String nama_perusahaan) {
    this.nama_perusahaan = nama_perusahaan;
  }
}
