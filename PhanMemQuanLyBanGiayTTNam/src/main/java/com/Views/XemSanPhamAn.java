package com.Views;

import com.CustomModels.ProductDetailCustomModel;
import com.Services.Implements.ProductDetailServiceImplement;
import com.Services.ProductDetailService;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

public class XemSanPhamAn extends javax.swing.JFrame {

    private ProductDetailService productDetailService = new ProductDetailServiceImplement();

    private DefaultTableModel defaultTableModel;

    public XemSanPhamAn() {
        initComponents();
        loadDataSanPhamChiTiet(productDetailService.getListProductDetalHide());
    }

    private void loadDataSanPhamChiTiet(List<ProductDetailCustomModel> list) {
        defaultTableModel = (DefaultTableModel) tblChiTietSanPhamPanelSanPham.getModel();
        defaultTableModel.setRowCount(0);
        int count = 1;
        for (ProductDetailCustomModel productDetailCustomModel : list) {
            defaultTableModel.addRow(new Object[]{
                count++,
                productDetailCustomModel.getMaSP(),
                productDetailCustomModel.getTenSP(),
                productDetailCustomModel.getLoaiSP(),
                productDetailCustomModel.getKichCo(),
                productDetailCustomModel.getMauSac(),
                productDetailCustomModel.getChatLieu(),
                productDetailCustomModel.getDonGia(),
                productDetailCustomModel.getSoLuong(),
                productDetailCustomModel.getMoTa()
            });
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jScrollPane6 = new javax.swing.JScrollPane();
        tblChiTietSanPhamPanelSanPham = new javax.swing.JTable();
        btnCloes = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setUndecorated(true);

        jPanel1.setBackground(new java.awt.Color(246, 248, 250));
        jPanel1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        tblChiTietSanPhamPanelSanPham.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "STT", "Mã SP", "Tên SP", "Loại SP", "Kích Cỡ", "Màu Sắc", "Chất Liệu", "Giá Bán", "Số Lượng", "Mô Tả"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblChiTietSanPhamPanelSanPham.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblChiTietSanPhamPanelSanPhamMouseClicked(evt);
            }
        });
        jScrollPane6.setViewportView(tblChiTietSanPhamPanelSanPham);

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
                    .addComponent(jScrollPane6, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 859, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(btnCloes)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnCloes)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane6, javax.swing.GroupLayout.DEFAULT_SIZE, 251, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void tblChiTietSanPhamPanelSanPhamMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblChiTietSanPhamPanelSanPhamMouseClicked
        int row = tblChiTietSanPhamPanelSanPham.getSelectedRow();
        if (row < 0) {
            return;
        }
        String code = tblChiTietSanPhamPanelSanPham.getValueAt(row, 1).toString();
        int confirm = JOptionPane.showConfirmDialog(this, "Bạn muốn bỏ ẩn Sản Phẩm này không ?", "Thông báo", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            if (productDetailService.hideOrShow(code, 1)) {
                loadDataSanPhamChiTiet(productDetailService.getListProductDetalHide());
                JOptionPane.showMessageDialog(this, "Bỏ ẩn Thành Công");
            } else {
                JOptionPane.showMessageDialog(this, "Bỏ ẩn Thất Bại");
            }
        } else {
            return;
        }
    }//GEN-LAST:event_tblChiTietSanPhamPanelSanPhamMouseClicked

    private void btnCloesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCloesActionPerformed
        dispose();
    }//GEN-LAST:event_btnCloesActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCloes;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JTable tblChiTietSanPhamPanelSanPham;
    // End of variables declaration//GEN-END:variables
}
