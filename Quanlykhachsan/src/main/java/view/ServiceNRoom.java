/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package view;

import QuanLyKhachSan.dao.BookingDAO;
import QuanLyKhachSan.dao.CustomerDAO;
import QuanLyKhachSan.dao.JDBCConnection;
import QuanLyKhachSan.dao.RoomDAO;
import QuanLyKhachSan.dao.ServiceUsageDAO;
import com.mycompany.quanlykhachsan.model.Booking;
import com.mycompany.quanlykhachsan.model.Customer;
import com.mycompany.quanlykhachsan.model.Room;
import com.mycompany.quanlykhachsan.model.Service;
import com.mycompany.quanlykhachsan.model.User;
import controler.ServiceNRoomController;
import java.awt.Color;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import com.mycompany.quanlykhachsan.model.ServiceUsage;
import java.io.*;
import java.awt.Desktop;
import javax.swing.JTable;
import javax.swing.table.TableModel;
import javax.swing.JOptionPane;
import java.awt.Component;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;

/**
 *
 * @author lenovo
 */
public class ServiceNRoom extends javax.swing.JFrame {

    private Home home;
    private int roomId;
    private Customer customer;
    private User currentUser;
    private boolean isCheckedIn;
    private int bookingId;
    private ServiceNRoomController controller;
    private boolean isRoomCheckedOut; // Biến để kiểm tra trạng thái phòng

    /**
     * Creates new form ServiceNRoom
     */
    public ServiceNRoom() {
        initComponents();
//        TenNhanvien.setBackground(Color.lightGray);
        

    }

