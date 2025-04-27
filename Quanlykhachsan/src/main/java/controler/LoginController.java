/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controler;

import QuanLyKhachSan.dao.UserDAO;
import com.mycompany.quanlykhachsan.model.User;
import view.Home;
import view.Login2;
import javax.swing.JOptionPane;

public class LoginController {
    private final Login2 view;
    private final UserDAO userDAO;

    public LoginController(Login2 view) {
        this.view = view;
        this.userDAO = new UserDAO();
        initController();
    }

    private void initController() {
        view.addLoginListener(e -> handleLogin());
    }

    private void handleLogin() {
        String username = view.getUsername();
        String password = view.getPassword();

        if (validateInput(username, password)) {
            User user = userDAO.authenticate(username, password);
            
            if (user != null) {
                view.showMessage("Đăng nhập thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                // Tạo Home với user và đóng Login2
                Home home = new Home(user);
                home.setVisible(true);
                view.dispose();
            } else {
                view.showMessage("Sai tên đăng nhập hoặc mật khẩu", "Lỗi đăng nhập", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private boolean validateInput(String username, String password) {
        StringBuilder sb = new StringBuilder();
        if (username.isEmpty()) {
            sb.append("Tên đăng nhập không được để trống!\n");
        }
        if (password.isEmpty()) {
            sb.append("Mật khẩu không được để trống!");
        }
        
        if (sb.length() > 0) {
            view.showMessage(sb.toString(), "Lỗi nhập liệu", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        return true;
    }

    public void showLoginView() {
        view.setVisible(true);
    }
}
