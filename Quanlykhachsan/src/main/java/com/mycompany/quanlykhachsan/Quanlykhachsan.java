/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.quanlykhachsan;

import view.Home;
import view.Login2;
import controler.LoginController;


/**
 *
 * @author Admin
 */
public class Quanlykhachsan {

    public static void main(String[] args) {
        System.out.println("? ? ?  ?");
////        new Login2().setVisible(true);
////         new Login2().setVisible(true);
//           new Home().setVisible(true);
       Login2 view = new Login2();
       new LoginController(view);
       view.setLocationRelativeTo(null);
       view.setVisible(true);
    }
}
