/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.coffeemanager.view.code;

/**
 *
 * @author HoTrongKim
 */
public class Products {
    private int product_id;
    private String name;
    private double price;

    public Products() {
    }

    public Products(int id, String name, double price) {
        this.product_id = id;
        this.name = name;
        this.price = price;
    }

    // Getters and Setters
    public int getProduct_id() {
        return product_id;
    }

    public void setProduct_id(int product_id) {
        this.product_id = product_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "Products{" +
                "id=" + product_id +
                ", name='" + name + '\'' +
                ", price=" + price +
                '}';
    }
}