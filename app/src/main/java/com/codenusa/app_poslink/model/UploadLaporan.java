package com.codenusa.app_poslink.model;

public class UploadLaporan {

    private int id_laporan;
    private String judul;
    private String deskripsi;
    private String foto;
    private String tgl;
    private String status;

    public UploadLaporan(){

    }

    public UploadLaporan(int id_laporan, String judul, String deskripsi, String foto, String tgl, String status) {
        this.id_laporan = id_laporan;
        this.judul = judul;
        this.deskripsi = deskripsi;
        this.foto = foto;
        this.tgl = tgl;
        this.status = status;
    }

    public int getId_laporan() {
        return id_laporan;
    }

    public void setId_laporan(int id_laporan) {
        this.id_laporan = id_laporan;
    }

    public String getJudul() {
        return judul;
    }

    public void setJudul(String judul) {
        this.judul = judul;
    }

    public String getDeskripsi() {
        return deskripsi;
    }

    public void setDeskripsi(String deskripsi) {
        this.deskripsi = deskripsi;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public String getTgl() {
        return tgl;
    }

    public void setTgl(String tgl) {
        this.tgl = tgl;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
