/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.coffeemanager.model;

/**
 *
 * @author Yuu
 */
public class Employees {

    private String maNV;
    private String fullName;
    private String sex;
    private String chucVu;
    private String taiKhoan;
    private String matKhau;
    private String soDienThoai;
    private String email;

    public Employees() {
    }

    public Employees(String maNV, String fullName, String sex, String chucVu, String taiKhoan, String matKhau, String soDienThoai, String email) {
        this.maNV = maNV;
        this.fullName = fullName;
        this.sex = sex;
        this.chucVu = chucVu;
        this.taiKhoan = taiKhoan;
        this.matKhau = matKhau;
        this.soDienThoai = soDienThoai;
        this.email = email;
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

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getChucVu() {
        return chucVu;
    }

    public void setChucVu(String chucVu) {
        this.chucVu = chucVu;
    }

    public String getTaiKhoan() {
        return taiKhoan;
    }

    public void setTaiKhoan(String taiKhoan) {
        this.taiKhoan = taiKhoan;
    }

    public String getMatKhau() {
        return matKhau;
    }

    public void setMatKhau(String matKhau) {
        this.matKhau = matKhau;
    }

    public String getSoDienThoai() {
        return soDienThoai;
    }

    public void setSoDienThoai(String soDienThoai) {
        this.soDienThoai = soDienThoai;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "Employees{"
                + "maNV='" + maNV + '\''
                + ", fullName='" + fullName + '\''
                + ", sex='" + sex + '\''
                + ", chucVu='" + chucVu + '\''
                + ", taiKhoan='" + taiKhoan + '\''
                + ", matKhau='" + matKhau + '\''
                + ", soDienThoai='" + soDienThoai + '\''
                + ", email='" + email + '\''
                + '}';
    }
}