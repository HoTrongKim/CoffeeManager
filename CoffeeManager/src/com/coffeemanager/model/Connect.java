/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.coffeemanager.model;

import com.coffeemanager.model.Products;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author HoTrongKim
 */
public class Connect {

    public List<Products> SelectAll() {
        List<Products> list = new ArrayList<>();
        try {
            Connection conn = DriverManager.getConnection("jdbc:sqlite:HoaDon.db");
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM Products");

            while (rs.next()) {
                int id = rs.getInt("product_id");
                String name = rs.getString("name");
                double price = rs.getDouble("price");

                Products p = new Products(id, name, price);
                list.add(p);
            }

            rs.close();
            stmt.close();
            conn.close();

        } catch (SQLException e) {
            e.printStackTrace(); // In lỗi nếu có
        }

        return list;
    }

    public Connection connect() {
        String url = "jdbc:sqlite:HoaDon.db";
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url);
        } catch (Exception e) {
            e.printStackTrace(); // nên in lỗi
        }
        return conn;
    }

    public Connection connectLogin() {
        String url = "jdbc:sqlite:AccountTest.db";
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url);
            System.out.println("✅ Kết nối thành công tới cơ sở dữ liệu.");
        } catch (SQLException e) {
            System.err.println("❌ Lỗi kết nối: " + e.getMessage());
            e.printStackTrace(); // In chi tiết lỗi để debug
        }
        return conn;
    }

    public Connection connectHoaDon() {
        String url = "jdbc:sqlite:HoaDon.db";
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url);
            System.out.println("✅ Kết nối thành công tới cơ sở dữ liệu.");
        } catch (SQLException e) {
            System.err.println("❌ Lỗi kết nối: " + e.getMessage());
            e.printStackTrace(); // In chi tiết lỗi để debug
        }
        return conn;
    }

    // Lấy tất cả danh sách hóa đơn
    public List<DanhSachHoaDon> getAllHoaDon() {
        List<DanhSachHoaDon> list = new ArrayList<>();
        String sql = "SELECT * FROM DanhSachHoaDon";

        try (Connection conn = connectHoaDon(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                int maHD = rs.getInt("maHD");
                String ngayTao = rs.getString("ngayTao");
                String gioTao = rs.getString("gioTao");
                double tongTien = rs.getDouble("tongTien");

                DanhSachHoaDon hd = new DanhSachHoaDon(maHD, ngayTao, gioTao, tongTien);
                list.add(hd);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

// Lấy chi tiết hóa đơn theo maHD
    public List<ChiTietHoaDon> getChiTietHoaDonByMaHD(int maHDParam) {
        List<ChiTietHoaDon> list = new ArrayList<>();
        String sql = "SELECT * FROM ChiTietHoaDon WHERE maHD = ?";

        try (Connection conn = connectHoaDon(); java.sql.PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, maHDParam);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("id");
                int maHD = rs.getInt("maHD");
                String tenSP = rs.getString("tenSanPham");
                int soLuong = rs.getInt("soLuong");
                double donGia = rs.getDouble("donGia");

                ChiTietHoaDon cthd = new ChiTietHoaDon(id, maHD, tenSP, soLuong, donGia);
                list.add(cthd);
            }

            rs.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

}
