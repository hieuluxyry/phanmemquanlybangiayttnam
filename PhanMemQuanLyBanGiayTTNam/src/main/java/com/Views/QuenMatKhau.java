package com.Views;

import com.Models.Staff;
import com.Services.Implements.StaffServiceImplement;
import com.Services.StaffService;
import com.Utilities.LogicUtil;

import javax.swing.*;
import java.util.Random;

public class QuenMatKhau extends javax.swing.JFrame {

    private LogicUtil logicUtil = new LogicUtil();

    private StaffService staffService = new StaffServiceImplement();

    public QuenMatKhau() {
        initComponents();
    }
    
    public String ranDomCaptcha() {
        Random random = new Random();
        int captcha = random.nextInt(100000, 1000000);
        return String.valueOf(captcha);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        btnLoadCaptchaPanelDoiMatKhau = new javax.swing.JButton();
        btnLuuPanelDoiMatKhau = new javax.swing.JButton();
        jLabel44 = new javax.swing.JLabel();
        btnThoatPanelDoiMatKhau = new javax.swing.JButton();
        jLabel45 = new javax.swing.JLabel();
        jLabel59 = new javax.swing.JLabel();
        jLabel46 = new javax.swing.JLabel();
        jLabel60 = new javax.swing.JLabel();
        txtTaiKhoanPanelDoiMatKhau = new javax.swing.JTextField();
        jLabel61 = new javax.swing.JLabel();
        txtMatKhauMoiPanelDoiMatKhau = new javax.swing.JPasswordField();
        txtXBMatKhauPanelDoiMatKhau = new javax.swing.JPasswordField();
        txtCaptchaPanelDoiMatKhau = new javax.swing.JTextField();
        jLabel41 = new javax.swing.JLabel();
        jLabel42 = new javax.swing.JLabel();
        lblMaXacNhan = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setUndecorated(true);

        jPanel1.setBackground(new java.awt.Color(246, 248, 250));
        jPanel1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        btnLoadCaptchaPanelDoiMatKhau.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/update.png"))); // NOI18N
        btnLoadCaptchaPanelDoiMatKhau.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLoadCaptchaPanelDoiMatKhauActionPerformed(evt);
            }
        });

        btnLuuPanelDoiMatKhau.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnLuuPanelDoiMatKhau.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/luu.png"))); // NOI18N
        btnLuuPanelDoiMatKhau.setText("Lưu Thay Đổi");
        btnLuuPanelDoiMatKhau.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLuuPanelDoiMatKhauActionPerformed(evt);
            }
        });

        jLabel44.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel44.setText("Mật Khẩu Mới");

        btnThoatPanelDoiMatKhau.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnThoatPanelDoiMatKhau.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/dangxuat.png"))); // NOI18N
        btnThoatPanelDoiMatKhau.setText("Thoát");
        btnThoatPanelDoiMatKhau.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnThoatPanelDoiMatKhauActionPerformed(evt);
            }
        });

        jLabel45.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel45.setText("Xác Nhận Mật Khẩu");

        jLabel59.setFont(new java.awt.Font("Segoe UI", 2, 12)); // NOI18N
        jLabel59.setText("Lưu ý: Mật khẩu ít nhất 8 ký tự");

        jLabel46.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel46.setText("Mã Captcha");

        jLabel60.setFont(new java.awt.Font("Segoe UI", 2, 12)); // NOI18N
        jLabel60.setText("Mật khẩu có ký tự viết hoa, các chữ cái từ a-z, số 0-9");

        jLabel61.setFont(new java.awt.Font("Segoe UI", 2, 12)); // NOI18N
        jLabel61.setText("Và một số ký tự đặc biệt như \"@, !, #, *\"");

        txtCaptchaPanelDoiMatKhau.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N

        jLabel41.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel41.setText("Quên Mật Khẩu");

        jLabel42.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel42.setText("Tài khoản");

        lblMaXacNhan.setBackground(new java.awt.Color(246, 248, 250));
        lblMaXacNhan.setForeground(new java.awt.Color(246, 248, 250));
        lblMaXacNhan.setText(" ");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(17, 17, 17)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel42)
                            .addComponent(jLabel45)
                            .addComponent(jLabel46)
                            .addComponent(jLabel44)
                            .addComponent(jLabel41)
                            .addComponent(lblMaXacNhan, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(txtTaiKhoanPanelDoiMatKhau)
                                .addComponent(txtMatKhauMoiPanelDoiMatKhau)
                                .addComponent(txtXBMatKhauPanelDoiMatKhau, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 270, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                    .addComponent(txtCaptchaPanelDoiMatKhau, javax.swing.GroupLayout.PREFERRED_SIZE, 151, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(btnLoadCaptchaPanelDoiMatKhau)))
                            .addComponent(jLabel60)
                            .addComponent(jLabel59)
                            .addComponent(jLabel61)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(35, 35, 35)
                        .addComponent(btnLuuPanelDoiMatKhau)
                        .addGap(130, 130, 130)
                        .addComponent(btnThoatPanelDoiMatKhau)))
                .addContainerGap(20, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel41)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(19, 19, 19)
                        .addComponent(jLabel59)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel60)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel61)
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel42)
                            .addComponent(txtTaiKhoanPanelDoiMatKhau, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(38, 38, 38)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel44)
                            .addComponent(txtMatKhauMoiPanelDoiMatKhau, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(40, 40, 40)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel45)
                            .addComponent(txtXBMatKhauPanelDoiMatKhau, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(29, 29, 29)
                        .addComponent(btnLoadCaptchaPanelDoiMatKhau))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(44, 44, 44)
                        .addComponent(lblMaXacNhan)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtCaptchaPanelDoiMatKhau, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel46))))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnLuuPanelDoiMatKhau)
                    .addComponent(btnThoatPanelDoiMatKhau))
                .addContainerGap(28, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void btnLoadCaptchaPanelDoiMatKhauActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLoadCaptchaPanelDoiMatKhauActionPerformed
        String email = txtTaiKhoanPanelDoiMatKhau.getText();
        if (!staffService.checkAccountStaff(email)){
            JOptionPane.showMessageDialog(this, "Tài khoản không tồn tại");
            return;
        }
        lblMaXacNhan.setText(ranDomCaptcha());
        logicUtil.codeVerification(email, lblMaXacNhan.getText());
        JOptionPane.showMessageDialog(this, "Mã xác nhận đã được gửi đến email của bạn");
    }//GEN-LAST:event_btnLoadCaptchaPanelDoiMatKhauActionPerformed

    private void btnLuuPanelDoiMatKhauActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLuuPanelDoiMatKhauActionPerformed
        String email = txtTaiKhoanPanelDoiMatKhau.getText();
        String matKhauMoi = txtMatKhauMoiPanelDoiMatKhau.getText();
        String xacNhanMatKhau = txtXBMatKhauPanelDoiMatKhau.getText();
        String captcha = txtCaptchaPanelDoiMatKhau.getText();
        if (!staffService.checkAccountStaff(email)){
            JOptionPane.showMessageDialog(this, "Tài khoản không tồn tại");
            return;
        }
        if (captcha.equals(lblMaXacNhan.getText())) {
            if (matKhauMoi.equals(xacNhanMatKhau)) {
                staffService.updatePassword(email, logicUtil.taoMaHoa(matKhauMoi));
                JOptionPane.showMessageDialog(this, "Đổi mật khẩu thành công");
                new Login().setVisible(true);
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Mật khẩu mới không khớp");
            }
        } else {
            JOptionPane.showMessageDialog(this, "Mã xác nhận không đúng");
        }
    }//GEN-LAST:event_btnLuuPanelDoiMatKhauActionPerformed

    private void btnThoatPanelDoiMatKhauActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnThoatPanelDoiMatKhauActionPerformed
        new Login().setVisible(true);
        dispose();
    }//GEN-LAST:event_btnThoatPanelDoiMatKhauActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnLoadCaptchaPanelDoiMatKhau;
    private javax.swing.JButton btnLuuPanelDoiMatKhau;
    private javax.swing.JButton btnThoatPanelDoiMatKhau;
    private javax.swing.JLabel jLabel41;
    private javax.swing.JLabel jLabel42;
    private javax.swing.JLabel jLabel44;
    private javax.swing.JLabel jLabel45;
    private javax.swing.JLabel jLabel46;
    private javax.swing.JLabel jLabel59;
    private javax.swing.JLabel jLabel60;
    private javax.swing.JLabel jLabel61;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JLabel lblMaXacNhan;
    private javax.swing.JTextField txtCaptchaPanelDoiMatKhau;
    private javax.swing.JPasswordField txtMatKhauMoiPanelDoiMatKhau;
    private javax.swing.JTextField txtTaiKhoanPanelDoiMatKhau;
    private javax.swing.JPasswordField txtXBMatKhauPanelDoiMatKhau;
    // End of variables declaration//GEN-END:variables
}
