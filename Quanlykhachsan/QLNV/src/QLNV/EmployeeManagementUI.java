package QLNV;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class EmployeeManagementUI extends JFrame {

    private JLabel lblImageDisplay;

    public EmployeeManagementUI() {
        setTitle("Quản Lý Nhân Viên");
        setSize(1100, 750);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(20, 50, 80));

        // === FORM PANEL + IMAGE PANEL ===
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(new Color(20, 50, 80));

        // === LEFT: FORM ===
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(new Color(20, 50, 80));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 10, 5, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // === Row 1 ===
        gbc.gridy = 0;
        gbc.gridx = 0;
        formPanel.add(createLabel("ID Nhân Viên"), gbc);
        gbc.gridx = 1;
        JTextField txtID = new JTextField(15);
        formPanel.add(txtID, gbc);

        gbc.gridx = 2;
        formPanel.add(createLabel("Số Điện Thoại"), gbc);
        gbc.gridx = 3;
        JTextField txtPhone = new JTextField(15);
        formPanel.add(txtPhone, gbc);

        // === Row 2 ===
        gbc.gridy++;
        gbc.gridx = 0;
        formPanel.add(createLabel("Tên Nhân Viên"), gbc);
        gbc.gridx = 1;
        JTextField txtName = new JTextField(15);
        formPanel.add(txtName, gbc);

        gbc.gridx = 2;
        formPanel.add(createLabel("Giới Tính"), gbc);
        gbc.gridx = 3;
        JRadioButton rbMale = new JRadioButton("Nam");
        JRadioButton rbFemale = new JRadioButton("Nữ");
        setupRadio(rbMale); setupRadio(rbFemale);
        ButtonGroup genderGroup = new ButtonGroup();
        genderGroup.add(rbMale); genderGroup.add(rbFemale);
        JPanel genderPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        genderPanel.setBackground(new Color(20, 50, 80));
        genderPanel.add(rbMale); genderPanel.add(rbFemale);
        formPanel.add(genderPanel, gbc);

        // === Row 3 ===
        gbc.gridy++;
        gbc.gridx = 0;
        formPanel.add(createLabel("Hình Ảnh"), gbc);
        gbc.gridx = 1;
        JButton btnChooseImage = new JButton("Nhập Ảnh");
        formPanel.add(btnChooseImage, gbc);

        gbc.gridx = 2;
        formPanel.add(createLabel("Mật Khẩu"), gbc);
        gbc.gridx = 3;
        JPasswordField txtPassword = new JPasswordField(15);
        formPanel.add(txtPassword, gbc);

        // === Row 4 ===
        gbc.gridy++;
        gbc.gridx = 0;
        formPanel.add(createLabel("Xác Nhận MK"), gbc);
        gbc.gridx = 1;
        JPasswordField txtConfirm = new JPasswordField(15);
        formPanel.add(txtConfirm, gbc);

        gbc.gridx = 2;
        formPanel.add(createLabel("Ngày Sinh"), gbc);
        gbc.gridx = 3;
        JTextField txtBirth = new JTextField(15);
        formPanel.add(txtBirth, gbc);

        // === Row 5 ===
        gbc.gridy++;
        gbc.gridx = 0;
        formPanel.add(createLabel("Email"), gbc);
        gbc.gridx = 1;
        JTextField txtEmail = new JTextField(15);
        formPanel.add(txtEmail, gbc);

        gbc.gridx = 2;
        formPanel.add(createLabel("Tên Đăng Nhập"), gbc);
        gbc.gridx = 3;
        JTextField txtUsername = new JTextField(15);
        formPanel.add(txtUsername, gbc);

        // === Row 6 ===
        gbc.gridy++;
        gbc.gridx = 0;
        formPanel.add(createLabel("Quyền"), gbc);
        gbc.gridx = 1;
        JRadioButton rbEmployee = new JRadioButton("Nhân Viên");
        JRadioButton rbAdmin = new JRadioButton("Admin");
        setupRadio(rbEmployee); setupRadio(rbAdmin);
        ButtonGroup roleGroup = new ButtonGroup();
        roleGroup.add(rbEmployee); roleGroup.add(rbAdmin);
        JPanel rolePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        rolePanel.setBackground(new Color(20, 50, 80));
        rolePanel.add(rbEmployee); rolePanel.add(rbAdmin);
        formPanel.add(rolePanel, gbc);

        // === Row 7: Buttons ===
        gbc.gridy++;
        gbc.gridx = 0;
        gbc.gridwidth = 4;
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(new Color(20, 50, 80));
        buttonPanel.add(new JButton("THÊM"));
        buttonPanel.add(new JButton("XÓA"));
        buttonPanel.add(new JButton("SỬA"));
        buttonPanel.add(new JButton("LƯU"));
        buttonPanel.add(new JButton("QUAY LẠI"));
        formPanel.add(buttonPanel, gbc);
        gbc.gridwidth = 1;

        topPanel.add(formPanel, BorderLayout.CENTER);

        // === RIGHT: IMAGE PANEL ===
        JPanel imagePanel = new JPanel(new BorderLayout());
        imagePanel.setPreferredSize(new Dimension(200, 240));
        imagePanel.setBackground(new Color(30, 60, 90));
        lblImageDisplay = new JLabel("Chưa có ảnh", SwingConstants.CENTER);
        lblImageDisplay.setForeground(Color.WHITE);
        lblImageDisplay.setBorder(BorderFactory.createLineBorder(Color.WHITE));
        imagePanel.add(lblImageDisplay, BorderLayout.CENTER);
        topPanel.add(imagePanel, BorderLayout.EAST);

        // === TABLE ===
        String[] cols = {"Mã NV", "Tên NV", "Ngày Sinh", "SĐT", "Email", "Giới Tính", "Hình Ảnh", "Tài Khoản", "Mật Khẩu", "Quyền"};
        DefaultTableModel model = new DefaultTableModel(cols, 0);
        JTable table = new JTable(model);
        JScrollPane tableScroll = new JScrollPane(table);

        // === ADD TO MAIN PANEL ===
        mainPanel.add(topPanel, BorderLayout.NORTH);
        mainPanel.add(tableScroll, BorderLayout.CENTER);
        add(mainPanel);
    }

    private JLabel createLabel(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setForeground(Color.WHITE);
        return lbl;
    }

    private void setupRadio(JRadioButton rb) {
        rb.setForeground(Color.WHITE);
        rb.setBackground(new Color(20, 50, 80));
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new EmployeeManagementUI().setVisible(true));
    }

	@Override
	public String toString() {
		return "EmployeeManagementUI [lblImageDisplay=" + lblImageDisplay + "]";
	}

	public JLabel getLblImageDisplay() {
		return lblImageDisplay;
	}

	public void setLblImageDisplay(JLabel lblImageDisplay) {
		this.lblImageDisplay = lblImageDisplay;
	}
}


