package com.mycompany.quanlykhachsan.model;

import java.util.ArrayList;
import java.util.List;

public class UserDatabase {
    public static List<User> users = new ArrayList<>();

    static {
        users.add(new User("0000", "admin", "1234", "Quản trị viên", "admin"));
        users.add(new User("0001", "nv01", "1111", "Nhân viên A", "employee"));
    }

    public static User checkLogin(String username, String password) {
        for (User u : users) {
            if (u.login(username, password)) {
                return u;
            }
        }
        return null;
    }
}
