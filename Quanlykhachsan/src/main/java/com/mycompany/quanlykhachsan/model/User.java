package com.mycompany.quanlykhachsan.model;

public class User {
    private String userID;
    private String username;
    private String password;
    private String fullname;
    private String role;

    public User(String userID, String username, String password, String fullname, String role) {
        this.userID = userID;
        this.username = username;
        this.password = password;
        this.fullname = fullname;
        this.role = role;
    }

    public String getUserID() {
        return userID;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getFullname() {
        return fullname;
    }

    public String getRole() {
        return role;
    }

    public boolean login(String inputUsername, String inputPassword) {
        return this.username.equals(inputUsername) && this.password.equals(inputPassword);
    }

    public void logout() {
        System.out.println(fullname + " đã đăng xuất.");
    }
}
