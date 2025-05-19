/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.coffeemanager.model;

/**
 *
 * @author HoTrongKim
 */
public class Account {

    private String User;
    private String Pass;

    public Account() {
    }

    public Account(String User, String Pass) {
        this.User = User;
        this.Pass = Pass;
    }

    public String getPass() {
        return Pass;
    }

    public String getUser() {
        return User;
    }

    public void setPass(String Pass) {
        this.Pass = Pass;
    }

    public void setUser(String User) {
        this.User = User;
    }
}
