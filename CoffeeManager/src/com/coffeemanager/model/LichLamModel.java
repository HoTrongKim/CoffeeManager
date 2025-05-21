package com.coffeemanager.model;

public class LichLamModel {

    private int id;
    private String maNV;
    private String thu;
    private String ca;
    private double soGio;

    public LichLamModel(int id, String maNV, String thu, String ca, double soGio) {
        this.id = id;
        this.maNV = maNV;
        this.thu = thu;
        this.ca = ca;
        this.soGio = soGio;
    }

    // Getters
    public int getId() {
        return id;
    }

    public String getMaNV() {
        return maNV;
    }

    public String getThu() {
        return thu;
    }

    public String getCa() {
        return ca;
    }

    public double getSoGio() {
        return soGio;
    }
}