    public ServiceNRoom(Home home, int roomId, Room room, User currentUser) {
        initComponents();
        this.home = home;
        this.roomId = roomId;
        this.currentUser = currentUser;
        this.isCheckedIn = false;
        this.bookingId = -1;
        this.isRoomCheckedOut = false; // Thêm dòng này
      
        
        

        setTitle("Chi tiết phòng");
        TenphongjLabel1.setText(room.getTenPhong());
        GiaphongjLabel2.setText(String.format("%,.0f VND", room.getGiaPhong()));
        TenNhanvien.setText(currentUser != null && currentUser.getFullname() != null ? currentUser.getFullname() : "Không xác định");
       
        // Khởi tạo Controller
        this.controller = new ServiceNRoomController(this, bookingId, isCheckedIn);

        // Tải danh sách dịch vụ mặc định
        controller.searchServices("");
         

        loadtb();
        

        // Kiểm tra xem phòng đã check-in chưa
        try {
            BookingDAO bookingDAO = new BookingDAO(JDBCConnection.getConnection());
            Booking booking = bookingDAO.getLatestBookingByRoomId(roomId);
            if (booking != null && booking.getStatus().equals("Đang sử dụng")) {
                isCheckedIn = true;
                bookingId = booking.getId();
                this.controller = new ServiceNRoomController(this, bookingId, isCheckedIn);
                Customer customer = booking.getCustomer();
                Tenkh.setText(customer.getTenKhachHang());
                CCCD.setText(customer.getCccd());
                jTextField6.setText(customer.getSoDienThoai());
                jComboBox1.setSelectedItem(customer.getGioiTinh());
                jTextPane5.setText(String.valueOf(booking.getSoNguoiO()));
                TrangThaiHD.setText(booking.getStatus());
                TrangThaiHD.setBackground(Color.GREEN);

                if (booking.getCheckInDate() != null) {
                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                    jTextPane8.setText(sdf.format(booking.getCheckInDate()));
                }

                if (booking.getCheckOutDate() != null) {
                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                    jTextPane9.setText(sdf.format(booking.getCheckOutDate()));
                    long durationMillis = booking.getCheckOutDate().getTime() - booking.getCheckInDate().getTime();
                    long hours = durationMillis / (1000 * 60 * 60);
                    long minutes = (durationMillis % (1000 * 60 * 60)) / (1000 * 60);
                    jTextPane11.setText(hours + " giờ " + minutes + " phút");
                }

                jTextPane1.setText(String.format("HD%04d", booking.getId()));
                CheckinjButton5.setEnabled(!isCheckedIn);
                checkout.setEnabled(isCheckedIn);
                TinhTienjButton7.setEnabled(isCheckedIn); // Thêm dòng này

                // Tải danh sách dịch vụ đã sử dụng
                controller.loadServiceUsages();
            } else {
                resetForm();
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Lỗi khi tải thông tin check-in: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
    private  void loadtb(){
        
        DefaultTableModel defaultTableModel = new DefaultTableModel();
        jTable1.setModel(defaultTableModel);
        defaultTableModel.addColumn("Mã Hóa Đơn");
        defaultTableModel.addColumn("Mã Phòng");
        defaultTableModel.addColumn("Tên Khách hàng");
        defaultTableModel.addColumn("Check in");
        defaultTableModel.addColumn("Check out");
        defaultTableModel.addColumn("Tổng Tiền");
        defaultTableModel.addColumn("Trạng thái HD");
      
        List<Booking> bookings = controller.getAllBookings(roomId);
        for (Booking bk : bookings) {
            defaultTableModel.addRow(new Object[]{bk.getMaHD(), bk.getRoomId(),bk.getCustomer().getTenKhachHang(), bk.getCheckInDate(), bk.getCheckOutDate(), bk.getTongTien(),bk.getStatus()});
        }
    }
    private void resetForm() {
        Tenkh.setText("");
        CCCD.setText("");
        jTextField6.setText("");
        jComboBox1.setSelectedIndex(0); // Đặt lại về giá trị mặc định (Nam)
        jTextPane5.setText("");
        jTextPane8.setText(""); // Check-in
        jTextPane9.setText(""); // Check-out
        jTextPane11.setText(""); // Tổng thời gian
        jTextPane1.setText(""); // Mã HĐ
        TrangThaiHD.setText("Chưa đặt");
        TrangThaiHD.setBackground(Color.GRAY);
        otienphongjTextPane2.setText(""); // Tiền phòng
        otiendichvujTextPane3.setText(""); // Tiền dịch vụ
        tongtienjTextPane4.setText(""); // Tổng tiền
        TienThoiLaikhacjTextPane2.setText("");
        TienkhactrajTextField1.setText("");
        bookingId = -1;
        isCheckedIn = false;
        isRoomCheckedOut = false;

        // Reset bảng dịch vụ đã sử dụng
        DefaultTableModel model = (DefaultTableModel) jTable3.getModel();
        model.setRowCount(0);
    }
    private  void setDisForm(){
        
        Tenkh.setEnabled(false);
        CCCD.setEnabled(false);
        jTextField6.setEnabled(false);
        jComboBox1.setEnabled(false); // Đặt lại về giá trị mặc định (Nam)
        jTextPane5.setEnabled(false);
        jTextPane8.setEnabled(false);// Check-in
        jTextPane9.setEnabled(false);// Check-out
        jTextPane11.setEnabled(false); // Tổng thời gian
        jTextPane1.setEnabled(false); // Mã HĐ
        TrangThaiHD.setEnabled(false);
       
    }
    private void setOpeForm(){
         Tenkh.setEnabled(true);
        CCCD.setEnabled(true);
        jTextField6.setEnabled(true);
        jComboBox1.setEnabled(true); // Đặt lại về giá trị mặc định (Nam)
        jTextPane5.setEnabled(true);
       
      
    }
            
    private void loadServicesToTable(List<Service> services) {
        DefaultTableModel model = (DefaultTableModel) jTable2.getModel();
        model.setRowCount(0); // Xóa dữ liệu cũ
        for (Service service : services) {
            model.addRow(new Object[]{
                service.getServiceID(),
                service.getServiceName(),
                String.format("%,.0f VND", service.getServicePrice()),
                service.getQuantityInStock() // Hiển thị số lượng còn lại
            });
        }
    }

    // Getter cho các thành phần giao diện
    public javax.swing.JTextPane getJTextPane5() {
        return jTextPane5;
    }

    public javax.swing.JTextField getJTextField4() {
        return jTextField4;
    }

    public JLabel getTenphongjLabel1() {
        return TenphongjLabel1;
    }

    public javax.swing.JTextField getJTextField2() {
        return jTextField2;
    }

    public javax.swing.JTextField getJTextField7() {
        return jTextField7;
    }

    public javax.swing.JTextField getJTextField10() {
        return jTextField10;
    }

    public javax.swing.JTextField getJTextField12() {
        return jTextField12;
    }

    public javax.swing.JTextField getJTextField13() {
        return jTextField13;
    }

    public javax.swing.JTextField getJTextField8() {
        return jTextField8;
    }

    public javax.swing.JTextPane getTongtienjTextPane4() {
        return tongtienjTextPane4;
    }

    public javax.swing.JLabel getGiaphongjLabel2() {
        return GiaphongjLabel2;
    }

    public JLabel getHoadonTongTien() {
        return jLabel24;
    }

    public JTable getJTable4() {
        return jTable4;
    }

    public JDialog getJDialog1() {
        return jDialog1;
    }

    public String getTenPhong() {
        return TenphongjLabel1.getText();
    }

    public JTable getJTable2() {
        return jTable2;
    }

    public JTable getJTable3() {
        return jTable3;
    }

    public int getRoomId() {
        return roomId;
    }

    public JSpinner getJSpinner2() {
        return jSpinner2;
    }

    public javax.swing.JTextPane getJTextPane1() {
        return jTextPane1;
    }

    public javax.swing.JTextPane getJTextPane8() {
        return jTextPane8;
    }

    public javax.swing.JTextPane getJTextPane9() {
        return jTextPane9;
    }

    public javax.swing.JTextPane getJTextPane11() {
        return jTextPane11;
    }

    public javax.swing.JTextPane getJTextPane2() {
        return otienphongjTextPane2; // Tiền phòng
    }

    public javax.swing.JTextPane getJTextPane3() {
        return otiendichvujTextPane3; // Tiền dịch vụ
    }

    public javax.swing.JTextPane getJTextPane4() {
        return tongtienjTextPane4; // Tổng tiền
    }

    public javax.swing.JTextPane getTienThoiLaikhacjTextPane2() {
        return TienThoiLaikhacjTextPane2;
    }

    public javax.swing.JTextPane getTrangThaiHD() {
        return TrangThaiHD;
    }

    public Home getHome() {
        return home;
    }

    public javax.swing.JButton getCheckinjButton5() {
        return CheckinjButton5;
    }

    public javax.swing.JButton getJButton18() {
        return checkout;
    }

    public javax.swing.JButton getTinhTienjButton7() {
        return TinhTienjButton7;
    }

    public javax.swing.JTextField getTienkhactrajTextField1() {
        return TienkhactrajTextField1;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    public void setTenphong(String str) {
        this.TenphongjLabel1.setText(str);
    }

    // Phương thức tổng quát cho mọi class kế thừa JFrame

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jDialog1 = new javax.swing.JDialog();
        jTextField4 = new javax.swing.JTextField();
        jTextField7 = new javax.swing.JTextField();
        jTextField8 = new javax.swing.JTextField();
        jTextField10 = new javax.swing.JTextField();
        jTextField12 = new javax.swing.JTextField();
        jTextField13 = new javax.swing.JTextField();
        jLabel21 = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        jLabel23 = new javax.swing.JLabel();
        jLabel26 = new javax.swing.JLabel();
        jLabel28 = new javax.swing.JLabel();
        jLabel31 = new javax.swing.JLabel();
        jScrollPane6 = new javax.swing.JScrollPane();
        jTable4 = new javax.swing.JTable();
        jLabel34 = new javax.swing.JLabel();
        jLabel35 = new javax.swing.JLabel();
        jLabel37 = new javax.swing.JLabel();
        jLabel40 = new javax.swing.JLabel();
        jTextField2 = new javax.swing.JTextField();
        jLabel42 = new javax.swing.JLabel();
        jLabel24 = new javax.swing.JLabel();
        jLabel27 = new javax.swing.JLabel();
        jLabel30 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        TenphongjLabel1 = new javax.swing.JLabel();
        GiaphongjLabel2 = new javax.swing.JLabel();
        cccdjPanel5 = new javax.swing.JPanel();
        jScrollPane8 = new javax.swing.JScrollPane();
        jTextPane8 = new javax.swing.JTextPane();
        jScrollPane9 = new javax.swing.JScrollPane();
        jTextPane9 = new javax.swing.JTextPane();
        jScrollPane5 = new javax.swing.JScrollPane();
        jTextPane5 = new javax.swing.JTextPane();
        jScrollPane10 = new javax.swing.JScrollPane();
        TrangThaiHD = new javax.swing.JTextPane();
        jScrollPane11 = new javax.swing.JScrollPane();
        jTextPane11 = new javax.swing.JTextPane();
        jLabel6 = new javax.swing.JLabel();
        checkinjLabel11 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        cojLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        tenjLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        songuoijLabel8 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        Tenkh = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        CCCD = new javax.swing.JTextField();
        sdtjLabel2 = new javax.swing.JLabel();
        jTextField6 = new javax.swing.JTextField();
        jComboBox1 = new javax.swing.JComboBox<>();
        gtjLabel9 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextPane1 = new javax.swing.JTextPane();
        jScrollPane2 = new javax.swing.JScrollPane();
        TenNhanvien = new javax.swing.JTextPane();
        jPanel6 = new javax.swing.JPanel();
        jButton2 = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        jLabel16 = new javax.swing.JLabel();
        ThemdichvujButton11 = new javax.swing.JButton();
        timkiemdichvujTextField2 = new javax.swing.JTextField();
        jPanel10 = new javax.swing.JPanel();
        jLabel38 = new javax.swing.JLabel();
        jScrollPane21 = new javax.swing.JScrollPane();
        jTable2 = new javax.swing.JTable();
        TimkiemjButton5 = new javax.swing.JButton();
        jLabel55 = new javax.swing.JLabel();
        jSpinner2 = new javax.swing.JSpinner();
        loadlaiCTDVjButton3 = new javax.swing.JButton();
        jScrollPane4 = new javax.swing.JScrollPane();
        jTable3 = new javax.swing.JTable();
        jLabel41 = new javax.swing.JLabel();
        backjButton9 = new javax.swing.JButton();
        jScrollPane20 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        CheckinjButton5 = new javax.swing.JButton();
        Capnhat = new javax.swing.JButton();
        jButton8 = new javax.swing.JButton();
        checkout = new javax.swing.JButton();
        TienKhachtra = new javax.swing.JLabel();
        TinhTienjButton7 = new javax.swing.JButton();
        InhoadonjButton4 = new javax.swing.JButton();
        jLabel10 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        TienkhactrajTextField1 = new javax.swing.JTextField();
        jScrollPane3 = new javax.swing.JScrollPane();
        TienThoiLaikhacjTextPane2 = new javax.swing.JTextPane();
        jScrollPane13 = new javax.swing.JScrollPane();
        otienphongjTextPane2 = new javax.swing.JTextPane();
        jScrollPane14 = new javax.swing.JScrollPane();
        otiendichvujTextPane3 = new javax.swing.JTextPane();
        jScrollPane15 = new javax.swing.JScrollPane();
        tongtienjTextPane4 = new javax.swing.JTextPane();
        TinhtienthoilaijButton1 = new javax.swing.JButton();

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 414, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );

        jTextField4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField4ActionPerformed(evt);
            }
        });

