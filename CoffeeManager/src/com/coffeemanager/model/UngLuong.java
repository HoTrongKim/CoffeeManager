/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.coffeemanager.model;

/**
 *
 * @author Yuu
 */
public class UngLuong {

    private String id;
    private String maNV;
    private String fullName;
    private String chucVu;
    private double tienUng;

    public UngLuong(String id, String maNV, String fullName, String chucVu, double tienUng) {
        this.id = id;
        this.maNV = maNV;
        this.fullName = fullName;
        this.chucVu = chucVu;
        this.tienUng = tienUng;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMaNV() {
        return maNV;
    }

    public void setMaNV(String maNV) {
        this.maNV = maNV;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getChucVu() {
        return chucVu;
    }

    public void setChucVu(String chucVu) {
        this.chucVu = chucVu;
    }

    public double getTienUng() {
        return tienUng;
    }

    public void setTienUng(double tienUng) {
        this.tienUng = tienUng;
    }
}
