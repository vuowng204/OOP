/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package view;

/**
 *
 * @author Admin
 */
import javax.swing.*;
import java.awt.*;

public class PlaceholderPasswordField extends JPasswordField {
    private String placeholder;

    public PlaceholderPasswordField() {
        super();
    }

    public void setPlaceholder(String text) {
        this.placeholder = text;
        repaint();
    }

    public String getPlaceholder() {
        return this.placeholder;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (getPassword().length == 0 && placeholder != null) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setColor(Color.GRAY);
            g2.setFont(getFont().deriveFont(Font.ITALIC));
            Insets insets = getInsets();
            int x = insets.left + 5;
            int y = getHeight() / 2 + getFont().getSize() / 2 - 2;
            g2.drawString(placeholder, x, y);
            g2.dispose();
        }
    }
}