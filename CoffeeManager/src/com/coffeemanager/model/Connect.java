/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.coffeemanager.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author HoTrongKim, Yuu
 */

public class Connect {

    public List<Products> SelectAll() {
        List<Products> list = new ArrayList<>();
        try {
            Connection conn = DriverManager.getConnection("jdbc:sqlite:CoffeeManager.db");
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
            e.printStackTrace();
        }

        return list;
    }

    // Thêm sản phẩm mới
    public boolean addProduct(String name, double price) {
        String sql = "INSERT INTO Products (name, price) VALUES (?, ?)";
        try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, name);
            pstmt.setDouble(2, price);
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Sửa sản phẩm
    public boolean updateProduct(int productId, String name, double price) {
        String sql = "UPDATE Products SET name = ?, price = ? WHERE product_id = ?";
        try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, name);
            pstmt.setDouble(2, price);
            pstmt.setInt(3, productId);
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Xóa sản phẩm
    public boolean deleteProduct(int productId) {
        String sql = "DELETE FROM Products WHERE product_id = ?";
        try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, productId);
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public Connection connect() {
        String url = "jdbc:sqlite:CoffeeManager.db";
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return conn;
    }

    public Connection connectHoaDon() {
        String url = "jdbc:sqlite:CoffeeManager.db";
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url);
            System.out.println("✅ Kết nối thành công tới cơ sở dữ liệu.");
        } catch (SQLException e) {
            System.err.println("❌ Lỗi kết nối: " + e.getMessage());
            e.printStackTrace();
        }
        return conn;
    }

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

    // Lấy tất cả nhân viên
    public List<Employees> selectAllEmployees() {
        List<Employees> list = new ArrayList<>();
        String sql = "SELECT * FROM Employees";

        try (Connection conn = connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                String maNV = rs.getString("maNV");
                String fullName = rs.getString("fullName");
                String sex = rs.getString("sex");
                String chucVu = rs.getString("chucVu");
                String taiKhoan = rs.getString("taiKhoan");
                String matKhau = rs.getString("matKhau");
                String soDienThoai = rs.getString("soDienThoai");
                String email = rs.getString("email");

                Employees emp = new Employees(maNV, fullName, sex, chucVu, taiKhoan, matKhau, soDienThoai, email);
                list.add(emp);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    // Thêm nhân viên mới
    public boolean addEmployee(String maNV, String fullName, String sex, String chucVu, String taiKhoan, String matKhau, String soDienThoai, String email) {
        String sql = "INSERT INTO Employees (maNV, fullName, sex, chucVu, taiKhoan, matKhau, soDienThoai, email) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, maNV);
            pstmt.setString(2, fullName);
            pstmt.setString(3, sex);
            pstmt.setString(4, chucVu);
            pstmt.setString(5, taiKhoan);
            pstmt.setString(6, matKhau);
            pstmt.setString(7, soDienThoai);
            pstmt.setString(8, email);
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Sửa thông tin nhân viên
    public boolean updateEmployee(String maNV, String fullName, String sex, String chucVu, String taiKhoan, String matKhau, String soDienThoai, String email) {
        String sql = "UPDATE Employees SET fullName = ?, sex = ?, chucVu = ?, taiKhoan = ?, matKhau = ?, soDienThoai = ?, email = ? WHERE maNV = ?";
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, fullName);
            pstmt.setString(2, sex);
            pstmt.setString(3, chucVu);
            pstmt.setString(4, taiKhoan);
            pstmt.setString(5, matKhau);
            pstmt.setString(6, soDienThoai);
            pstmt.setString(7, email);
            pstmt.setString(8, maNV);
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Xóa nhân viên
    public boolean deleteEmployee(String maNV) {
        String sql = "DELETE FROM Employees WHERE maNV = ?";
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, maNV);
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Khởi tạo table Employees
    public void initializeEmployeesTable() {
        String sql = "DROP TABLE IF EXISTS Employees; " +
                     "CREATE TABLE Employees (" +
                     "maNV TEXT PRIMARY KEY, " +
                     "fullName TEXT, " +
                     "sex TEXT, " +
                     "chucVu TEXT, " +
                     "taiKhoan TEXT, " +
                     "matKhau TEXT, " +
                     "soDienThoai TEXT, " +
                     "email TEXT)";
        try (Connection conn = connect();
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
            System.out.println("Table Employees created successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}