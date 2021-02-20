package com.example.newrobonesia;

public class Profile {
    private String name, fotoUrl, nis;

    public Profile(){
        this.name = "";
        this.fotoUrl = "";
        this.nis = "";
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
