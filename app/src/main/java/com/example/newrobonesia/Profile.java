package com.example.newrobonesia;

public class Profile {
    private String name, fotoUrl, nis, sekolah, kelas, alamat;

    public Profile(){
        this.name = "";
        this.fotoUrl = "";
        this.nis = "";
        this.sekolah ="";
        this.kelas ="";
        this.alamat ="";
    }

    public String getSekolah() {
        return sekolah;
    }

    public String getKelas() {
        return kelas;
    }

    public void setKelas(String kelas) {
        this.kelas = kelas;
    }

    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }

    public void setSekolah(String sekolah) {
        this.sekolah = sekolah;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFotoUrl() {
        return fotoUrl;
    }

    public void setFotoUrl(String fotoUrl) {
        this.fotoUrl = fotoUrl;
    }

    public String getNis() {
        return nis;
    }

    public void setNis(String nis) {
        this.nis = nis;
    }
}
