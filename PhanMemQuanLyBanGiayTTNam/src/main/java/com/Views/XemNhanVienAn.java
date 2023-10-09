package com.Views;

import com.CustomModels.StaffCustomModel;
import com.Services.Implements.StaffServiceImplement;
import com.Services.StaffService;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

public class XemNhanVienAn extends javax.swing.JFrame {

    private StaffService staffService = new StaffServiceImplement();

    private DefaultTableModel defaultTableModel;

    public XemNhanVienAn() {
        initComponents();
        loadDataNhanVien(staffService.getListStaffOff());
    }

    private void loadDataNhanVien(List<StaffCustomModel> list) {
        defaultTableModel = (DefaultTableModel) tblNhanVienFrameAn.getModel();
        defaultTableModel.setRowCount(0);
        int count = 1;
        for (StaffCustomModel staffCustomModel : list) {
            defaultTableModel.addRow(new Object[]{count++, staffCustomModel.getMaNV(), staffCustomModel.getHoTen(), staffCustomModel.getNgaySinh(), staffCustomModel.getGioiTinh(), staffCustomModel.getSdt(), staffCustomModel.getEmail(), staffCustomModel.getDiaChi(), staffCustomModel.getChucVu() == 1 ? "Quản Lý" : "Nhân Viên"});
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        tblNhanVienFrameAn = new javax.swing.JTable();
        btnCloes = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setUndecorated(true);

        jPanel1.setBackground(new java.awt.Color(246, 248, 250));
        jPanel1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        tblNhanVienFrameAn.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "STT", "Mã NV", "Họ và Tên", "Ngày Sinh", "Giới Tính", "SDT", "Email", "Địa Chỉ", "Chức Vụ"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblNhanVienFrameAn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblNhanVienFrameAnMouseClicked(evt);
            }
        });
        jScrollPane3.setViewportView(tblNhanVienFrameAn);

        btnCloes.setBackground(new java.awt.Color(255, 0, 0));
        btnCloes.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnCloes.setForeground(new java.awt.Color(255, 255, 255));
        btnCloes.setText("X");
        btnCloes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCloesActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 863, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(btnCloes)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnCloes)
                .addGap(11, 11, 11)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 267, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void tblNhanVienFrameAnMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblNhanVienFrameAnMouseClicked
        int row = tblNhanVienFrameAn.getSelectedRow();
        if (row == -1) {
            return;
        }
        String code = tblNhanVienFrameAn.getValueAt(row, 1).toString();
        int confirm = JOptionPane.showConfirmDialog(this, "Bạn muốn bỏ ẩn Nhân Viên này không ?", "Thông báo", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            if (staffService.hideOrShow(code, 1)) {
                loadDataNhanVien(staffService.getListStaffOff());
                JOptionPane.showMessageDialog(this, "Bỏ ẩn Thành Công");
            } else {
                JOptionPane.showMessageDialog(this, "Bỏ ẩn Thất Bại");
            }
        } else {
            return;
        }
    }//GEN-LAST:event_tblNhanVienFrameAnMouseClicked

    private void btnCloesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCloesActionPerformed
        dispose();
    }//GEN-LAST:event_btnCloesActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCloes;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTable tblNhanVienFrameAn;
    // End of variables declaration//GEN-END:variables
}
