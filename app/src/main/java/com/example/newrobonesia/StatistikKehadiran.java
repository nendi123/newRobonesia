package com.example.newrobonesia;

public class StatistikKehadiran {
    private String izin, sakit, alfa, hadir;

    public StatistikKehadiran(){
        this.izin = "";
        this.sakit = "";
        this.alfa = "";
        this.hadir = "";
    }

    public String getIzin() {
        return izin;
    }

    public void setIzin(String izin) {
        this.izin = izin;
    }

    public String getSakit() {
        return sakit;
    }

    public void setSakit(String sakit) {
        this.sakit = sakit;
    }

    public String getAlfa() {
        return alfa;
    }

    public void setAlfa(String alfa) {
        this.alfa = alfa;
    }

    public String getHadir() {
        return hadir;
    }

    public void setHadir(String hadir) {
        this.hadir = hadir;
    }
}
