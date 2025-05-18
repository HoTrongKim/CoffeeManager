/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.coffeemanager.view.ConnectSql;

import com.coffeemanager.view.code.Invoice;
import com.coffeemanager.view.code.InvoiceDetails;
import com.coffeemanager.view.code.Products;
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

}
