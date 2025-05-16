package com.example.lab4.model;

// Lớp User này chỉ dùng để chứa thông tin user trả về khi login thành công (nếu API có trả)
// Không bao gồm password
class UserLoginInfo {
    private int id;
    private String username;

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}


public class AuthResponse {
    private String status;
    private String message;
    private UserLoginInfo user; // Dùng cho login response, có thể null

    // Getters and Setters
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public UserLoginInfo getUser() {
        return user;
    }

    public void setUser(UserLoginInfo user) {
        this.user = user;
    }
}