package com.example.lab4.model;

public class User {
    private String username;
    private String password; // Sẽ chứa mật khẩu đã mã hóa MD5 khi đăng ký, hoặc mật khẩu MD5 khi đăng nhập

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    // Getters
    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}