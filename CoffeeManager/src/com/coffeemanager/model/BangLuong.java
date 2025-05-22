/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.coffeemanager.model;

/**
 *
 * @author ADMIN
 */
public class BangLuong {

    private String id;
    private String maNV;
    private String fullName;
    private String chucVu;
    private double gioLam;
    private double baseLuong;
    private double thucNhan;

    public BangLuong() {
    }

    /**
     * Khởi tạo và tự động tính thucNhan = gioLam * baseLuong
     */
    public BangLuong(String id, String maNV, String fullName, String chucVu,
            double gioLam, double baseLuong) {
        this.id = id;
        this.maNV = maNV;
        this.fullName = fullName;
        this.chucVu = chucVu;
        this.gioLam = gioLam;
        this.baseLuong = baseLuong;
        this.thucNhan = this.gioLam * this.baseLuong;
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

    public double getGioLam() {
        return gioLam;
    }

    public void setGioLam(double gioLam) {
        this.gioLam = gioLam;
        updateThucNhan();
    }

    public double getBaseLuong() {
        return baseLuong;
    }

    public void setBaseLuong(double baseLuong) {
        this.baseLuong = baseLuong;
        updateThucNhan();
    }

    public double getThucNhan() {
        return thucNhan;
    }

    public void setThucNhan(double thucNhan) {
        this.thucNhan = thucNhan;
    }

    /**
     * Cập nhật lại thucNhan khi gioLam hoặc baseLuong thay đổi
     */
    private void updateThucNhan() {
        this.thucNhan = this.gioLam * this.baseLuong;
    }
}
