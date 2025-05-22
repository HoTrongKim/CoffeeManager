/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.coffeemanager.model;

/**
 *
 * @author ADMIN
 */
public class Luong {

    private int id;
    private String maNV;
    private String hoTen;
    private String chucVu;
    private double tongGio;
    private int soCa;
    private double luongCoBan;
    private double thucNhan;

    public Luong() {
    }

    public Luong(String maNV, String hoTen, String chucVu, double tongGio, int soCa, double luongCoBan) {
        this.maNV = maNV;
        this.hoTen = hoTen;
        this.chucVu = chucVu;
        this.tongGio = tongGio;
        this.soCa = soCa;
        this.luongCoBan = luongCoBan;
        this.thucNhan = tongGio * luongCoBan;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMaNV() {
        return maNV;
    }

    public void setMaNV(String maNV) {
        this.maNV = maNV;
    }

    public String getHoTen() {
        return hoTen;
    }

    public void setHoTen(String hoTen) {
        this.hoTen = hoTen;
    }

    public String getChucVu() {
        return chucVu;
    }

    public void setChucVu(String chucVu) {
        this.chucVu = chucVu;
    }

    public double getTongGio() {
        return tongGio;
    }

    public void setTongGio(double tongGio) {
        this.tongGio = tongGio;
    }

    public int getSoCa() {
        return soCa;
    }

    public void setSoCa(int soCa) {
        this.soCa = soCa;
    }

    public double getLuongCoBan() {
        return luongCoBan;
    }

    public void setLuongCoBan(double luongCoBan) {
        this.luongCoBan = luongCoBan;
    }

    public double getThucNhan() {
        return thucNhan;
    }

    public void setThucNhan(double thucNhan) {
        this.thucNhan = thucNhan;
    }
    
}