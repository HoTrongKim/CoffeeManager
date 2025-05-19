/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.coffeemanager.model;

/**
 *
 * @author Yuu
 */
public class DanhSachHoaDon {

    private int maHD;
    private String ngayTao;
    private String gioTao;
    private double tongTien;

    public DanhSachHoaDon(int maHD, String ngayTao, String gioTao, double tongTien) {
        this.maHD = maHD;
        this.ngayTao = ngayTao;
        this.gioTao = gioTao;
        this.tongTien = tongTien;
    }

    // Getter và Setter nếu cần
    public int getMaHD() {
        return maHD;
    }

    public String getNgayTao() {
        return ngayTao;
    }

    public String getGioTao() {
        return gioTao;
    }

    public double getTongTien() {
        return tongTien;
    }

    public void setMaHD(int maHD) {
        this.maHD = maHD;
    }

    public void setNgayTao(String ngayTao) {
        this.ngayTao = ngayTao;
    }

    public void setGioTao(String gioTao) {
        this.gioTao = gioTao;
    }

    public void setTongTien(double tongTien) {
        this.tongTien = tongTien;
    }

}
