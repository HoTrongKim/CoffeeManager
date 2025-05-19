/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.coffeemanager.model;

/**
 *
 * @author Yuu
 */
public class ChiTietHoaDon {

    private String tenSanPham;
    private int soLuong;
    private double giaTien;
    private double thanhTien;

    public ChiTietHoaDon(String tenSanPham, int soLuong, double giaTien) {
        this.tenSanPham = tenSanPham;
        this.soLuong = soLuong;
        this.giaTien = giaTien;
        this.thanhTien = soLuong * giaTien;
    }

    public String getTenSanPham() {
        return tenSanPham;
    }

    public int getSoLuong() {
        return soLuong;
    }

    public double getGiaTien() {
        return giaTien;
    }

    public double getThanhTien() {
        return thanhTien;
    }
}