        jTextField8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField8ActionPerformed(evt);
            }
        });

        jTextField10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField10ActionPerformed(evt);
            }
        });

        jTextField12.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField12ActionPerformed(evt);
            }
        });

        jTextField13.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField13ActionPerformed(evt);
            }
        });

        jLabel21.setText("Tên Khách Hàng :");

        jLabel22.setText("Ngày Đặt Phòng :");

        jLabel23.setText("Ngày Trả Phòng :");

        jLabel26.setText("___________________________________________________________________________________________________________________________________________________");

        jLabel28.setText("Phòng :");

        jLabel31.setText("Số Ngày Lưu Trú");

        jTable4.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "STT", "Dịch vụ", "Số Lượng", "Đơn Gía", "Thành Tiền"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        jScrollPane6.setViewportView(jTable4);

        jLabel34.setText("Địa chỉ:20 hàng bè-tây hồ- Hà Nội");

        jLabel35.setBackground(new java.awt.Color(0, 255, 51));
        jLabel35.setFont(new java.awt.Font("Segoe UI Black", 0, 18)); // NOI18N
        jLabel35.setForeground(new java.awt.Color(255, 255, 255));
        jLabel35.setText("GREEN LEAVES HOTEL");
        jLabel35.setOpaque(true);

        jLabel37.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel37.setText("ID Hóa Đơn :");

        jLabel40.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel40.setText("Mã Phòng :");

        jLabel42.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel42.setText("Tổng Tiền");

        jLabel24.setFont(new java.awt.Font("Segoe UI Black", 2, 14)); // NOI18N
        jLabel24.setText("100.000");

        jLabel27.setFont(new java.awt.Font("Segoe UI Black", 1, 14)); // NOI18N
        jLabel27.setText("VND");

        jLabel30.setFont(new java.awt.Font("Segoe UI Black", 1, 36)); // NOI18N
        jLabel30.setText("HÓA ĐƠN");

        javax.swing.GroupLayout jDialog1Layout = new javax.swing.GroupLayout(jDialog1.getContentPane());
        jDialog1.getContentPane().setLayout(jDialog1Layout);
        jDialog1Layout.setHorizontalGroup(
            jDialog1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jDialog1Layout.createSequentialGroup()
                .addGroup(jDialog1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jDialog1Layout.createSequentialGroup()
                        .addGap(100, 100, 100)
                        .addGroup(jDialog1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel37)
                            .addComponent(jLabel21)
                            .addComponent(jLabel23, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel22, javax.swing.GroupLayout.Alignment.TRAILING))
                        .addGap(37, 37, 37)
                        .addGroup(jDialog1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jTextField13, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jDialog1Layout.createSequentialGroup()
                                .addGroup(jDialog1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jTextField8, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(jDialog1Layout.createSequentialGroup()
                                        .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(136, 136, 136)
                                        .addGroup(jDialog1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabel40)
                                            .addComponent(jLabel31)))
                                    .addComponent(jTextField7, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(jDialog1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jDialog1Layout.createSequentialGroup()
                                        .addGap(56, 56, 56)
                                        .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jDialog1Layout.createSequentialGroup()
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jTextField12, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE))))
                            .addComponent(jTextField10, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jDialog1Layout.createSequentialGroup()
                        .addGap(89, 89, 89)
                        .addGroup(jDialog1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 744, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jDialog1Layout.createSequentialGroup()
                                .addComponent(jLabel42, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jLabel24)
                                .addGap(56, 56, 56)
                                .addComponent(jLabel27))))
                    .addGroup(jDialog1Layout.createSequentialGroup()
                        .addGap(339, 339, 339)
                        .addComponent(jLabel30)))
                .addContainerGap(20, Short.MAX_VALUE))
            .addGroup(jDialog1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jDialog1Layout.createSequentialGroup()
                    .addGroup(jDialog1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jDialog1Layout.createSequentialGroup()
                            .addGap(16, 16, 16)
                            .addGroup(jDialog1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(jDialog1Layout.createSequentialGroup()
                                    .addGap(6, 6, 6)
                                    .addComponent(jLabel34))
                                .addComponent(jLabel35)))
                        .addGroup(jDialog1Layout.createSequentialGroup()
                            .addGap(94, 94, 94)
                            .addGroup(jDialog1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(jDialog1Layout.createSequentialGroup()
                                    .addGap(378, 378, 378)
                                    .addComponent(jLabel28))
                                .addComponent(jLabel26))))
                    .addContainerGap(24, Short.MAX_VALUE)))
        );
        jDialog1Layout.setVerticalGroup(
            jDialog1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jDialog1Layout.createSequentialGroup()
                .addGap(65, 65, 65)
                .addComponent(jLabel30)
                .addGap(34, 34, 34)
                .addGroup(jDialog1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel37)
                    .addComponent(jLabel40)
                    .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(32, 32, 32)
                .addGroup(jDialog1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel21))
                .addGap(18, 18, 18)
                .addGroup(jDialog1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel23)
                    .addComponent(jTextField7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jDialog1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel31)
                    .addComponent(jTextField12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jDialog1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel22)
                    .addComponent(jTextField10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(56, 56, 56)
                .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 225, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jDialog1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel42)
                    .addComponent(jLabel24)
                    .addComponent(jLabel27))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(jDialog1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jDialog1Layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(jLabel35)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                    .addComponent(jLabel34)
                    .addGap(152, 152, 152)
                    .addComponent(jLabel28)
                    .addGap(121, 121, 121)
                    .addComponent(jLabel26, javax.swing.GroupLayout.PREFERRED_SIZE, 13, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(284, Short.MAX_VALUE)))
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);

        jPanel2.setBackground(new java.awt.Color(0, 51, 102));

        TenphongjLabel1.setFont(new java.awt.Font("sansserif", 1, 18)); // NOI18N
        TenphongjLabel1.setForeground(new java.awt.Color(255, 255, 51));
        TenphongjLabel1.setText("PHÒNG 101 ");

        GiaphongjLabel2.setFont(new java.awt.Font("sansserif", 1, 18)); // NOI18N
        GiaphongjLabel2.setForeground(new java.awt.Color(204, 51, 0));
        GiaphongjLabel2.setText("|  Giá Phòng 450.000d");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(489, 489, 489)
                .addComponent(TenphongjLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 235, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(GiaphongjLabel2)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(TenphongjLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(GiaphongjLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(16, Short.MAX_VALUE))
        );

        jScrollPane8.setViewportView(jTextPane8);

        jScrollPane9.setViewportView(jTextPane9);

        jScrollPane5.setViewportView(jTextPane5);

        jScrollPane10.setViewportView(TrangThaiHD);

        jScrollPane11.setViewportView(jTextPane11);

        jLabel6.setText("Mã HĐ");

        checkinjLabel11.setText("Check-In");

        jLabel15.setText("Ngày");

        cojLabel12.setText("Check-Out");

        jLabel13.setText("Tổng thời gian");

        tenjLabel4.setText("Tên Khách Hàng ");

        jLabel5.setText("Nhân VIên");

        jLabel14.setText("Trạng thái HĐ");

        songuoijLabel8.setText("Số Người ở");

        jPanel3.setBackground(new java.awt.Color(0, 51, 102));

        jLabel3.setFont(new java.awt.Font("sansserif", 1, 14)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("Chi Tiết Đặt Phòng");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 192, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, 40, Short.MAX_VALUE)
                .addContainerGap())
        );

        Tenkh.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                TenkhActionPerformed(evt);
            }
        });

        jLabel1.setText("CCCD");

        sdtjLabel2.setText("Số Điện Thoại");

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Nam", "Nữ" }));
        jComboBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox1ActionPerformed(evt);
            }
        });

        gtjLabel9.setText("Giới Tính");

        jScrollPane1.setViewportView(jTextPane1);

        TenNhanvien.setBackground(new java.awt.Color(204, 204, 204));
        TenNhanvien.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        jScrollPane2.setViewportView(TenNhanvien);

        javax.swing.GroupLayout cccdjPanel5Layout = new javax.swing.GroupLayout(cccdjPanel5);
        cccdjPanel5.setLayout(cccdjPanel5Layout);
        cccdjPanel5Layout.setHorizontalGroup(
            cccdjPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(cccdjPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(cccdjPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(cccdjPanel5Layout.createSequentialGroup()
                        .addGroup(cccdjPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(tenjLabel4)
                            .addComponent(jLabel1)
                            .addComponent(sdtjLabel2)
                            .addComponent(gtjLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(cccdjPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(cccdjPanel5Layout.createSequentialGroup()
                                .addGroup(cccdjPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(CCCD, javax.swing.GroupLayout.PREFERRED_SIZE, 173, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(cccdjPanel5Layout.createSequentialGroup()
                                        .addComponent(Tenkh, javax.swing.GroupLayout.PREFERRED_SIZE, 173, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18)
                                        .addComponent(songuoijLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(cccdjPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 173, Short.MAX_VALUE)
                                .addComponent(jTextField6, javax.swing.GroupLayout.Alignment.LEADING))))
                    .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(36, 36, 36)
                .addGroup(cccdjPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(cojLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(checkinjLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel13)
                    .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(34, 34, 34)
                .addGroup(cccdjPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(cccdjPanel5Layout.createSequentialGroup()
                        .addGroup(cccdjPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jScrollPane9, javax.swing.GroupLayout.PREFERRED_SIZE, 154, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jScrollPane8, javax.swing.GroupLayout.PREFERRED_SIZE, 154, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, cccdjPanel5Layout.createSequentialGroup()
                        .addGroup(cccdjPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jScrollPane10, javax.swing.GroupLayout.DEFAULT_SIZE, 152, Short.MAX_VALUE)
                            .addComponent(jScrollPane11)
                            .addComponent(jScrollPane1))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel15)
                        .addGap(150, 150, 150))))
            .addGroup(cccdjPanel5Layout.createSequentialGroup()
                .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        cccdjPanel5Layout.setVerticalGroup(
            cccdjPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, cccdjPanel5Layout.createSequentialGroup()
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 18, Short.MAX_VALUE)
                .addGroup(cccdjPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane5, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(tenjLabel4, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(Tenkh, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(songuoijLabel8, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(checkinjLabel11, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane8, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(cccdjPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(cccdjPanel5Layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(jLabel1)
                        .addGap(18, 18, 18)
                        .addGroup(cccdjPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(sdtjLabel2)
                            .addComponent(jTextField6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(cccdjPanel5Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(CCCD, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(52, 52, 52)
                        .addGroup(cccdjPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(gtjLabel9)
                            .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, cccdjPanel5Layout.createSequentialGroup()
                        .addGroup(cccdjPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(cccdjPanel5Layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 18, Short.MAX_VALUE)
                                .addComponent(jScrollPane9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(12, 12, 12)
                                .addComponent(jScrollPane11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(cccdjPanel5Layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(cojLabel12)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(cccdjPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel15)
                                    .addComponent(jLabel13))))
                        .addGap(18, 18, 18)
                        .addGroup(cccdjPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel14)
                            .addComponent(jScrollPane10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(6, 6, 6)))
                .addGroup(cccdjPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(cccdjPanel5Layout.createSequentialGroup()
                        .addGap(1, 1, 1)
                        .addGroup(cccdjPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel5)
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(30, 30, 30))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, cccdjPanel5Layout.createSequentialGroup()
                        .addGroup(cccdjPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel6))
                        .addGap(14, 14, 14))))
        );

        jButton2.setFont(new java.awt.Font("sansserif", 1, 14)); // NOI18N
        jButton2.setForeground(new java.awt.Color(0, 102, 102));
        jButton2.setText("QUY ĐỊNH VỀ CHECK_IN CHECK_OUT");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jPanel4.setBackground(new java.awt.Color(0, 51, 102));

        jLabel16.setFont(new java.awt.Font("sansserif", 1, 14)); // NOI18N
        jLabel16.setForeground(new java.awt.Color(255, 255, 255));
        jLabel16.setText("QUY ĐỊNH");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(126, 126, 126))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        ThemdichvujButton11.setText("Thêm  Dịch Vụ");
        ThemdichvujButton11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ThemdichvujButton11ActionPerformed(evt);
            }
        });

        jPanel10.setBackground(new java.awt.Color(0, 51, 102));

        jLabel38.setFont(new java.awt.Font("sansserif", 1, 14)); // NOI18N
        jLabel38.setForeground(new java.awt.Color(255, 255, 255));
        jLabel38.setText("Chi Tiết Dịch Vụ");

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel38, javax.swing.GroupLayout.PREFERRED_SIZE, 211, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(256, Short.MAX_VALUE))
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel38, javax.swing.GroupLayout.DEFAULT_SIZE, 28, Short.MAX_VALUE)
                .addContainerGap())
        );

        jTable2.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Mã DV", "Tên DV", "Giá DV", "Số lượng "
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Object.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        jScrollPane21.setViewportView(jTable2);

        TimkiemjButton5.setBackground(new java.awt.Color(0, 51, 102));
        TimkiemjButton5.setForeground(new java.awt.Color(255, 255, 255));
        TimkiemjButton5.setText("Tìm Kiếm");
        TimkiemjButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                TimkiemjButton5ActionPerformed(evt);
            }
        });

        jLabel55.setText("Chọn Số Lượng");

        loadlaiCTDVjButton3.setText("Load loại CTDV");
        loadlaiCTDVjButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                loadlaiCTDVjButton3ActionPerformed(evt);
            }
        });

        jTable3.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Mã Hóa Đơn", "Tên Khách hàng", "Tên dịch vụ", "Số lần sử dụng"
            }
        ));
        jScrollPane4.setViewportView(jTable3);

        jLabel41.setText("Tìm Kiếm DV");

        backjButton9.setText("Quay Lại");
        backjButton9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                backjButton9ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane21)
                    .addComponent(jScrollPane4, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 361, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jPanel10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel6Layout.createSequentialGroup()
                                .addComponent(jLabel41, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(timkiemdichvujTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, 285, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(TimkiemjButton5))
                            .addGroup(jPanel6Layout.createSequentialGroup()
                                .addGap(6, 6, 6)
                                .addComponent(jLabel55, javax.swing.GroupLayout.PREFERRED_SIZE, 94, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jSpinner2, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(30, 30, 30)
                                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(ThemdichvujButton11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(loadlaiCTDVjButton3, javax.swing.GroupLayout.DEFAULT_SIZE, 189, Short.MAX_VALUE))))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(backjButton9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jButton2)
                .addGap(22, 22, 22)
                .addComponent(jPanel10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(timkiemdichvujTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(TimkiemjButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel41))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane21, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel55, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jSpinner2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(loadlaiCTDVjButton3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(ThemdichvujButton11)
                .addGap(42, 42, 42)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(backjButton9, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null}
            },
            new String [] {
                "Mã Hóa Đơn ", "Tên phòng", "Tên Khách Hàng", "Tên Nhân Viên", "Check-In ", "Check-Out", "Tổng Thời Gian", "Trạng Thái Hóa Đơn"
            }
        ));
        jTable1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable1MouseClicked(evt);
            }
        });
        jScrollPane20.setViewportView(jTable1);

        CheckinjButton5.setText("Check-in");
        CheckinjButton5.setToolTipText("");
        CheckinjButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CheckinjButton5ActionPerformed(evt);
            }
        });

        Capnhat.setText("Cập Nhật");
        Capnhat.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CapnhatActionPerformed(evt);
            }
        });

        jButton8.setText("Đổi phòng");
        jButton8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton8ActionPerformed(evt);
            }
        });

        checkout.setBackground(new java.awt.Color(0, 51, 102));
        checkout.setForeground(new java.awt.Color(255, 255, 255));
        checkout.setText("Check-out");
        checkout.setToolTipText("");
        checkout.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                checkoutActionPerformed(evt);
            }
        });

        TienKhachtra.setText("Tền khách trả ");

        TinhTienjButton7.setBackground(new java.awt.Color(0, 51, 102));
        TinhTienjButton7.setForeground(new java.awt.Color(255, 255, 255));
        TinhTienjButton7.setText("Tính Tiền");
        TinhTienjButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                TinhTienjButton7ActionPerformed(evt);
            }
        });

        InhoadonjButton4.setBackground(new java.awt.Color(0, 51, 102));
        InhoadonjButton4.setForeground(new java.awt.Color(255, 255, 255));
        InhoadonjButton4.setText("In Hóa Đơn");
        InhoadonjButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                InhoadonjButton4ActionPerformed(evt);
            }
        });

        jLabel10.setText("Tiền Phòng");

        jLabel17.setText("Tiền Dịch Vụ");

        jLabel18.setText("Tổng tiền phòng + Dv");

        jLabel7.setText("Tiền thối lại khach");

        TienkhactrajTextField1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                TienkhactrajTextField1ActionPerformed(evt);
            }
        });

        TienThoiLaikhacjTextPane2.setBackground(new java.awt.Color(204, 204, 204));
        jScrollPane3.setViewportView(TienThoiLaikhacjTextPane2);

        otienphongjTextPane2.setBackground(new java.awt.Color(204, 204, 204));
        jScrollPane13.setViewportView(otienphongjTextPane2);

        otiendichvujTextPane3.setBackground(new java.awt.Color(204, 204, 204));
        jScrollPane14.setViewportView(otiendichvujTextPane3);

        tongtienjTextPane4.setBackground(new java.awt.Color(204, 204, 204));
        jScrollPane15.setViewportView(tongtienjTextPane4);

        TinhtienthoilaijButton1.setBackground(new java.awt.Color(0, 51, 102));
        TinhtienthoilaijButton1.setForeground(new java.awt.Color(255, 255, 255));
        TinhtienthoilaijButton1.setText("Tính Tiền Thối Lại");
        TinhtienthoilaijButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                TinhtienthoilaijButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(19, 19, 19)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addGroup(layout.createSequentialGroup()
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(layout.createSequentialGroup()
                                                .addComponent(CheckinjButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 139, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(59, 59, 59)
                                                .addComponent(Capnhat, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE))
                                            .addGroup(layout.createSequentialGroup()
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                    .addComponent(jLabel10)
                                                    .addComponent(jLabel17))
                                                .addGap(244, 244, 244)
                                                .addComponent(TienKhachtra)))
                                        .addGap(22, 22, 22))
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jLabel18)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jScrollPane15, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jLabel7)))
                                .addGap(8, 8, 8)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jButton8, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(0, 0, Short.MAX_VALUE))
                                    .addGroup(layout.createSequentialGroup()
                                        .addGap(4, 4, 4)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(TienkhactrajTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 158, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                                .addComponent(TinhTienjButton7, javax.swing.GroupLayout.PREFERRED_SIZE, 164, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(195, 195, 195))
                                            .addComponent(InhoadonjButton4, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 359, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(TinhtienthoilaijButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 164, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                            .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(checkout, javax.swing.GroupLayout.PREFERRED_SIZE, 151, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jScrollPane20, javax.swing.GroupLayout.PREFERRED_SIZE, 989, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(141, 141, 141)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jScrollPane13, javax.swing.GroupLayout.PREFERRED_SIZE, 151, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jScrollPane14, javax.swing.GroupLayout.PREFERRED_SIZE, 151, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addGap(0, 7, Short.MAX_VALUE)
                                .addComponent(cccdjPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(18, 18, 18)
                        .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(cccdjPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane20, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(CheckinjButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(Capnhat, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(checkout, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(TinhTienjButton7, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jButton8, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel10)
                                .addGap(18, 18, 18)
                                .addComponent(jLabel17))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jScrollPane13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jScrollPane14, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jScrollPane15, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(TienKhachtra, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(TienkhactrajTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(layout.createSequentialGroup()
                                        .addGap(14, 14, 14)
                                        .addComponent(TinhtienthoilaijButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jLabel7, javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addComponent(jLabel18))
                                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(InhoadonjButton4))))))
                .addGap(87, 87, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void checkoutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_checkoutActionPerformed
        // TODO add your handling code here:
        if (!isCheckedIn || bookingId == -1) {
            JOptionPane.showMessageDialog(this, "Phòng chưa được check-in hoặc không tìm thấy hóa đơn!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        controller.checkOut();
        isRoomCheckedOut = true; // Đánh dấu phòng đã check-out
        JOptionPane.showMessageDialog(this, "Check-out thành công!");
    }//GEN-LAST:event_checkoutActionPerformed

    private void CheckinjButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CheckinjButton5ActionPerformed
        // TODO add your handling code here:

        if (isCheckedIn) {
            JOptionPane.showMessageDialog(this, "Phòng đã được check-in!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        String tenKhachHang = Tenkh.getText();
        String cccd = CCCD.getText();
        String soDienThoai = jTextField6.getText();
        String gioiTinh = (String) jComboBox1.getSelectedItem();
        String soNguoiO = jTextPane5.getText();
       

        if (tenKhachHang.isEmpty() || cccd.isEmpty() || soDienThoai.isEmpty() || soNguoiO.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ thông tin khách hàng!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Customer customer = new Customer();
        customer.setTenKhachHang(tenKhachHang);
        customer.setCccd(cccd);
        customer.setSoDienThoai(soDienThoai);
        customer.setGioiTinh(gioiTinh);

        try {
            // Tạo CustomerDAO để quản lý khách hàng
            CustomerDAO customerDAO = new CustomerDAO(JDBCConnection.getConnection());

            // Kiểm tra xem khách hàng đã tồn tại chưa
            if (!customerDAO.customerExists(cccd)) {
                // Nếu chưa tồn tại, thêm khách hàng vào bảng customers
                customerDAO.addCustomer(customer);
            }

            // Tiếp tục tạo hóa đơn
            BookingDAO bookingDAO = new BookingDAO(JDBCConnection.getConnection());
            Booking booking = new Booking();
            booking.setRoomId(roomId);
            booking.setCustomer(customer);
            booking.setUserId(currentUser.getUserID());
            Timestamp checkInDate = new Timestamp(System.currentTimeMillis());
            booking.setCheckInDate(checkInDate);
            booking.setStatus("Đang sử dụng");
            booking.setSoNguoiO(Integer.parseInt(soNguoiO));

            int generatedId = bookingDAO.createBooking(booking);
            bookingId = generatedId;
            isCheckedIn = true;
            this.controller = new ServiceNRoomController(this, bookingId, isCheckedIn);

            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            jTextPane8.setText(sdf.format(checkInDate));
            jTextPane1.setText(String.format("HD%04d", generatedId));
            booking.setMaHD(jTextPane1.getText());
            bookingDAO.updateBookingMaHD(bookingId,booking.getMaHD());

            RoomDAO roomDAO = new RoomDAO(JDBCConnection.getConnection());
            roomDAO.updateRoomStatus(roomId, "Đang sử dụng");

            TrangThaiHD.setText("Đang sử dụng");
            TrangThaiHD.setBackground(Color.GREEN);
            home.setPhongDaCheckIn(roomId);
            CheckinjButton5.setEnabled(false);
            setDisForm();
            checkout.setEnabled(true);

            controller.loadServiceUsages();
            
            JOptionPane.showMessageDialog(this, "Check-in thành công!");
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Số người ở phải là số nguyên!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Lỗi khi lưu thông tin: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }

    }//GEN-LAST:event_CheckinjButton5ActionPerformed

    private void jComboBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jComboBox1ActionPerformed

    private void TenkhActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TenkhActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_TenkhActionPerformed

    private void TimkiemjButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TimkiemjButton5ActionPerformed
        // TODO add your handling code here:
        String keyword = timkiemdichvujTextField2.getText().trim();
        controller.searchServices(keyword);
    }//GEN-LAST:event_TimkiemjButton5ActionPerformed

    private void ThemdichvujButton11ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ThemdichvujButton11ActionPerformed
        // TODO add your handling code here:
        int selectedRow = jTable2.getSelectedRow();
        int quantity = (Integer) jSpinner2.getValue();
        controller.addService(selectedRow, quantity);
        jSpinner2.setValue(0); // Reset spinner
    }//GEN-LAST:event_ThemdichvujButton11ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton2ActionPerformed

    private void loadlaiCTDVjButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_loadlaiCTDVjButton3ActionPerformed
        // TODO add your handling code here:
        controller.refreshServiceUsages();
    }//GEN-LAST:event_loadlaiCTDVjButton3ActionPerformed

    private void backjButton9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_backjButton9ActionPerformed
        // TODO add your handling code here:
        if (isRoomCheckedOut) {
            resetForm();
            controller = new ServiceNRoomController(this, -1, false); // Reset controller
        }
        this.dispose();
        home.setVisible(true);
    }//GEN-LAST:event_backjButton9ActionPerformed

    private void TinhTienjButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TinhTienjButton7ActionPerformed
        // TODO add your handling code here:
        if (!isCheckedIn || bookingId == -1) {
            JOptionPane.showMessageDialog(this, "Phòng chưa được check-in hoặc không tìm thấy hóa đơn!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        try {

            controller.calculateBill();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập số tiền khách trả hợp lệ!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_TinhTienjButton7ActionPerformed

    private void TienkhactrajTextField1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TienkhactrajTextField1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_TienkhactrajTextField1ActionPerformed

    private void TinhtienthoilaijButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TinhtienthoilaijButton1ActionPerformed
        // TODO add your handling code here:
        if (!isCheckedIn || bookingId == -1) {
            JOptionPane.showMessageDialog(this, "Phòng chưa được check-in hoặc không tìm thấy hóa đơn!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        try {
            double tienKhachTra = Double.parseDouble(TienkhactrajTextField1.getText().trim());
            controller.calculateTralai(tienKhachTra);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập số tiền khách trả hợp lệ!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }


    }//GEN-LAST:event_TinhtienthoilaijButton1ActionPerformed

    private void jTextField4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField4ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField4ActionPerformed

    private void jTextField10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField10ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField10ActionPerformed

    private void jTextField12ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField12ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField12ActionPerformed

    private void jTextField13ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField13ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField13ActionPerformed

    private void InhoadonjButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_InhoadonjButton4ActionPerformed
// TODO add your handling code here:
        // Kiểm tra trạng thái
        if (!isCheckedIn || bookingId == -1) {
            JOptionPane.showMessageDialog(this, "Phòng chưa được check-in hoặc không có hóa đơn!", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            // Lấy dữ liệu từ database
            BookingDAO bookingDAO = new BookingDAO(JDBCConnection.getConnection());
            RoomDAO roomDAO = new RoomDAO(JDBCConnection.getConnection());

            Booking booking = bookingDAO.getBookingById(bookingId);
            if (booking == null) {
                JOptionPane.showMessageDialog(this, "Không tìm thấy hóa đơn!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Room room = roomDAO.getRoomById(booking.getRoomId());
            if (room == null) {
                JOptionPane.showMessageDialog(this, "Không tìm thấy thông tin phòng!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Customer customer = booking.getCustomer();
            if (customer == null) {
                JOptionPane.showMessageDialog(this, "Không tìm thấy thông tin khách hàng!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Fill dữ liệu vào jDialog1
            // Lấy danh sách dịch vụ đã sử dụng
            // Tạo PDF trực tiếp
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setSelectedFile(new File("HD_" + String.format("%04d", booking.getId()) + ".pdf"));
            int userSelection = fileChooser.showSaveDialog(null);

            if (userSelection == JFileChooser.APPROVE_OPTION) {
                String filePath = fileChooser.getSelectedFile().getAbsolutePath();
                if (!filePath.toLowerCase().endsWith(".pdf")) {
                    filePath += ".pdf";
                }
                controller.createInvoicePDF(filePath, booking, room, customer);
            }
            // Hiển thị jDialog1 tạm thời để lấy kích thước đúng
            if (!getJDialog1().isDisplayable()) {
                getJDialog1().pack();
                getJDialog1().setVisible(true);
                getJDialog1().setVisible(false);
            }

//            // Xuất PDF
//            String fileName = "invoice_HD" + booking.getId() + ".pdf";
//            controller.exportComponentToPDF(getJDialog1().getContentPane(), fileName);
//            JOptionPane.showMessageDialog(this, "Xuất hóa đơn PDF thành công!");

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi khi in hóa đơn: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }

    }//GEN-LAST:event_InhoadonjButton4ActionPerformed

    private void jTable1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable1MouseClicked
        // TODO add your handling code here:

    }//GEN-LAST:event_jTable1MouseClicked

    private void jTextField8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField8ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField8ActionPerformed

    private void CapnhatActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CapnhatActionPerformed
        // TODO add your handling code here:
    
  setOpeForm(); // Bật các trường để chỉnh sửa
    String tenKhachHang = Tenkh.getText();
    String cccd = CCCD.getText();
    String soDienThoai = jTextField6.getText();
    String gioiTinh = (String) jComboBox1.getSelectedItem();
    String soNguoiO = jTextPane5.getText();

    if (tenKhachHang.isEmpty() || cccd.isEmpty() || soDienThoai.isEmpty() || soNguoiO.isEmpty()) {
        JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ thông tin!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        return;
    }

    try {
        // Tạo đối tượng Customer mới với thông tin cập nhật
        Customer updatedCustomer = new Customer();
        updatedCustomer.setTenKhachHang(tenKhachHang);
        updatedCustomer.setCccd(cccd);
        updatedCustomer.setSoDienThoai(soDienThoai);
        updatedCustomer.setGioiTinh(gioiTinh);

        BookingDAO bookingDAO = new BookingDAO(JDBCConnection.getConnection());
        CustomerDAO customerDAO = new CustomerDAO(JDBCConnection.getConnection());
        Booking booking = bookingDAO.getBookingById(bookingId);
        
        if (booking != null) {
            // Cập nhật thông tin khách hàng và số người ở trong bookings
            booking.setCustomer(updatedCustomer);
            booking.setSoNguoiO(Integer.parseInt(soNguoiO));
            bookingDAO.updateCustomerInfo(bookingId, updatedCustomer, Integer.parseInt(soNguoiO));

            // Cập nhật thông tin khách hàng trong customers
            
            customerDAO.updateCustomer(updatedCustomer);

            // Cập nhật giao diện
            Tenkh.setText(tenKhachHang);
            CCCD.setText(cccd);
            jTextField6.setText(soDienThoai);
            jComboBox1.setSelectedItem(gioiTinh);
            jTextPane5.setText(soNguoiO);
            
            setDisForm(); // Tắt các trường sau khi cập nhật
            JOptionPane.showMessageDialog(this, "Cập nhật thông tin thành công!", "Thành công", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "Không tìm thấy thông tin đặt phòng!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    } catch (NumberFormatException e) {
        JOptionPane.showMessageDialog(this, "Số người ở phải là số nguyên!", "Lỗi", JOptionPane.ERROR_MESSAGE);
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(this, "Lỗi khi cập nhật thông tin: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        e.printStackTrace();
    }
        
     loadtb();
          
            
           
        
    }//GEN-LAST:event_CapnhatActionPerformed

    private void jButton8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton8ActionPerformed
        // TODO add your handling code here:
        if (!isCheckedIn || bookingId == -1) {
        JOptionPane.showMessageDialog(this, "Phòng chưa được check-in hoặc không tìm thấy hóa đơn!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
        return;
    }

    // Mở cửa sổ chọn phòng mới
    RoomSelectionDialog roomSelectionDialog = new RoomSelectionDialog(this, roomId);
    roomSelectionDialog.setVisible(true);

    // Lấy phòng mới từ dialog (nếu người dùng chọn)
    int newRoomId = roomSelectionDialog.getSelectedRoomId();
    if (newRoomId != -1) {
        try {
            // Thực hiện đổi phòng
            BookingDAO bookingDAO = new BookingDAO(JDBCConnection.getConnection());
            RoomDAO roomDAO = new RoomDAO(JDBCConnection.getConnection());

            // Lấy thông tin booking hiện tại
            Booking currentBooking = bookingDAO.getBookingById(bookingId);
            if (currentBooking != null) {
                // Cập nhật booking với roomId mới
                currentBooking.setRoomId(newRoomId);
                bookingDAO.updateRoomId(bookingId, newRoomId);

                // Cập nhật trạng thái phòng
                roomDAO.updateRoomStatus(roomId, "Trống"); // Phòng cũ thành trống
                roomDAO.updateRoomStatus(newRoomId, "Đang sử dụng"); // Phòng mới thành đang sử dụng

                // Cập nhật giao diện
                home.setPhongDaCheckOut(roomId); // Cập nhật phòng cũ
                home.setPhongDaCheckIn(newRoomId); // Cập nhật phòng mới
                roomId = newRoomId; // Cập nhật roomId hiện tại
                TenphongjLabel1.setText("PHÒNG " + roomId); // Cập nhật tên phòng trên giao diện
                JOptionPane.showMessageDialog(this, "Đổi phòng thành công sang Phòng " + newRoomId + "!", "Thành công", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Không tìm thấy thông tin booking!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Lỗi khi đổi phòng: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
    }//GEN-LAST:event_jButton8ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(ServiceNRoom.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ServiceNRoom.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ServiceNRoom.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ServiceNRoom.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ServiceNRoom().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField CCCD;
    private javax.swing.JButton Capnhat;
    private javax.swing.JButton CheckinjButton5;
    private javax.swing.JLabel GiaphongjLabel2;
    private javax.swing.JButton InhoadonjButton4;
    private javax.swing.JTextPane TenNhanvien;
    private javax.swing.JTextField Tenkh;
    private javax.swing.JLabel TenphongjLabel1;
    private javax.swing.JButton ThemdichvujButton11;
    private javax.swing.JLabel TienKhachtra;
    private javax.swing.JTextPane TienThoiLaikhacjTextPane2;
    private javax.swing.JTextField TienkhactrajTextField1;
    private javax.swing.JButton TimkiemjButton5;
    private javax.swing.JButton TinhTienjButton7;
    private javax.swing.JButton TinhtienthoilaijButton1;
    private javax.swing.JTextPane TrangThaiHD;
    private javax.swing.JButton backjButton9;
    private javax.swing.JPanel cccdjPanel5;
    private javax.swing.JLabel checkinjLabel11;
    private javax.swing.JButton checkout;
    private javax.swing.JLabel cojLabel12;
    private javax.swing.JLabel gtjLabel9;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton8;
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JDialog jDialog1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel37;
    private javax.swing.JLabel jLabel38;
    private javax.swing.JLabel jLabel40;
    private javax.swing.JLabel jLabel41;
    private javax.swing.JLabel jLabel42;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel55;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane10;
    private javax.swing.JScrollPane jScrollPane11;
    private javax.swing.JScrollPane jScrollPane13;
    private javax.swing.JScrollPane jScrollPane14;
    private javax.swing.JScrollPane jScrollPane15;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane20;
    private javax.swing.JScrollPane jScrollPane21;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JScrollPane jScrollPane8;
    private javax.swing.JScrollPane jScrollPane9;
    private javax.swing.JSpinner jSpinner2;
    private javax.swing.JTable jTable1;
    private javax.swing.JTable jTable2;
    private javax.swing.JTable jTable3;
    private javax.swing.JTable jTable4;
    private javax.swing.JTextField jTextField10;
    private javax.swing.JTextField jTextField12;
    private javax.swing.JTextField jTextField13;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField4;
    private javax.swing.JTextField jTextField6;
    private javax.swing.JTextField jTextField7;
    private javax.swing.JTextField jTextField8;
    private javax.swing.JTextPane jTextPane1;
    private javax.swing.JTextPane jTextPane11;
    private javax.swing.JTextPane jTextPane5;
    private javax.swing.JTextPane jTextPane8;
    private javax.swing.JTextPane jTextPane9;
    private javax.swing.JButton loadlaiCTDVjButton3;
    private javax.swing.JTextPane otiendichvujTextPane3;
    private javax.swing.JTextPane otienphongjTextPane2;
    private javax.swing.JLabel sdtjLabel2;
    private javax.swing.JLabel songuoijLabel8;
    private javax.swing.JLabel tenjLabel4;
    private javax.swing.JTextField timkiemdichvujTextField2;
    private javax.swing.JTextPane tongtienjTextPane4;
    // End of variables declaration//GEN-END:variables
}
