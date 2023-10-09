package com.Views;

import com.Models.Staff;
import com.Services.Implements.StaffServiceImplement;
import com.Services.StaffService;
import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamPanel;
import com.github.sarxos.webcam.WebcamResolution;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.LuminanceSource;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.NotFoundException;
import com.google.zxing.Result;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;

import java.awt.image.BufferedImage;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

public class QRCode extends javax.swing.JFrame implements Runnable, ThreadFactory {

    private StaffService staffService = new StaffServiceImplement();

    private WebcamPanel panel = null;
    private Webcam webcam = null;
    private Executor executor = Executors.newSingleThreadExecutor(this);

    public QRCode() {
        initComponents();
        setResizable(false);
        this.initWebcam();
    }

    private void initWebcam() {
        java.awt.Dimension size = WebcamResolution.QVGA.getSize();
        webcam = Webcam.getWebcams().get(0);
        webcam.setViewSize(size);
        panel = new WebcamPanel(webcam);
        panel.setPreferredSize(size);

        Camera.add(panel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 483, 320));
        executor.execute(this);
    }

    private boolean connect = true;

    @Override
    public void run() {
        do {
            try {
                Thread.sleep(100);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
            Result result = null;
            BufferedImage image = null;
            if (webcam.isOpen()) {
                if ((image = webcam.getImage()) == null) {
                    continue;
                }
            }
            LuminanceSource source = new BufferedImageLuminanceSource(image);
            BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
            try {
                result = new MultiFormatReader().decode(bitmap);
            } catch (NotFoundException ex) {
                Logger.getLogger(QRCode.class.getName()).log(Level.SEVERE, null, ex);
            }
            if (result != null) {
                if (staffService.checkAccountStaffQR(String.valueOf(result))) {
                    loadAccountStaff(staffService.getAccountStaffQR(String.valueOf(result)));
                    connect = false;
                    webcam.close();
                } else {
                    JOptionPane.showMessageDialog(this, "Tài khoản này không có trong hệ thống");
                }
                System.out.println(result);
            }
        } while (connect);

    }

    @Override
    public Thread newThread(Runnable r) {
        Thread t = new Thread(r, "my thread");
        t.setDaemon(true);
        return t;
    }

    private void loadAccountStaff(List<Staff> list) {
        for (Staff staff : list) {
            int chucVu = staff.getRole();
            String hoTen = staff.getLastName() + " " + staff.getFirstName();
            String ma = staff.getCode();
            if (staff.getStatus() == 1) {
                new TrangChu(ma, hoTen, chucVu).setVisible(true);
                this.dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Tài khoản này không có quyền truy cập");
            }

        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        Camera = new javax.swing.JPanel();
        btnExit = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setUndecorated(true);

        jPanel1.setBackground(new java.awt.Color(246, 248, 250));
        jPanel1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        Camera.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        jPanel1.add(Camera, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 50, 483, 320));

        btnExit.setBackground(new java.awt.Color(255, 0, 0));
        btnExit.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnExit.setForeground(new java.awt.Color(255, 255, 255));
        btnExit.setText("X");
        btnExit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExitActionPerformed(evt);
            }
        });
        jPanel1.add(btnExit, new org.netbeans.lib.awtextra.AbsoluteConstraints(460, 10, -1, -1));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 505, Short.MAX_VALUE));
        layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 376, Short.MAX_VALUE));

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void btnExitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExitActionPerformed
        new Login().setVisible(true);
        webcam.close();
        dispose();
    }//GEN-LAST:event_btnExitActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel Camera;
    private javax.swing.JButton btnExit;
    private javax.swing.JPanel jPanel1;
    // End of variables declaration//GEN-END:variables

}
