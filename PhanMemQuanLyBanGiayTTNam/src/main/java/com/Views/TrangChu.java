package com.Views;

import com.CustomModels.*;
import com.Models.*;
import com.Services.*;
import com.Services.Implements.*;
import com.Utilities.ExportExcel;
import com.Utilities.LogicUtil;

import javax.swing.*;
import javax.swing.event.CaretEvent;
import javax.swing.table.DefaultTableModel;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.io.FileOutputStream;
import java.sql.Date;

import com.Utilities.SendEmail;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import com.itextpdf.text.Font;
import com.sun.xml.fastinfoset.sax.Properties;
import com.toedter.calendar.JDateChooser;

public class TrangChu extends javax.swing.JFrame implements Runnable, ThreadFactory {

    private BillDetailService billDetailService = new BillDetailServiceImplement();

    private BillService billService = new BillServiceImplement();

    private ColorService colorService = new ColorServiceImplement();

    private CustomerService customerService = new CustomerServiceImplement();

    private ProductDetailService productDetailService = new ProductDetailServiceImplement();

    private ProductTypeService productTypeService = new ProductTypeImplement();

    private PromotionDetailService promotionDetailService = new PromotionDetailServiceImplement();

    private PromotionService promotionService = new PromotionServiceImplement();

    private SizeService sizeService = new SizeServiceImplement();

    private StaffService staffService = new StaffServiceImplement();

    private SubstanceService substanceService = new SubstanceServiceImplement();

    private DefaultTableModel defaultTableModel;

    private DefaultComboBoxModel defaultComboBoxModel;

    private Color colorXanh = new Color(0, 153, 153);
    private Color colorTrang = new Color(246, 248, 250);

    private LogicUtil logicUtil = new LogicUtil();
    private ExportExcel exportExcel = new ExportExcel();

    private SendEmail sendEmail = new SendEmail();

    private WebcamPanel panel = null;
    private Webcam webcam = null;
    private Executor executor = Executors.newSingleThreadExecutor(this);

    public TrangChu(String ma, String hoTen, int chucVu) {
        initComponents();
        hideDatePromotion();
        tabbedPaneDatHang.setEnabled(false);
        lblMaNhanVienPanelMain.setText(ma);
        lblHoTenNVPanelMain.setText(hoTen);
        if (chucVu == 0) {
            btnThongKe.setEnabled(false);
            btnNhanVien.setEnabled(false);
            btnThongKe.setBackground(colorTrang);
            btnSanPham.setBackground(colorTrang);
            btnHoaDon.setBackground(colorXanh);
            btnLichSu.setBackground(colorTrang);
            btnKhuyenMai.setBackground(colorTrang);
            btnNhanVien.setBackground(colorTrang);
            btnKhachHang.setBackground(colorTrang);
            btnDoiMatKhau.setBackground(colorTrang);
            setPanel(panelHoaDon);
            loadDataSanPhamChiTietPanelHoaDon(productDetailService.getListProductDetal());
            setResizable(false);
            this.initWebcam();
            loadDataHoaDonPanelHoaDon(billService.getListBill());
            return;
        } else {
            btnThongKe.setBackground(colorXanh);
            btnSanPham.setBackground(colorTrang);
            btnHoaDon.setBackground(colorTrang);
            btnLichSu.setBackground(colorTrang);
            btnKhuyenMai.setBackground(colorTrang);
            btnNhanVien.setBackground(colorTrang);
            btnKhachHang.setBackground(colorTrang);
            btnDoiMatKhau.setBackground(colorTrang);
            setPanel(panelThongKe);
            cbbNamDoanhThuPanelThongKe.removeAllItems();
            addCbbNamDoanhThu(billService.getYear());
            lblSumDonHang.setText(String.valueOf(sumBill(billService.getList())));
            lblSumDate.setText(sum(billDetailService.sumDate(getNgayHienTai())));
            lblSumMonth.setText(sum(billDetailService.sumMonth(getMonth())));
            lblSumYear.setText(sum(billDetailService.sumYear(getYear())));
            donHang();
        }
        loadDataSanPhamChiTiet(productDetailService.getListProductDetal());
        insertKM(productDetailService.getList());
    }

    public TrangChu() {
        initComponents();
        hideDatePromotion();
        lblMaNhanVienPanelMain.setText("Admin");
        lblHoTenNVPanelMain.setText("Phần mềm quản lý bán Giày TTNam");
        btnThongKe.setEnabled(false);
        btnHoaDon.setEnabled(false);
        btnLichSu.setEnabled(false);
        btnThongKe.setBackground(colorTrang);
        btnSanPham.setBackground(colorXanh);
        btnHoaDon.setBackground(colorTrang);
        btnLichSu.setBackground(colorTrang);
        btnKhuyenMai.setBackground(colorTrang);
        btnNhanVien.setBackground(colorTrang);
        btnKhachHang.setBackground(colorTrang);
        btnDoiMatKhau.setBackground(colorTrang);
        setPanel(panelSanPham);
        rdoLoaiSanPhamPanelSanPham.setSelected(true);

        cbbLoaiSPPanelSanPham.removeAllItems();
        addCbbLoaiSanPham(productTypeService.getList());

        cbbMauSacPanelSanPham.removeAllItems();
        addCbbMauSac(colorService.getList());

        cbbKichCoPanelSanPham.removeAllItems();
        addCbbKichCo(sizeService.getList());

        cbbChatLieuPanelSanPham.removeAllItems();
        addCbbChatLieu(substanceService.getList());

        loadDataLoaiSanPham(productTypeService.getList());

        loadDataSanPhamChiTiet(productDetailService.getListProductDetal());
    }

    private void initWebcam() {
        java.awt.Dimension size = WebcamResolution.QVGA.getSize();
        webcam = Webcam.getWebcams().get(0);
        webcam.setViewSize(size);
        panel = new WebcamPanel(webcam);
        panel.setPreferredSize(size);

        jPanel22.add(panel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 207, 137));
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
                String maHD = tblHoaDonPanelHoaDon.getValueAt(tblHoaDonPanelHoaDon.getSelectedRow(), 1).toString();
                String maSP = String.valueOf(result);

                if (!billService.checkStatus(maHD)) {
                    JOptionPane.showMessageDialog(this, "Không thể thêm sản phẩm!");
                    return;
                }

                int soLuong = productDetailService.getBySoLuong(maSP);
                int setSL = soLuong - 1;
                BillDetails billDetails = getDataBillDetails(1, maSP, maHD, productDetailService.getByDonGia(maSP));

                if (soLuong == 0) {
                    JOptionPane.showMessageDialog(this, "Số lượng đã hết!");
                    return;
                }
                if (1 > soLuong) {
                    JOptionPane.showMessageDialog(this, "Số lượng trong kho không đủ");
                    return;
                }
                if (soLuong > 0) {
                    productDetailService.updateSoLuong(maSP, setSL);
                    loadDataSanPhamChiTietPanelHoaDon(productDetailService.getListProductDetal());
                    if (!billDetailService.checkProducts(maSP, maHD)) {
                        billDetailService.insert(billDetails);
                        loadDataGioHang(billDetailService.getListBill(maHD));
                        JOptionPane.showMessageDialog(this, "Thêm thành công");
                    } else {
                        int soLuongPanelGioHang = billDetailService.getSoLuong(maSP, maHD) + 1;
                        String idSP = productDetailService.getByID(maSP);
                        String idHD = billService.getByID(maHD);
                        billDetailService.update(idSP, idHD, soLuongPanelGioHang);
                        loadDataGioHang(billDetailService.getListBill(maHD));
                        JOptionPane.showMessageDialog(this, "Thêm thành công");
                    }
                }
                lblTongTienPanelHoaDon.setText(billDetailService.sumDonGia(maHD) + " VND");
            }
        } while (connect);

    }

    @Override
    public Thread newThread(Runnable r) {
        Thread t = new Thread(r, "my thread");
        t.setDaemon(true);
        return t;
    }

    private Date getNgayHienTai() {
        long millis = System.currentTimeMillis();
        java.sql.Date date = new java.sql.Date(millis);
        return date;
    }

    private int getMonth() {
        String tach[] = String.valueOf(getNgayHienTai()).split("-");
        String thang = tach[1];
        return Integer.parseInt(thang);
    }

    private int getYear() {
        String tach[] = String.valueOf(getNgayHienTai()).split("-");
        String nam = tach[0];
        return Integer.parseInt(nam);
    }

    private void setPanel(JPanel panel) {
        panelMain.removeAll();
        panelMain.repaint();
        panelMain.revalidate();
        panelMain.add(panel);
        panelMain.repaint();
        panelMain.revalidate();
    }

    private String sum(List<Double> list) {
        for (Double sum : list) {
            if (sum == null) {
                return 0 + ".VND";
            } else {
                int intValue = (int) Math.round(sum);
                return intValue + ".VND";
            }
        }
        return 0 + ".VND";
    }

    private int sumBill(List<Bill> list) {
        int count = 0;
        for (Bill bill : list) {
            count++;
        }
        return count;
    }

    private String sumThongKe(List<Bill> list) {
        int count = 0;
        for (Bill bill : list) {
            count++;
        }
        return String.valueOf(count);
    }

    private void donHang() {
        lblSumHangPanelThongKe.setText(sumThongKe(billService.sumAllThanhCong()));
        lblSumHuyPanelThongKe.setText(sumThongKe(billService.sumAllHuy()));
        lblSumNgayTCPanelThongKe.setText(sumThongKe(billService.sumNgayThanhCong(getNgayHienTai())));
        lblSumNgayHPanelThongKe.setText(sumThongKe(billService.sumNgayHuy(getNgayHienTai())));
        lblSumThangTCPanelThongKe.setText(sumThongKe(billService.sumThangThanhCong(getMonth())));
        lblSumThangHPanelThongKe.setText(sumThongKe(billService.sumThangHuy(getMonth())));
        lblSumNamTCPanelThongKe.setText(sumThongKe(billService.sumNamThanhCong(getYear())));
        lblSumNamHPanelThongKe.setText(sumThongKe(billService.sumNamHuy(getYear())));
    }

    private void insertKM(List<ProductDetails> list) {
        for (ProductDetails productDetails : list) {
            Promotion promotion = new Promotion(promotionService.getByID("KM0001"));
            ProductDetails PD = new ProductDetails(productDetailService.getByID(productDetails.getCode()));
            PromotionDetails promotionDetails = new PromotionDetails(promotion, PD, 1);
            promotionDetailService.insert(promotionDetails);
        }
    }

    private void hideDatePromotion() {
        promotionService.hideDate(getNgayHienTai(), 0);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        buttonGroup2 = new javax.swing.ButtonGroup();
        buttonGroup3 = new javax.swing.ButtonGroup();
        jSeparator6 = new javax.swing.JSeparator();
        buttonGroup4 = new javax.swing.ButtonGroup();
        panelTrangChu = new javax.swing.JPanel();
        btnExit = new javax.swing.JButton();
        btnThongKe = new javax.swing.JButton();
        btnSanPham = new javax.swing.JButton();
        btnHoaDon = new javax.swing.JButton();
        btnLichSu = new javax.swing.JButton();
        btnKhuyenMai = new javax.swing.JButton();
        btnNhanVien = new javax.swing.JButton();
        btnKhachHang = new javax.swing.JButton();
        btnDoiMatKhau = new javax.swing.JButton();
        btnDangXuat = new javax.swing.JButton();
        panelMain = new javax.swing.JPanel();
        panelThongKe = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        lblSumDate = new javax.swing.JLabel();
        lblSumNgayTCPanelThongKe = new javax.swing.JLabel();
        lblSumNgayHPanelThongKe = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        lblSumDonHang = new javax.swing.JLabel();
        lblSumHangPanelThongKe = new javax.swing.JLabel();
        lblSumHuyPanelThongKe = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        lblSumMonth = new javax.swing.JLabel();
        lblSumThangTCPanelThongKe = new javax.swing.JLabel();
        lblSumThangHPanelThongKe = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        lblSumYear = new javax.swing.JLabel();
        lblSumNamTCPanelThongKe = new javax.swing.JLabel();
        lblSumNamHPanelThongKe = new javax.swing.JLabel();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel6 = new javax.swing.JPanel();
        jScrollPane18 = new javax.swing.JScrollPane();
        tblDoanhThuPanelThongKe = new javax.swing.JTable();
        jLabel89 = new javax.swing.JLabel();
        cbbNamDoanhThuPanelThongKe = new javax.swing.JComboBox<>();
        jPanel7 = new javax.swing.JPanel();
        jScrollPane19 = new javax.swing.JScrollPane();
        tblSanPhamPanelThongKe = new javax.swing.JTable();
        btnXuatFileExcelPanelThongKe = new javax.swing.JButton();
        jSeparator5 = new javax.swing.JSeparator();
        jSeparator7 = new javax.swing.JSeparator();
        jLabel87 = new javax.swing.JLabel();
        jLabel88 = new javax.swing.JLabel();
        txtNgayBatDauPanelThongKe = new com.toedter.calendar.JDateChooser();
        txtNgayKetThucPanelThongKe = new com.toedter.calendar.JDateChooser();
        btnTimKiemPanelThongKe = new javax.swing.JButton();
        panelSanPham = new javax.swing.JPanel();
        jTabbedPane2 = new javax.swing.JTabbedPane();
        jPanel8 = new javax.swing.JPanel();
        jPanel14 = new javax.swing.JPanel();
        jScrollPane6 = new javax.swing.JScrollPane();
        tblChiTietSanPhamPanelSanPham = new javax.swing.JTable();
        jPanel15 = new javax.swing.JPanel();
        jLabel49 = new javax.swing.JLabel();
        jLabel50 = new javax.swing.JLabel();
        jLabel51 = new javax.swing.JLabel();
        jLabel52 = new javax.swing.JLabel();
        jLabel53 = new javax.swing.JLabel();
        jLabel54 = new javax.swing.JLabel();
        jLabel55 = new javax.swing.JLabel();
        jLabel56 = new javax.swing.JLabel();
        jLabel57 = new javax.swing.JLabel();
        txtMaSPPanelSanPham = new javax.swing.JTextField();
        txtTenSPPanelSanPham = new javax.swing.JTextField();
        txtGiaPanelSanPham = new javax.swing.JTextField();
        spnSoLuongPanelSanPham = new javax.swing.JSpinner();
        cbbLoaiSPPanelSanPham = new javax.swing.JComboBox<>();
        cbbKichCoPanelSanPham = new javax.swing.JComboBox<>();
        cbbMauSacPanelSanPham = new javax.swing.JComboBox<>();
        cbbChatLieuPanelSanPham = new javax.swing.JComboBox<>();
        jScrollPane11 = new javax.swing.JScrollPane();
        txtMoTaPanelSanPham = new javax.swing.JTextArea();
        txtTimKiemSPPanelSanPham = new javax.swing.JTextField();
        jLabel58 = new javax.swing.JLabel();
        cbbTimKiemSPPanelSanPham = new javax.swing.JComboBox<>();
        btnTimKiemPanelSanPham = new javax.swing.JButton();
        jSeparator11 = new javax.swing.JSeparator();
        btnXoaFormPanelSanPhamCT = new javax.swing.JButton();
        btnThemPanelSanPhamCT = new javax.swing.JButton();
        btnSuaPanelSanPhamCT = new javax.swing.JButton();
        btnAnPanelSanPhamCT = new javax.swing.JButton();
        btnXemAnPanelSanPhamCT = new javax.swing.JButton();
        jPanel9 = new javax.swing.JPanel();
        jPanel10 = new javax.swing.JPanel();
        txtTenThuocTinhPanelSanPham = new javax.swing.JTextField();
        jLabel48 = new javax.swing.JLabel();
        rdoLoaiSanPhamPanelSanPham = new javax.swing.JRadioButton();
        rdoMauSacPanelSanPham = new javax.swing.JRadioButton();
        rdoKichCoPanelSanPham = new javax.swing.JRadioButton();
        rdoChatLieuPanelSanPham = new javax.swing.JRadioButton();
        btnThemPanelSanPham = new javax.swing.JButton();
        btnSuaPanelSanPham = new javax.swing.JButton();
        btnXoaPanelSanPham = new javax.swing.JButton();
        txtMaThuocTinhPanelSanPham = new javax.swing.JTextField();
        jLabel62 = new javax.swing.JLabel();
        jPanel11 = new javax.swing.JPanel();
        jScrollPane5 = new javax.swing.JScrollPane();
        tblThuocTinhPanelSanPham = new javax.swing.JTable();
        panelHoaDon = new javax.swing.JPanel();
        jPanel16 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblHoaDonPanelHoaDon = new javax.swing.JTable();
        jPanel17 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblGioHangPanelHoaDon = new javax.swing.JTable();
        btnXoaGioHangPanelHoaDon = new javax.swing.JButton();
        btnSuaGioHangPanelHoaDon = new javax.swing.JButton();
        jPanel18 = new javax.swing.JPanel();
        jScrollPane14 = new javax.swing.JScrollPane();
        tblSanPhamPanelHoaDon = new javax.swing.JTable();
        jPanel19 = new javax.swing.JPanel();
        tabbedPaneDatHang = new javax.swing.JTabbedPane();
        jPanel20 = new javax.swing.JPanel();
        jSeparator1 = new javax.swing.JSeparator();
        jSeparator2 = new javax.swing.JSeparator();
        jLabel18 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        txtTenKhachHangPanelHoaDon = new javax.swing.JTextField();
        txtSDTPanelHoaDon = new javax.swing.JTextField();
        jLabel20 = new javax.swing.JLabel();
        jLabel30 = new javax.swing.JLabel();
        jLabel47 = new javax.swing.JLabel();
        jLabel63 = new javax.swing.JLabel();
        lblTongTienPanelHoaDon = new javax.swing.JLabel();
        cbbHTTTPanelHoaDon = new javax.swing.JComboBox<>();
        txtTienKhachDuaPanelHoaDon = new javax.swing.JTextField();
        lblTienThuaPanelHoaDon = new javax.swing.JLabel();
        btnThanhToanPanelHoaDon = new javax.swing.JButton();
        btnTaoHoaDonPanelHoaDon = new javax.swing.JButton();
        btnHuyPanelHoaDon = new javax.swing.JButton();
        jLabel76 = new javax.swing.JLabel();
        jScrollPane15 = new javax.swing.JScrollPane();
        txtGhiChuHoaDon = new javax.swing.JTextArea();
        btnTimKiemKhachHangPanelHoaDon = new javax.swing.JButton();
        jPanel21 = new javax.swing.JPanel();
        jSeparator3 = new javax.swing.JSeparator();
        jSeparator4 = new javax.swing.JSeparator();
        jLabel78 = new javax.swing.JLabel();
        jLabel79 = new javax.swing.JLabel();
        txtTenKhachHangPanelDatHang = new javax.swing.JTextField();
        txtSDTPanelDatHang = new javax.swing.JTextField();
        jLabel80 = new javax.swing.JLabel();
        jLabel81 = new javax.swing.JLabel();
        jLabel82 = new javax.swing.JLabel();
        jLabel83 = new javax.swing.JLabel();
        lblTongTienPanelDatHang = new javax.swing.JLabel();
        cbbHTTTPanelDatHang = new javax.swing.JComboBox<>();
        txtTienKhachDuaPanelDatHang = new javax.swing.JTextField();
        lblTienThuaPanelDatHang = new javax.swing.JLabel();
        btnDangGiaoPanelGiaoHang = new javax.swing.JButton();
        btnTaoHoaDonPanelDatHang = new javax.swing.JButton();
        jLabel84 = new javax.swing.JLabel();
        jScrollPane16 = new javax.swing.JScrollPane();
        txtDiaChiKhachHang = new javax.swing.JTextArea();
        jLabel85 = new javax.swing.JLabel();
        jScrollPane17 = new javax.swing.JScrollPane();
        txtGhiChuPanelDatHang = new javax.swing.JTextArea();
        btnDaGiaoPanelGiaoHang = new javax.swing.JButton();
        btnTimKiemPanelDatHang = new javax.swing.JButton();
        btnTraHangPanelGiaoHang = new javax.swing.JButton();
        jPanel22 = new javax.swing.JPanel();
        Camera = new javax.swing.JPanel();
        panelLichSu = new javax.swing.JPanel();
        jScrollPane8 = new javax.swing.JScrollPane();
        txtMoTaPanelLichSu = new javax.swing.JTextArea();
        jLabel33 = new javax.swing.JLabel();
        jLabel34 = new javax.swing.JLabel();
        jLabel35 = new javax.swing.JLabel();
        jLabel36 = new javax.swing.JLabel();
        jLabel37 = new javax.swing.JLabel();
        jLabel38 = new javax.swing.JLabel();
        jLabel40 = new javax.swing.JLabel();
        jScrollPane7 = new javax.swing.JScrollPane();
        txtDiaChiPanelLichSu = new javax.swing.JTextArea();
        jLabel32 = new javax.swing.JLabel();
        jPanel12 = new javax.swing.JPanel();
        jScrollPane9 = new javax.swing.JScrollPane();
        tblHoaDonPanelLichSu = new javax.swing.JTable();
        jPanel13 = new javax.swing.JPanel();
        jScrollPane10 = new javax.swing.JScrollPane();
        tblSanPhamPanelLichSu = new javax.swing.JTable();
        lblMaNVPanelLichSu = new javax.swing.JLabel();
        lblTenNVPanelLichSu = new javax.swing.JLabel();
        lblTenKHPanelLichSu = new javax.swing.JLabel();
        lblSDTPanelLichSu = new javax.swing.JLabel();
        lblTongTienPanelLichSu = new javax.swing.JLabel();
        lblNgayTaoPanelLichSu = new javax.swing.JLabel();
        jPanel25 = new javax.swing.JPanel();
        txtMaHoaDonPanelLichSua = new javax.swing.JTextField();
        btnTimKiemMaHDPanelLichSu = new javax.swing.JButton();
        panelKhuyenMai = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jLabel70 = new javax.swing.JLabel();
        txtMaKhuyenMaiPanelKhuyenMai = new javax.swing.JTextField();
        jLabel71 = new javax.swing.JLabel();
        txtTenChuongTrinhPanelKhuyenMai = new javax.swing.JTextField();
        jLabel72 = new javax.swing.JLabel();
        txtNgayBatDauPanelKhuyenMai = new com.toedter.calendar.JDateChooser();
        txtNgayKetThucPanelKhuyenMai = new com.toedter.calendar.JDateChooser();
        jLabel73 = new javax.swing.JLabel();
        jLabel74 = new javax.swing.JLabel();
        spnPhanTramKhuyenMaiPanelKhuyenMai = new javax.swing.JSpinner();
        btnThemPanelKhuyenMai = new javax.swing.JButton();
        btnSuaPanelKhuyenMai = new javax.swing.JButton();
        btnAnPanelKhuyenMai = new javax.swing.JButton();
        btnXoaFormPanelKhuyenMai = new javax.swing.JButton();
        btnXemKhuyenMaiPanelKhuyenMai = new javax.swing.JButton();
        jSeparator8 = new javax.swing.JSeparator();
        jPanel23 = new javax.swing.JPanel();
        jScrollPane13 = new javax.swing.JScrollPane();
        tblKhuyenMaiPanelKhuyenMai = new javax.swing.JTable();
        jPanel24 = new javax.swing.JPanel();
        jScrollPane20 = new javax.swing.JScrollPane();
        tblSanPhamPanelKhuyenMai = new javax.swing.JTable();
        btnChonTatCaPanelKhuyenMai = new javax.swing.JButton();
        panelNhanVien = new javax.swing.JPanel();
        rdoNamPanelNhanVien = new javax.swing.JRadioButton();
        rdoNuPanelNhanVien = new javax.swing.JRadioButton();
        jLabel21 = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        jLabel23 = new javax.swing.JLabel();
        jLabel24 = new javax.swing.JLabel();
        jLabel25 = new javax.swing.JLabel();
        jLabel26 = new javax.swing.JLabel();
        jLabel27 = new javax.swing.JLabel();
        jLabel28 = new javax.swing.JLabel();
        txtHoPanelNhanVien = new javax.swing.JTextField();
        txtTenPanelNhanVien = new javax.swing.JTextField();
        txtMaNhanVienPanelNhanVien = new javax.swing.JTextField();
        txtEmailPanelNhanVien = new javax.swing.JTextField();
        txtSDTPanelNhanVien = new javax.swing.JTextField();
        cbbChucVuPanelNhanVien = new javax.swing.JComboBox<>();
        jScrollPane4 = new javax.swing.JScrollPane();
        txtDiaChiPanelNhanVien = new javax.swing.JTextArea();
        txtMatKhauPanelNhanVien = new javax.swing.JPasswordField();
        jLabel29 = new javax.swing.JLabel();
        jLabel31 = new javax.swing.JLabel();
        btnXemNhanVienDaNghiPanelNV = new javax.swing.JButton();
        txtNgaySinhNhanVienPanelNhanVien = new com.toedter.calendar.JDateChooser();
        btnXoaFormPanelNhanVien = new javax.swing.JButton();
        btnThemPanelNhanVien = new javax.swing.JButton();
        btnSuaPanelNhanVien = new javax.swing.JButton();
        btnAnPanelNhanVien = new javax.swing.JButton();
        jScrollPane3 = new javax.swing.JScrollPane();
        tblNhanVienPanelNhanVien = new javax.swing.JTable();
        jSeparator10 = new javax.swing.JSeparator();
        panelKhachHang = new javax.swing.JPanel();
        jScrollPane12 = new javax.swing.JScrollPane();
        tblKhachHangPanelKhachHang = new javax.swing.JTable();
        jLabel64 = new javax.swing.JLabel();
        jLabel65 = new javax.swing.JLabel();
        jLabel66 = new javax.swing.JLabel();
        txtHoPanelKhachHang = new javax.swing.JTextField();
        txtTenPanelKhachHang = new javax.swing.JTextField();
        txtMaKhachHangPanelKhachHang = new javax.swing.JTextField();
        txtSDTPanelKhachHang = new javax.swing.JTextField();
        jLabel67 = new javax.swing.JLabel();
        jLabel68 = new javax.swing.JLabel();
        txtEmailPanelKhachHang = new javax.swing.JTextField();
        btnXoaFormPanelKhachHang = new javax.swing.JButton();
        btnThemPanelKhachHang = new javax.swing.JButton();
        btnSuaPanelKhachHang = new javax.swing.JButton();
        txtTimKiemKhachHangPanelKH = new javax.swing.JTextField();
        jLabel69 = new javax.swing.JLabel();
        jSeparator9 = new javax.swing.JSeparator();
        panelDoiMatKhau = new javax.swing.JPanel();
        jLabel41 = new javax.swing.JLabel();
        jLabel42 = new javax.swing.JLabel();
        jLabel43 = new javax.swing.JLabel();
        jLabel44 = new javax.swing.JLabel();
        jLabel45 = new javax.swing.JLabel();
        jLabel46 = new javax.swing.JLabel();
        txtTaiKhoanPanelDoiMatKhau = new javax.swing.JTextField();
        txtMatKhauMoiPanelDoiMatKhau = new javax.swing.JPasswordField();
        txtXNMatKhauPanelDoiMatKhau = new javax.swing.JPasswordField();
        txtMatKhauCuPanelDoiMatKhau = new javax.swing.JPasswordField();
        txtCaptchaPanelDoiMatKhau = new javax.swing.JTextField();
        txtLoadCaptchaPanelDoiMatKhau = new javax.swing.JTextField();
        btnLoadCaptchaPanelDoiMatKhau = new javax.swing.JButton();
        btnLuuPanelDoiMatKhau = new javax.swing.JButton();
        btnThoatPanelDoiMatKhau = new javax.swing.JButton();
        jLabel59 = new javax.swing.JLabel();
        jLabel60 = new javax.swing.JLabel();
        jLabel61 = new javax.swing.JLabel();
        jLabel75 = new javax.swing.JLabel();
        lblMaNhanVienPanelMain = new javax.swing.JLabel();
        lblHoTenNVPanelMain = new javax.swing.JLabel();
        jLabel77 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setUndecorated(true);

        panelTrangChu.setBackground(new java.awt.Color(255, 255, 255));
        panelTrangChu.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        panelTrangChu.setPreferredSize(new java.awt.Dimension(1330, 765));

        btnExit.setBackground(new java.awt.Color(255, 0, 0));
        btnExit.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnExit.setForeground(new java.awt.Color(255, 255, 255));
        btnExit.setText("X");
        btnExit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExitActionPerformed(evt);
            }
        });

        btnThongKe.setBackground(new java.awt.Color(246, 248, 250));
        btnThongKe.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnThongKe.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/thongke.png"))); // NOI18N
        btnThongKe.setText("Thống Kê");
        btnThongKe.setToolTipText("");
        btnThongKe.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnThongKeActionPerformed(evt);
            }
        });

        btnSanPham.setBackground(new java.awt.Color(246, 248, 250));
        btnSanPham.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnSanPham.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/sanpham.png"))); // NOI18N
        btnSanPham.setText("Sản Phẩm");
        btnSanPham.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSanPhamActionPerformed(evt);
            }
        });

        btnHoaDon.setBackground(new java.awt.Color(246, 248, 250));
        btnHoaDon.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnHoaDon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/hoadon.png"))); // NOI18N
        btnHoaDon.setText("Hóa Đơn");
        btnHoaDon.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnHoaDonActionPerformed(evt);
            }
        });

        btnLichSu.setBackground(new java.awt.Color(246, 248, 250));
        btnLichSu.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnLichSu.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/lichsu.png"))); // NOI18N
        btnLichSu.setText("Lịch Sử");
        btnLichSu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLichSuActionPerformed(evt);
            }
        });

        btnKhuyenMai.setBackground(new java.awt.Color(246, 248, 250));
        btnKhuyenMai.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnKhuyenMai.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/khuyenmai.png"))); // NOI18N
        btnKhuyenMai.setText("Khuyến Mãi");
        btnKhuyenMai.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnKhuyenMaiActionPerformed(evt);
            }
        });

        btnNhanVien.setBackground(new java.awt.Color(246, 248, 250));
        btnNhanVien.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnNhanVien.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/nhanvien.png"))); // NOI18N
        btnNhanVien.setText("Nhân Viên");
        btnNhanVien.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNhanVienActionPerformed(evt);
            }
        });

        btnKhachHang.setBackground(new java.awt.Color(246, 248, 250));
        btnKhachHang.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnKhachHang.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/khachhang.png"))); // NOI18N
        btnKhachHang.setText("Khách Hàng");
        btnKhachHang.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnKhachHangActionPerformed(evt);
            }
        });

        btnDoiMatKhau.setBackground(new java.awt.Color(246, 248, 250));
        btnDoiMatKhau.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnDoiMatKhau.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/doimatkhau.png"))); // NOI18N
        btnDoiMatKhau.setText("Đổi mật khẩu");
        btnDoiMatKhau.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDoiMatKhauActionPerformed(evt);
            }
        });

        btnDangXuat.setBackground(new java.awt.Color(246, 248, 250));
        btnDangXuat.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnDangXuat.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/dangxuat.png"))); // NOI18N
        btnDangXuat.setText("Đăng Xuất");
        btnDangXuat.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDangXuatActionPerformed(evt);
            }
        });

        panelMain.setBackground(new java.awt.Color(246, 248, 250));
        panelMain.setLayout(new java.awt.CardLayout());

        panelThongKe.setBackground(new java.awt.Color(246, 248, 250));

        jPanel2.setBackground(new java.awt.Color(102, 204, 255));
        jPanel2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel2.setText("Tổng Doanh Thu Ngày");

        jLabel3.setText("Thành Công:");

        jLabel4.setText("Bị Hủy:");

        jLabel5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/ngay.png"))); // NOI18N

        lblSumDate.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblSumDate.setForeground(new java.awt.Color(255, 0, 0));
        lblSumDate.setText(" ");

        lblSumNgayTCPanelThongKe.setText(" ");

        lblSumNgayHPanelThongKe.setText(" ");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addContainerGap(127, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel3)
                            .addComponent(jLabel4))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblSumNgayHPanelThongKe, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblSumNgayTCPanelThongKe, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 27, Short.MAX_VALUE)
                        .addComponent(jLabel5)
                        .addGap(14, 14, 14))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(lblSumDate, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addContainerGap())))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(lblSumDate)
                .addGap(28, 28, 28)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel5)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel3)
                            .addComponent(lblSumNgayTCPanelThongKe))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel4)
                            .addComponent(lblSumNgayHPanelThongKe))))
                .addContainerGap(15, Short.MAX_VALUE))
        );

        jPanel3.setBackground(new java.awt.Color(102, 204, 255));
        jPanel3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabel6.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel6.setText("Tổng Đơn Hàng");

        jLabel7.setText("Thành Công:");

        jLabel8.setText("Bị Hủy:");

        jLabel9.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/giohang.png"))); // NOI18N

        lblSumDonHang.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblSumDonHang.setForeground(new java.awt.Color(255, 0, 0));
        lblSumDonHang.setText(" ");

        lblSumHangPanelThongKe.setText(" ");

        lblSumHuyPanelThongKe.setText(" ");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel6)
                    .addComponent(lblSumDonHang, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(jLabel7)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(lblSumHangPanelThongKe, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(jLabel8)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 42, Short.MAX_VALUE)
                                .addComponent(lblSumHuyPanelThongKe, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel9)))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(lblSumDonHang)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 28, Short.MAX_VALUE)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel7)
                            .addComponent(lblSumHangPanelThongKe))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel8)
                            .addComponent(lblSumHuyPanelThongKe)))
                    .addComponent(jLabel9))
                .addGap(15, 15, 15))
        );

        jPanel4.setBackground(new java.awt.Color(102, 204, 255));
        jPanel4.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabel10.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel10.setText("Tổng Doanh Thu Tháng");

        jLabel11.setText("Thành Công:");

        jLabel12.setText("Bị Hủy:");

        jLabel13.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/thang.png"))); // NOI18N

        lblSumMonth.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblSumMonth.setForeground(new java.awt.Color(255, 0, 0));
        lblSumMonth.setText(" ");

        lblSumThangTCPanelThongKe.setText(" ");

        lblSumThangHPanelThongKe.setText(" ");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabel10)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(lblSumMonth, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel11)
                                    .addComponent(jLabel12))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(lblSumThangHPanelThongKe, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(lblSumThangTCPanelThongKe, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(36, 36, 36)
                                .addComponent(jLabel13)))
                        .addGap(14, 14, 14))))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel10)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(lblSumMonth)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 26, Short.MAX_VALUE)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel13, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel11)
                            .addComponent(lblSumThangTCPanelThongKe))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel12)
                            .addComponent(lblSumThangHPanelThongKe))))
                .addGap(17, 17, 17))
        );

        jPanel5.setBackground(new java.awt.Color(102, 204, 255));
        jPanel5.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabel14.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel14.setText("Tổng Doanh Thu Năm");

        jLabel15.setText("Thành Công:");

        jLabel16.setText("Bị Hủy:");

        jLabel17.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/nam.png"))); // NOI18N

        lblSumYear.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblSumYear.setForeground(new java.awt.Color(255, 0, 0));
        lblSumYear.setText(" ");

        lblSumNamTCPanelThongKe.setText(" ");

        lblSumNamHPanelThongKe.setText(" ");

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblSumYear, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel15)
                            .addComponent(jLabel16))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblSumNamHPanelThongKe, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblSumNamTCPanelThongKe, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 35, Short.MAX_VALUE)
                        .addComponent(jLabel17))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(jLabel14)
                        .addGap(0, 125, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel14)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(lblSumYear)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 26, Short.MAX_VALUE)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel17, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel15)
                            .addComponent(lblSumNamTCPanelThongKe))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel16)
                            .addComponent(lblSumNamHPanelThongKe))))
                .addGap(17, 17, 17))
        );

        jPanel6.setBackground(new java.awt.Color(246, 248, 250));

        tblDoanhThuPanelThongKe.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Tháng", "Tổng Số Sản Phẩm", "Tổng Giá Bán", "Tổng Giá Giảm", "Doanh Thu"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane18.setViewportView(tblDoanhThuPanelThongKe);
        if (tblDoanhThuPanelThongKe.getColumnModel().getColumnCount() > 0) {
            tblDoanhThuPanelThongKe.getColumnModel().getColumn(0).setPreferredWidth(20);
        }

        jLabel89.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel89.setText("Năm");

        cbbNamDoanhThuPanelThongKe.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        cbbNamDoanhThuPanelThongKe.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbbNamDoanhThuPanelThongKeActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(jLabel89)
                .addGap(18, 18, 18)
                .addComponent(cbbNamDoanhThuPanelThongKe, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane18, javax.swing.GroupLayout.DEFAULT_SIZE, 997, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane18, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(39, 39, 39)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel89)
                    .addComponent(cbbNamDoanhThuPanelThongKe, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Doanh Thu", jPanel6);

        jPanel7.setBackground(new java.awt.Color(246, 248, 250));

        tblSanPhamPanelThongKe.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "STT", "Mã SP", "Tên SP", "Loại SP", "Kích Cỡ", "Màu Sắc", "Chất Liệu", "Số Lượng"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane19.setViewportView(tblSanPhamPanelThongKe);
        if (tblSanPhamPanelThongKe.getColumnModel().getColumnCount() > 0) {
            tblSanPhamPanelThongKe.getColumnModel().getColumn(0).setPreferredWidth(20);
            tblSanPhamPanelThongKe.getColumnModel().getColumn(0).setHeaderValue("STT");
            tblSanPhamPanelThongKe.getColumnModel().getColumn(1).setHeaderValue("Mã SP");
            tblSanPhamPanelThongKe.getColumnModel().getColumn(2).setHeaderValue("Tên SP");
            tblSanPhamPanelThongKe.getColumnModel().getColumn(3).setHeaderValue("Loại SP");
            tblSanPhamPanelThongKe.getColumnModel().getColumn(4).setHeaderValue("Kích Cỡ");
            tblSanPhamPanelThongKe.getColumnModel().getColumn(5).setHeaderValue("Màu Sắc");
            tblSanPhamPanelThongKe.getColumnModel().getColumn(6).setHeaderValue("Chất Liệu");
            tblSanPhamPanelThongKe.getColumnModel().getColumn(7).setHeaderValue("Số Lượng");
        }

        btnXuatFileExcelPanelThongKe.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnXuatFileExcelPanelThongKe.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/excel.png"))); // NOI18N
        btnXuatFileExcelPanelThongKe.setText("Xuất File Excel");
        btnXuatFileExcelPanelThongKe.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnXuatFileExcelPanelThongKeActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane19, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 1177, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(btnXuatFileExcelPanelThongKe)))
                .addContainerGap())
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane19, javax.swing.GroupLayout.DEFAULT_SIZE, 386, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnXuatFileExcelPanelThongKe)
                .addContainerGap())
        );

        jTabbedPane1.addTab("Sản Phẩm", jPanel7);

        jLabel87.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel87.setText("Ngày Bắt Đầu");

        jLabel88.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel88.setText("Ngày Kết Thúc");

        txtNgayBatDauPanelThongKe.setDateFormatString("yyyy-MM-dd");

        txtNgayKetThucPanelThongKe.setDateFormatString("yyyy-MM-dd");

        btnTimKiemPanelThongKe.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnTimKiemPanelThongKe.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/search.png"))); // NOI18N
        btnTimKiemPanelThongKe.setText("Tìm Kiếm");
        btnTimKiemPanelThongKe.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTimKiemPanelThongKeActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelThongKeLayout = new javax.swing.GroupLayout(panelThongKe);
        panelThongKe.setLayout(panelThongKeLayout);
        panelThongKeLayout.setHorizontalGroup(
            panelThongKeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelThongKeLayout.createSequentialGroup()
                .addGap(344, 344, 344)
                .addComponent(jLabel87)
                .addGap(18, 18, 18)
                .addComponent(txtNgayBatDauPanelThongKe, javax.swing.GroupLayout.PREFERRED_SIZE, 213, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel88)
                .addGap(18, 18, 18)
                .addComponent(txtNgayKetThucPanelThongKe, javax.swing.GroupLayout.PREFERRED_SIZE, 213, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(44, 44, 44)
                .addComponent(btnTimKiemPanelThongKe)
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelThongKeLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelThongKeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(jSeparator7, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jSeparator5, javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, panelThongKeLayout.createSequentialGroup()
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        panelThongKeLayout.setVerticalGroup(
            panelThongKeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelThongKeLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelThongKeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jSeparator5, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(panelThongKeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(panelThongKeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(panelThongKeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel87)
                            .addComponent(jLabel88))
                        .addComponent(txtNgayBatDauPanelThongKe, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(txtNgayKetThucPanelThongKe, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(btnTimKiemPanelThongKe))
                .addGap(22, 22, 22)
                .addComponent(jSeparator7, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTabbedPane1)
                .addContainerGap())
        );

        panelMain.add(panelThongKe, "card2");

        panelSanPham.setBackground(new java.awt.Color(246, 248, 250));

        jPanel8.setBackground(new java.awt.Color(246, 248, 250));

        jPanel14.setBackground(new java.awt.Color(246, 248, 250));
        jPanel14.setBorder(javax.swing.BorderFactory.createTitledBorder("Thông Tin Sản Phẩm"));

        tblChiTietSanPhamPanelSanPham.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "STT", "Mã SP", "Tên SP", "Loại SP", "Kích Cỡ", "Màu Sắc", "Chất Liệu", "Giá Bán", "Số Lượng", "Mô Tả"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Integer.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

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

        javax.swing.GroupLayout jPanel14Layout = new javax.swing.GroupLayout(jPanel14);
        jPanel14.setLayout(jPanel14Layout);
        jPanel14Layout.setHorizontalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel14Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane6)
                .addContainerGap())
        );
        jPanel14Layout.setVerticalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane6, javax.swing.GroupLayout.DEFAULT_SIZE, 266, Short.MAX_VALUE)
        );

        jPanel15.setBackground(new java.awt.Color(246, 248, 250));
        jPanel15.setBorder(javax.swing.BorderFactory.createTitledBorder("Quản Lý Sản Phẩm"));

        jLabel49.setText("Mã SP");

        jLabel50.setText("Tên SP");

        jLabel51.setText("Giá Bán");

        jLabel52.setText("Số Lượng");

        jLabel53.setText("Loại SP");

        jLabel54.setText("Kích Cỡ");

        jLabel55.setText("Màu Sắc");

        jLabel56.setText("Chất Liệu");

        jLabel57.setText("Mô Tả");

        txtMaSPPanelSanPham.setEditable(false);

        spnSoLuongPanelSanPham.setModel(new javax.swing.SpinnerNumberModel(0, 0, null, 1));

        txtMoTaPanelSanPham.setColumns(20);
        txtMoTaPanelSanPham.setRows(5);
        jScrollPane11.setViewportView(txtMoTaPanelSanPham);

        jLabel58.setText("Tìm Kiếm Theo");

        cbbTimKiemSPPanelSanPham.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Mã sản phẩm", "Tên sản phẩm", "Loại sản phẩm", "Kích cỡ", "Màu Sắc", "Chất Liệu" }));

        btnTimKiemPanelSanPham.setText("Tìm Kiếm");
        btnTimKiemPanelSanPham.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTimKiemPanelSanPhamActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel15Layout = new javax.swing.GroupLayout(jPanel15);
        jPanel15.setLayout(jPanel15Layout);
        jPanel15Layout.setHorizontalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel15Layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel15Layout.createSequentialGroup()
                        .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel49)
                            .addComponent(jLabel50))
                        .addGap(39, 39, 39)
                        .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtTenSPPanelSanPham, javax.swing.GroupLayout.PREFERRED_SIZE, 206, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtMaSPPanelSanPham, javax.swing.GroupLayout.PREFERRED_SIZE, 206, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel15Layout.createSequentialGroup()
                        .addComponent(jLabel52)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(spnSoLuongPanelSanPham, javax.swing.GroupLayout.PREFERRED_SIZE, 206, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel15Layout.createSequentialGroup()
                        .addComponent(jLabel51)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(txtGiaPanelSanPham, javax.swing.GroupLayout.PREFERRED_SIZE, 206, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel53)
                    .addComponent(jLabel54))
                .addGap(31, 31, 31)
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(cbbLoaiSPPanelSanPham, 0, 135, Short.MAX_VALUE)
                    .addComponent(cbbKichCoPanelSanPham, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(39, 39, 39)
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel56)
                    .addComponent(jLabel55))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(jPanel15Layout.createSequentialGroup()
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel15Layout.createSequentialGroup()
                        .addGap(635, 635, 635)
                        .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(cbbMauSacPanelSanPham, javax.swing.GroupLayout.PREFERRED_SIZE, 144, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cbbChatLieuPanelSanPham, javax.swing.GroupLayout.PREFERRED_SIZE, 144, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel15Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel15Layout.createSequentialGroup()
                                .addComponent(jLabel58)
                                .addGap(29, 29, 29)
                                .addComponent(cbbTimKiemSPPanelSanPham, javax.swing.GroupLayout.PREFERRED_SIZE, 137, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(txtTimKiemSPPanelSanPham, javax.swing.GroupLayout.PREFERRED_SIZE, 228, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(56, 56, 56)
                                .addComponent(btnTimKiemPanelSanPham))
                            .addComponent(jSeparator11, javax.swing.GroupLayout.PREFERRED_SIZE, 761, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 92, Short.MAX_VALUE)
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane11, javax.swing.GroupLayout.PREFERRED_SIZE, 278, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel57))
                .addGap(18, 18, 18))
        );
        jPanel15Layout.setVerticalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel15Layout.createSequentialGroup()
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel15Layout.createSequentialGroup()
                        .addGap(11, 11, 11)
                        .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel49)
                            .addComponent(jLabel53)
                            .addComponent(jLabel55)
                            .addComponent(txtMaSPPanelSanPham, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cbbLoaiSPPanelSanPham, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cbbMauSacPanelSanPham, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel15Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel57)))
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel15Layout.createSequentialGroup()
                        .addGap(22, 22, 22)
                        .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel50)
                            .addComponent(txtTenSPPanelSanPham, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(26, 26, 26)
                        .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(cbbChatLieuPanelSanPham, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel56)
                            .addComponent(cbbKichCoPanelSanPham, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel54)
                            .addComponent(txtGiaPanelSanPham, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel51))
                        .addGap(26, 26, 26)
                        .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel52)
                            .addComponent(spnSoLuongPanelSanPham, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jSeparator11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel15Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jScrollPane11, javax.swing.GroupLayout.PREFERRED_SIZE, 146, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel58)
                    .addComponent(cbbTimKiemSPPanelSanPham, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtTimKiemSPPanelSanPham, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnTimKiemPanelSanPham))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        btnXoaFormPanelSanPhamCT.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnXoaFormPanelSanPhamCT.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/clear.png"))); // NOI18N
        btnXoaFormPanelSanPhamCT.setText("Xóa Form");
        btnXoaFormPanelSanPhamCT.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnXoaFormPanelSanPhamCTActionPerformed(evt);
            }
        });

        btnThemPanelSanPhamCT.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnThemPanelSanPhamCT.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/add.png"))); // NOI18N
        btnThemPanelSanPhamCT.setText("Thêm");
        btnThemPanelSanPhamCT.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnThemPanelSanPhamCTActionPerformed(evt);
            }
        });

        btnSuaPanelSanPhamCT.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnSuaPanelSanPhamCT.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/update.png"))); // NOI18N
        btnSuaPanelSanPhamCT.setText("Sửa");
        btnSuaPanelSanPhamCT.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSuaPanelSanPhamCTActionPerformed(evt);
            }
        });

        btnAnPanelSanPhamCT.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnAnPanelSanPhamCT.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/remove.png"))); // NOI18N
        btnAnPanelSanPhamCT.setText("Ẩn");
        btnAnPanelSanPhamCT.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAnPanelSanPhamCTActionPerformed(evt);
            }
        });

        btnXemAnPanelSanPhamCT.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnXemAnPanelSanPhamCT.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/an.png"))); // NOI18N
        btnXemAnPanelSanPhamCT.setText("Xem Sản Phẩm Đã Ẩn");
        btnXemAnPanelSanPhamCT.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnXemAnPanelSanPhamCTActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel14, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel15, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(btnXoaFormPanelSanPhamCT)
                .addGap(29, 29, 29)
                .addComponent(btnThemPanelSanPhamCT)
                .addGap(31, 31, 31)
                .addComponent(btnSuaPanelSanPhamCT)
                .addGap(22, 22, 22)
                .addComponent(btnAnPanelSanPhamCT)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnXemAnPanelSanPhamCT)
                .addGap(25, 25, 25))
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel15, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel14, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnXoaFormPanelSanPhamCT)
                    .addComponent(btnThemPanelSanPhamCT)
                    .addComponent(btnSuaPanelSanPhamCT)
                    .addComponent(btnAnPanelSanPhamCT)
                    .addComponent(btnXemAnPanelSanPhamCT))
                .addContainerGap(87, Short.MAX_VALUE))
        );

        jTabbedPane2.addTab("Thông Tin Chi Tiết", jPanel8);

        jPanel9.setBackground(new java.awt.Color(246, 248, 250));

        jPanel10.setBackground(new java.awt.Color(246, 248, 250));
        jPanel10.setBorder(javax.swing.BorderFactory.createTitledBorder("Thuộc Tính Sản Phẩm"));

        jLabel48.setText("Tên Thuộc Tính");

        buttonGroup2.add(rdoLoaiSanPhamPanelSanPham);
        rdoLoaiSanPhamPanelSanPham.setText("Loại Sản Phẩm");
        rdoLoaiSanPhamPanelSanPham.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoLoaiSanPhamPanelSanPhamActionPerformed(evt);
            }
        });

        buttonGroup2.add(rdoMauSacPanelSanPham);
        rdoMauSacPanelSanPham.setText("Màu Sắc");
        rdoMauSacPanelSanPham.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoMauSacPanelSanPhamActionPerformed(evt);
            }
        });

        buttonGroup2.add(rdoKichCoPanelSanPham);
        rdoKichCoPanelSanPham.setText("Kích Cỡ");
        rdoKichCoPanelSanPham.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoKichCoPanelSanPhamActionPerformed(evt);
            }
        });

        buttonGroup2.add(rdoChatLieuPanelSanPham);
        rdoChatLieuPanelSanPham.setText("Chất Liệu");
        rdoChatLieuPanelSanPham.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoChatLieuPanelSanPhamActionPerformed(evt);
            }
        });

        btnThemPanelSanPham.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnThemPanelSanPham.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/add.png"))); // NOI18N
        btnThemPanelSanPham.setText("Thêm");
        btnThemPanelSanPham.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnThemPanelSanPhamActionPerformed(evt);
            }
        });

        btnSuaPanelSanPham.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnSuaPanelSanPham.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/update.png"))); // NOI18N
        btnSuaPanelSanPham.setText("Sửa");
        btnSuaPanelSanPham.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSuaPanelSanPhamActionPerformed(evt);
            }
        });

        btnXoaPanelSanPham.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnXoaPanelSanPham.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/remove.png"))); // NOI18N
        btnXoaPanelSanPham.setText("Xóa");
        btnXoaPanelSanPham.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnXoaPanelSanPhamActionPerformed(evt);
            }
        });

        txtMaThuocTinhPanelSanPham.setEditable(false);

        jLabel62.setText("Mã Thuộc Tính");

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addContainerGap(75, Short.MAX_VALUE)
                .addComponent(jLabel62)
                .addGap(18, 18, 18)
                .addComponent(txtMaThuocTinhPanelSanPham, javax.swing.GroupLayout.PREFERRED_SIZE, 139, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(40, 40, 40)
                .addComponent(jLabel48)
                .addGap(18, 18, 18)
                .addComponent(txtTenThuocTinhPanelSanPham, javax.swing.GroupLayout.PREFERRED_SIZE, 161, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(36, 36, 36)
                .addComponent(rdoLoaiSanPhamPanelSanPham)
                .addGap(46, 46, 46)
                .addComponent(rdoMauSacPanelSanPham)
                .addGap(42, 42, 42)
                .addComponent(rdoKichCoPanelSanPham)
                .addGap(35, 35, 35)
                .addComponent(rdoChatLieuPanelSanPham)
                .addGap(39, 39, 39))
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addGap(147, 147, 147)
                .addComponent(btnThemPanelSanPham)
                .addGap(31, 31, 31)
                .addComponent(btnSuaPanelSanPham)
                .addGap(22, 22, 22)
                .addComponent(btnXoaPanelSanPham)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addGap(34, 34, 34)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel62)
                        .addComponent(txtMaThuocTinhPanelSanPham, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel48)
                        .addComponent(txtTenThuocTinhPanelSanPham, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(rdoLoaiSanPhamPanelSanPham)
                        .addComponent(rdoMauSacPanelSanPham)
                        .addComponent(rdoKichCoPanelSanPham)
                        .addComponent(rdoChatLieuPanelSanPham)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 122, Short.MAX_VALUE)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnThemPanelSanPham)
                    .addComponent(btnSuaPanelSanPham)
                    .addComponent(btnXoaPanelSanPham))
                .addGap(33, 33, 33))
        );

        jPanel11.setBackground(new java.awt.Color(246, 248, 250));
        jPanel11.setBorder(javax.swing.BorderFactory.createTitledBorder("Thông Tin Thuộc Tính"));

        tblThuocTinhPanelSanPham.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "STT", "Mã Loại", "Tên Loại"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblThuocTinhPanelSanPham.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblThuocTinhPanelSanPhamMouseClicked(evt);
            }
        });
        jScrollPane5.setViewportView(tblThuocTinhPanelSanPham);

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane5)
                .addContainerGap())
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel11Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane5, javax.swing.GroupLayout.DEFAULT_SIZE, 363, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(jPanel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addComponent(jPanel11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jTabbedPane2.addTab("Thuộc Tính Sản Phẩm", jPanel9);

        javax.swing.GroupLayout panelSanPhamLayout = new javax.swing.GroupLayout(panelSanPham);
        panelSanPham.setLayout(panelSanPhamLayout);
        panelSanPhamLayout.setHorizontalGroup(
            panelSanPhamLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelSanPhamLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTabbedPane2)
                .addContainerGap())
        );
        panelSanPhamLayout.setVerticalGroup(
            panelSanPhamLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelSanPhamLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTabbedPane2)
                .addContainerGap())
        );

        panelMain.add(panelSanPham, "card3");

        panelHoaDon.setBackground(new java.awt.Color(246, 248, 250));

        jPanel16.setBackground(new java.awt.Color(246, 248, 250));
        jPanel16.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Danh Sách Hóa Đơn", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 1, 12))); // NOI18N

        tblHoaDonPanelHoaDon.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "STT", "Mã Hóa Đơn", "Tên KH", "Tên NV", "Ngày Tạo", "Trạng Thái"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblHoaDonPanelHoaDon.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblHoaDonPanelHoaDonMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tblHoaDonPanelHoaDon);
        if (tblHoaDonPanelHoaDon.getColumnModel().getColumnCount() > 0) {
            tblHoaDonPanelHoaDon.getColumnModel().getColumn(0).setPreferredWidth(20);
        }

        javax.swing.GroupLayout jPanel16Layout = new javax.swing.GroupLayout(jPanel16);
        jPanel16.setLayout(jPanel16Layout);
        jPanel16Layout.setHorizontalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel16Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 536, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel16Layout.setVerticalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        jPanel17.setBackground(new java.awt.Color(246, 248, 250));
        jPanel17.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Giỏ Hàng", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 1, 12))); // NOI18N

        tblGioHangPanelHoaDon.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "STT", "Mã SP", "Tên SP", "Số Lượng", "Đơn Giá", "Được Giảm", "Thành Tiền", "Chọn"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Boolean.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, true, false, false, false, true
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblGioHangPanelHoaDon.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblGioHangPanelHoaDonMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(tblGioHangPanelHoaDon);
        if (tblGioHangPanelHoaDon.getColumnModel().getColumnCount() > 0) {
            tblGioHangPanelHoaDon.getColumnModel().getColumn(0).setPreferredWidth(20);
            tblGioHangPanelHoaDon.getColumnModel().getColumn(7).setPreferredWidth(10);
        }

        btnXoaGioHangPanelHoaDon.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnXoaGioHangPanelHoaDon.setText("Xóa");
        btnXoaGioHangPanelHoaDon.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnXoaGioHangPanelHoaDonActionPerformed(evt);
            }
        });

        btnSuaGioHangPanelHoaDon.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnSuaGioHangPanelHoaDon.setText("Sửa");
        btnSuaGioHangPanelHoaDon.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSuaGioHangPanelHoaDonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel17Layout = new javax.swing.GroupLayout(jPanel17);
        jPanel17.setLayout(jPanel17Layout);
        jPanel17Layout.setHorizontalGroup(
            jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel17Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel17Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(btnSuaGioHangPanelHoaDon)
                        .addGap(35, 35, 35)
                        .addComponent(btnXoaGioHangPanelHoaDon)))
                .addContainerGap())
        );
        jPanel17Layout.setVerticalGroup(
            jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel17Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnXoaGioHangPanelHoaDon)
                    .addComponent(btnSuaGioHangPanelHoaDon))
                .addContainerGap())
        );

        jPanel18.setBackground(new java.awt.Color(246, 248, 250));
        jPanel18.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Danh Sách Sản Phẩm", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 1, 12))); // NOI18N

        tblSanPhamPanelHoaDon.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Mã SP", "Tên SP", "Loại SP", "Kích Cỡ", "Màu Sắc", "Chất Liệu", "Số Lượng", "Giá Bán"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblSanPhamPanelHoaDon.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblSanPhamPanelHoaDonMouseClicked(evt);
            }
        });
        jScrollPane14.setViewportView(tblSanPhamPanelHoaDon);

        javax.swing.GroupLayout jPanel18Layout = new javax.swing.GroupLayout(jPanel18);
        jPanel18.setLayout(jPanel18Layout);
        jPanel18Layout.setHorizontalGroup(
            jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel18Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane14)
                .addContainerGap())
        );
        jPanel18Layout.setVerticalGroup(
            jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel18Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane14, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel19.setBackground(new java.awt.Color(246, 248, 250));
        jPanel19.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Tạo Hóa Đơn", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 1, 12))); // NOI18N

        jPanel20.setBackground(new java.awt.Color(246, 248, 250));

        jLabel18.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel18.setText("Tên KH");

        jLabel19.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel19.setText("SDT");

        jLabel20.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel20.setText("Tổng Tiền");

        jLabel30.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel30.setText("Tiền Khách Đưa");

        jLabel47.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel47.setText("Hình Thức Thanh Toán");

        jLabel63.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel63.setText("Tiền Thừa");

        lblTongTienPanelHoaDon.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblTongTienPanelHoaDon.setForeground(new java.awt.Color(255, 0, 0));
        lblTongTienPanelHoaDon.setText(" ");

        cbbHTTTPanelHoaDon.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Tiền Mặt", "Chuyển Khoản" }));
        cbbHTTTPanelHoaDon.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbbHTTTPanelHoaDonActionPerformed(evt);
            }
        });

        txtTienKhachDuaPanelHoaDon.addCaretListener(new javax.swing.event.CaretListener() {
            public void caretUpdate(javax.swing.event.CaretEvent evt) {
                txtTienKhachDuaPanelHoaDonCaretUpdate(evt);
            }
        });

        lblTienThuaPanelHoaDon.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblTienThuaPanelHoaDon.setForeground(new java.awt.Color(255, 0, 0));
        lblTienThuaPanelHoaDon.setText(" ");

        btnThanhToanPanelHoaDon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/pay40.png"))); // NOI18N
        btnThanhToanPanelHoaDon.setText("Thanh Toán");
        btnThanhToanPanelHoaDon.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnThanhToanPanelHoaDonActionPerformed(evt);
            }
        });

        btnTaoHoaDonPanelHoaDon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/add30.png"))); // NOI18N
        btnTaoHoaDonPanelHoaDon.setText("Tạo Hóa Đơn");
        btnTaoHoaDonPanelHoaDon.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTaoHoaDonPanelHoaDonActionPerformed(evt);
            }
        });

        btnHuyPanelHoaDon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/cloes30.png"))); // NOI18N
        btnHuyPanelHoaDon.setText("Hủy");
        btnHuyPanelHoaDon.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnHuyPanelHoaDonActionPerformed(evt);
            }
        });

        jLabel76.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel76.setText("Ghi chú");

        txtGhiChuHoaDon.setColumns(20);
        txtGhiChuHoaDon.setRows(5);
        jScrollPane15.setViewportView(txtGhiChuHoaDon);

        btnTimKiemKhachHangPanelHoaDon.setText("Tìm Kiếm");
        btnTimKiemKhachHangPanelHoaDon.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTimKiemKhachHangPanelHoaDonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel20Layout = new javax.swing.GroupLayout(jPanel20);
        jPanel20.setLayout(jPanel20Layout);
        jPanel20Layout.setHorizontalGroup(
            jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSeparator1)
            .addComponent(jSeparator2, javax.swing.GroupLayout.Alignment.TRAILING)
            .addGroup(jPanel20Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(btnTimKiemKhachHangPanelHoaDon)
                        .addGroup(jPanel20Layout.createSequentialGroup()
                            .addGroup(jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel18)
                                .addComponent(jLabel19))
                            .addGap(18, 18, 18)
                            .addGroup(jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(txtTenKhachHangPanelHoaDon)
                                .addComponent(txtSDTPanelHoaDon, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addGroup(jPanel20Layout.createSequentialGroup()
                            .addComponent(jLabel76)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jScrollPane15, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(jPanel20Layout.createSequentialGroup()
                            .addGroup(jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel20Layout.createSequentialGroup()
                                        .addComponent(jLabel47)
                                        .addGap(18, 18, 18))
                                    .addGroup(jPanel20Layout.createSequentialGroup()
                                        .addComponent(jLabel20)
                                        .addGap(87, 87, 87)))
                                .addGroup(jPanel20Layout.createSequentialGroup()
                                    .addGroup(jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jLabel30)
                                        .addComponent(jLabel63))
                                    .addGap(55, 55, 55)))
                            .addGroup(jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(lblTongTienPanelHoaDon, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(cbbHTTTPanelHoaDon, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(txtTienKhachDuaPanelHoaDon)
                                .addComponent(lblTienThuaPanelHoaDon, javax.swing.GroupLayout.PREFERRED_SIZE, 163, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(btnThanhToanPanelHoaDon, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 302, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(jPanel20Layout.createSequentialGroup()
                            .addComponent(btnTaoHoaDonPanelHoaDon)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btnHuyPanelHoaDon, javax.swing.GroupLayout.PREFERRED_SIZE, 132, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel20Layout.setVerticalGroup(
            jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel20Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel18)
                    .addComponent(txtTenKhachHangPanelHoaDon, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel19)
                    .addComponent(txtSDTPanelHoaDon, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(btnTimKiemKhachHangPanelHoaDon)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 49, Short.MAX_VALUE)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel20)
                    .addComponent(lblTongTienPanelHoaDon))
                .addGap(35, 35, 35)
                .addGroup(jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel47)
                    .addComponent(cbbHTTTPanelHoaDon, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(35, 35, 35)
                .addGroup(jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel30)
                    .addComponent(txtTienKhachDuaPanelHoaDon, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(29, 29, 29)
                .addGroup(jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel63)
                    .addComponent(lblTienThuaPanelHoaDon))
                .addGroup(jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel20Layout.createSequentialGroup()
                        .addGap(28, 28, 28)
                        .addComponent(jScrollPane15, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel20Layout.createSequentialGroup()
                        .addGap(48, 48, 48)
                        .addComponent(jLabel76)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnThanhToanPanelHoaDon, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnTaoHoaDonPanelHoaDon, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnHuyPanelHoaDon, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        tabbedPaneDatHang.addTab("Hóa Đơn", jPanel20);

        jPanel21.setBackground(new java.awt.Color(246, 248, 250));

        jLabel78.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel78.setText("Tên KH");

        jLabel79.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel79.setText("SDT");

        jLabel80.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel80.setText("Tổng Tiền");

        jLabel81.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel81.setText("Tiền Khách Đưa");

        jLabel82.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel82.setText("Hình Thức Thanh Toán");

        jLabel83.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel83.setText("Tiền Thừa");

        lblTongTienPanelDatHang.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblTongTienPanelDatHang.setForeground(new java.awt.Color(255, 0, 0));
        lblTongTienPanelDatHang.setText(" ");

        cbbHTTTPanelDatHang.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Tiền Mặt", "Chuyển Khoản" }));
        cbbHTTTPanelDatHang.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbbHTTTPanelDatHangActionPerformed(evt);
            }
        });

        txtTienKhachDuaPanelDatHang.addCaretListener(new javax.swing.event.CaretListener() {
            public void caretUpdate(javax.swing.event.CaretEvent evt) {
                txtTienKhachDuaPanelDatHangCaretUpdate(evt);
            }
        });

        lblTienThuaPanelDatHang.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblTienThuaPanelDatHang.setForeground(new java.awt.Color(255, 0, 0));
        lblTienThuaPanelDatHang.setText(" ");

        btnDangGiaoPanelGiaoHang.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/danggiao.png"))); // NOI18N
        btnDangGiaoPanelGiaoHang.setText("Giao Hàng");
        btnDangGiaoPanelGiaoHang.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDangGiaoPanelGiaoHangActionPerformed(evt);
            }
        });

        btnTaoHoaDonPanelDatHang.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/add30.png"))); // NOI18N
        btnTaoHoaDonPanelDatHang.setText("Tạo Hóa Đơn");
        btnTaoHoaDonPanelDatHang.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTaoHoaDonPanelDatHangActionPerformed(evt);
            }
        });

        jLabel84.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel84.setText("Ghi chú");

        txtDiaChiKhachHang.setColumns(20);
        txtDiaChiKhachHang.setRows(5);
        jScrollPane16.setViewportView(txtDiaChiKhachHang);

        jLabel85.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel85.setText("Địa Chỉ");

        txtGhiChuPanelDatHang.setColumns(20);
        txtGhiChuPanelDatHang.setRows(5);
        jScrollPane17.setViewportView(txtGhiChuPanelDatHang);

        btnDaGiaoPanelGiaoHang.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/dagiao.png"))); // NOI18N
        btnDaGiaoPanelGiaoHang.setText("Đã Giao");
        btnDaGiaoPanelGiaoHang.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDaGiaoPanelGiaoHangActionPerformed(evt);
            }
        });

        btnTimKiemPanelDatHang.setText("Tìm Kiếm");
        btnTimKiemPanelDatHang.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTimKiemPanelDatHangActionPerformed(evt);
            }
        });

        btnTraHangPanelGiaoHang.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/trahang.png"))); // NOI18N
        btnTraHangPanelGiaoHang.setText("Trả Hàng");
        btnTraHangPanelGiaoHang.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTraHangPanelGiaoHangActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel21Layout = new javax.swing.GroupLayout(jPanel21);
        jPanel21.setLayout(jPanel21Layout);
        jPanel21Layout.setHorizontalGroup(
            jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel21Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel21Layout.createSequentialGroup()
                        .addGroup(jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jSeparator3)
                            .addComponent(jSeparator4, javax.swing.GroupLayout.Alignment.TRAILING))
                        .addContainerGap())
                    .addGroup(jPanel21Layout.createSequentialGroup()
                        .addGroup(jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(jPanel21Layout.createSequentialGroup()
                                    .addGroup(jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jLabel78)
                                        .addComponent(jLabel79)
                                        .addComponent(jLabel85))
                                    .addGap(18, 18, 18)
                                    .addGroup(jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(txtTenKhachHangPanelDatHang)
                                        .addComponent(txtSDTPanelDatHang)
                                        .addComponent(jScrollPane16, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addComponent(btnTimKiemPanelDatHang, javax.swing.GroupLayout.Alignment.TRAILING))
                            .addGroup(jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addGroup(jPanel21Layout.createSequentialGroup()
                                    .addComponent(jLabel84)
                                    .addGap(18, 18, 18)
                                    .addComponent(jScrollPane17))
                                .addGroup(jPanel21Layout.createSequentialGroup()
                                    .addGroup(jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel21Layout.createSequentialGroup()
                                                .addComponent(jLabel82)
                                                .addGap(18, 18, 18))
                                            .addGroup(jPanel21Layout.createSequentialGroup()
                                                .addGroup(jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                    .addComponent(jLabel81)
                                                    .addComponent(jLabel83))
                                                .addGap(55, 55, 55)))
                                        .addGroup(jPanel21Layout.createSequentialGroup()
                                            .addComponent(jLabel80)
                                            .addGap(87, 87, 87)))
                                    .addGroup(jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(lblTongTienPanelDatHang, javax.swing.GroupLayout.PREFERRED_SIZE, 163, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGroup(jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                            .addComponent(cbbHTTTPanelDatHang, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(txtTienKhachDuaPanelDatHang)
                                            .addComponent(lblTienThuaPanelDatHang, javax.swing.GroupLayout.PREFERRED_SIZE, 163, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                            .addGroup(jPanel21Layout.createSequentialGroup()
                                .addGroup(jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(btnTaoHoaDonPanelDatHang, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(btnDangGiaoPanelGiaoHang, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addGap(37, 37, 37)
                                .addGroup(jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(btnDaGiaoPanelGiaoHang, javax.swing.GroupLayout.PREFERRED_SIZE, 132, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(btnTraHangPanelGiaoHang, javax.swing.GroupLayout.PREFERRED_SIZE, 132, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addGap(0, 6, Short.MAX_VALUE))))
        );
        jPanel21Layout.setVerticalGroup(
            jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel21Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel78)
                    .addComponent(txtTenKhachHangPanelDatHang, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel79)
                    .addComponent(txtSDTPanelDatHang, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(22, 22, 22)
                .addGroup(jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel85)
                    .addComponent(jScrollPane16, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 40, Short.MAX_VALUE)
                .addComponent(btnTimKiemPanelDatHang)
                .addGap(18, 18, 18)
                .addComponent(jSeparator3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel80)
                    .addComponent(lblTongTienPanelDatHang))
                .addGap(18, 18, 18)
                .addGroup(jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cbbHTTTPanelDatHang, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel82))
                .addGap(18, 18, 18)
                .addGroup(jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtTienKhachDuaPanelDatHang, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel81))
                .addGap(26, 26, 26)
                .addGroup(jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblTienThuaPanelDatHang)
                    .addComponent(jLabel83))
                .addGroup(jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel21Layout.createSequentialGroup()
                        .addGap(44, 44, 44)
                        .addComponent(jLabel84)
                        .addGap(38, 38, 38))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel21Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane17, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)))
                .addComponent(jSeparator4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnDangGiaoPanelGiaoHang, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnDaGiaoPanelGiaoHang, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(32, 32, 32)
                .addGroup(jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnTaoHoaDonPanelDatHang, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnTraHangPanelGiaoHang, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        tabbedPaneDatHang.addTab("Đặt Hàng", jPanel21);

        javax.swing.GroupLayout jPanel19Layout = new javax.swing.GroupLayout(jPanel19);
        jPanel19.setLayout(jPanel19Layout);
        jPanel19Layout.setHorizontalGroup(
            jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel19Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(tabbedPaneDatHang)
                .addContainerGap())
        );
        jPanel19Layout.setVerticalGroup(
            jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel19Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(tabbedPaneDatHang)
                .addContainerGap())
        );

        jPanel22.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        javax.swing.GroupLayout CameraLayout = new javax.swing.GroupLayout(Camera);
        Camera.setLayout(CameraLayout);
        CameraLayout.setHorizontalGroup(
            CameraLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 207, Short.MAX_VALUE)
        );
        CameraLayout.setVerticalGroup(
            CameraLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 140, Short.MAX_VALUE)
        );

        jPanel22.add(Camera, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, 140));

        javax.swing.GroupLayout panelHoaDonLayout = new javax.swing.GroupLayout(panelHoaDon);
        panelHoaDon.setLayout(panelHoaDonLayout);
        panelHoaDonLayout.setHorizontalGroup(
            panelHoaDonLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelHoaDonLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelHoaDonLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(panelHoaDonLayout.createSequentialGroup()
                        .addComponent(jPanel16, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jPanel22, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jPanel17, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel18, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addComponent(jPanel19, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        panelHoaDonLayout.setVerticalGroup(
            panelHoaDonLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelHoaDonLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelHoaDonLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel19, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(panelHoaDonLayout.createSequentialGroup()
                        .addGroup(panelHoaDonLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jPanel16, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(panelHoaDonLayout.createSequentialGroup()
                                .addGap(8, 8, 8)
                                .addComponent(jPanel22, javax.swing.GroupLayout.PREFERRED_SIZE, 137, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel17, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jPanel18, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addGap(18, 18, 18))
        );

        panelMain.add(panelHoaDon, "card3");

        panelLichSu.setBackground(new java.awt.Color(255, 255, 255));

        txtMoTaPanelLichSu.setColumns(20);
        txtMoTaPanelLichSu.setRows(5);
        jScrollPane8.setViewportView(txtMoTaPanelLichSu);

        jLabel33.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel33.setText("Tên Nhân Viên");

        jLabel34.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel34.setText("Tên Khách Hàng");

        jLabel35.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel35.setText("Số Điện Thoại");

        jLabel36.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel36.setText("Địa Chỉ");

        jLabel37.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel37.setText("Tổng Tiền Hàng");

        jLabel38.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel38.setText("Ngày Tạo");

        jLabel40.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel40.setText("Mô Tả");

        txtDiaChiPanelLichSu.setColumns(20);
        txtDiaChiPanelLichSu.setRows(5);
        jScrollPane7.setViewportView(txtDiaChiPanelLichSu);

        jLabel32.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel32.setText("Mã Nhân Viên");

        jPanel12.setBackground(new java.awt.Color(246, 248, 250));
        jPanel12.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Danh Sách Hóa Đơn", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 1, 12))); // NOI18N

        tblHoaDonPanelLichSu.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "STT", "Mã Hóa Đơn", "Tên Nhân Viên", "Tên Khách Hàng", "Ngày Tạo", "Trạng Thái"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblHoaDonPanelLichSu.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblHoaDonPanelLichSuMouseClicked(evt);
            }
        });
        jScrollPane9.setViewportView(tblHoaDonPanelLichSu);

        javax.swing.GroupLayout jPanel12Layout = new javax.swing.GroupLayout(jPanel12);
        jPanel12.setLayout(jPanel12Layout);
        jPanel12Layout.setHorizontalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel12Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane9, javax.swing.GroupLayout.DEFAULT_SIZE, 819, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel12Layout.setVerticalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane9, javax.swing.GroupLayout.DEFAULT_SIZE, 325, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel13.setBackground(new java.awt.Color(246, 248, 250));
        jPanel13.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Thông Tin Sản Phẩm", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 1, 12))); // NOI18N

        tblSanPhamPanelLichSu.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "STT", "Mã SP", "Tên SP", "Loại SP", "Kích Cỡ", "Màu Sắc", "Chất Liệu", "Số Lượng", "Đơn Giá", "Thành Tiền"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Object.class, java.lang.String.class, java.lang.String.class, java.lang.Object.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane10.setViewportView(tblSanPhamPanelLichSu);

        javax.swing.GroupLayout jPanel13Layout = new javax.swing.GroupLayout(jPanel13);
        jPanel13.setLayout(jPanel13Layout);
        jPanel13Layout.setHorizontalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane10)
                .addContainerGap())
        );
        jPanel13Layout.setVerticalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addComponent(jScrollPane10, javax.swing.GroupLayout.DEFAULT_SIZE, 216, Short.MAX_VALUE)
                .addContainerGap())
        );

        lblMaNVPanelLichSu.setText(" ");

        lblTenNVPanelLichSu.setText(" ");

        lblTenKHPanelLichSu.setText(" ");

        lblSDTPanelLichSu.setText(" ");

        lblTongTienPanelLichSu.setText(" ");

        lblNgayTaoPanelLichSu.setText(" ");

        jPanel25.setBackground(new java.awt.Color(246, 248, 250));
        jPanel25.setBorder(javax.swing.BorderFactory.createTitledBorder("Tìm Kiếm Mã Hóa Đơn"));

        btnTimKiemMaHDPanelLichSu.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnTimKiemMaHDPanelLichSu.setText("Tìm Kiếm");
        btnTimKiemMaHDPanelLichSu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTimKiemMaHDPanelLichSuActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel25Layout = new javax.swing.GroupLayout(jPanel25);
        jPanel25.setLayout(jPanel25Layout);
        jPanel25Layout.setHorizontalGroup(
            jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel25Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(txtMaHoaDonPanelLichSua)
                .addContainerGap())
            .addGroup(jPanel25Layout.createSequentialGroup()
                .addGap(94, 94, 94)
                .addComponent(btnTimKiemMaHDPanelLichSu, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel25Layout.setVerticalGroup(
            jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel25Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(txtMaHoaDonPanelLichSua, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(28, 28, 28)
                .addComponent(btnTimKiemMaHDPanelLichSu, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout panelLichSuLayout = new javax.swing.GroupLayout(panelLichSu);
        panelLichSu.setLayout(panelLichSuLayout);
        panelLichSuLayout.setHorizontalGroup(
            panelLichSuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelLichSuLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelLichSuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jPanel13, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(panelLichSuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelLichSuLayout.createSequentialGroup()
                        .addComponent(jLabel36)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 70, Short.MAX_VALUE)
                        .addComponent(jScrollPane7, javax.swing.GroupLayout.PREFERRED_SIZE, 216, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane8)
                    .addGroup(panelLichSuLayout.createSequentialGroup()
                        .addComponent(jLabel34)
                        .addGap(26, 26, 26)
                        .addComponent(lblTenKHPanelLichSu, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(panelLichSuLayout.createSequentialGroup()
                        .addComponent(jLabel35)
                        .addGap(40, 40, 40)
                        .addComponent(lblSDTPanelLichSu, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(panelLichSuLayout.createSequentialGroup()
                        .addComponent(jLabel37)
                        .addGap(28, 28, 28)
                        .addComponent(lblTongTienPanelLichSu, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(panelLichSuLayout.createSequentialGroup()
                        .addComponent(jLabel38)
                        .addGap(63, 63, 63)
                        .addComponent(lblNgayTaoPanelLichSu, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(panelLichSuLayout.createSequentialGroup()
                        .addComponent(jLabel33)
                        .addGap(35, 35, 35)
                        .addComponent(lblTenNVPanelLichSu, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(panelLichSuLayout.createSequentialGroup()
                        .addComponent(jLabel32)
                        .addGap(38, 38, 38)
                        .addComponent(lblMaNVPanelLichSu, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(panelLichSuLayout.createSequentialGroup()
                        .addComponent(jLabel40)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jPanel25, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(17, 17, 17))
        );
        panelLichSuLayout.setVerticalGroup(
            panelLichSuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelLichSuLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(panelLichSuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addGroup(panelLichSuLayout.createSequentialGroup()
                        .addComponent(jPanel12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(20, 20, 20)
                        .addComponent(jPanel13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panelLichSuLayout.createSequentialGroup()
                        .addComponent(jPanel25, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(18, 18, 18)
                        .addGroup(panelLichSuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel32)
                            .addComponent(lblMaNVPanelLichSu))
                        .addGap(18, 18, 18)
                        .addGroup(panelLichSuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel33)
                            .addComponent(lblTenNVPanelLichSu))
                        .addGap(18, 18, 18)
                        .addGroup(panelLichSuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel34)
                            .addComponent(lblTenKHPanelLichSu))
                        .addGap(18, 18, 18)
                        .addGroup(panelLichSuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel35)
                            .addComponent(lblSDTPanelLichSu))
                        .addGap(30, 30, 30)
                        .addGroup(panelLichSuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel36)
                            .addComponent(jScrollPane7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(23, 23, 23)
                        .addGroup(panelLichSuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel37)
                            .addComponent(lblTongTienPanelLichSu))
                        .addGap(26, 26, 26)
                        .addGroup(panelLichSuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel38)
                            .addComponent(lblNgayTaoPanelLichSu))
                        .addGap(20, 20, 20)
                        .addComponent(jLabel40)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jScrollPane8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(6, 6, 6)))
                .addGap(106, 106, 106))
        );

        panelMain.add(panelLichSu, "card9");

        panelKhuyenMai.setBackground(new java.awt.Color(246, 248, 250));

        jPanel1.setBackground(new java.awt.Color(246, 248, 250));
        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Khuyến Mãi"));

        jLabel70.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel70.setText("Mã Khuyến Mãi");

        txtMaKhuyenMaiPanelKhuyenMai.setEditable(false);

        jLabel71.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel71.setText("Tên Chương Trình");

        jLabel72.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel72.setText("Ngày Bắt Đầu");

        txtNgayBatDauPanelKhuyenMai.setDateFormatString("yyyy-MM-dd");

        txtNgayKetThucPanelKhuyenMai.setDateFormatString("yyyy-MM-dd");

        jLabel73.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel73.setText("Ngày Kết Thúc");

        jLabel74.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel74.setText("Phần Trăm Khuyến Mãi");

        spnPhanTramKhuyenMaiPanelKhuyenMai.setModel(new javax.swing.SpinnerNumberModel(0, 0, 100, 1));

        btnThemPanelKhuyenMai.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnThemPanelKhuyenMai.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/add.png"))); // NOI18N
        btnThemPanelKhuyenMai.setText("Thêm");
        btnThemPanelKhuyenMai.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnThemPanelKhuyenMaiActionPerformed(evt);
            }
        });

        btnSuaPanelKhuyenMai.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnSuaPanelKhuyenMai.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/update.png"))); // NOI18N
        btnSuaPanelKhuyenMai.setText("Sửa");
        btnSuaPanelKhuyenMai.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSuaPanelKhuyenMaiActionPerformed(evt);
            }
        });

        btnAnPanelKhuyenMai.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnAnPanelKhuyenMai.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/remove.png"))); // NOI18N
        btnAnPanelKhuyenMai.setText("Kết Thúc Trương Trình");
        btnAnPanelKhuyenMai.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAnPanelKhuyenMaiActionPerformed(evt);
            }
        });

        btnXoaFormPanelKhuyenMai.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnXoaFormPanelKhuyenMai.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/clear.png"))); // NOI18N
        btnXoaFormPanelKhuyenMai.setText("Xóa Form");
        btnXoaFormPanelKhuyenMai.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnXoaFormPanelKhuyenMaiActionPerformed(evt);
            }
        });

        btnXemKhuyenMaiPanelKhuyenMai.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnXemKhuyenMaiPanelKhuyenMai.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/an.png"))); // NOI18N
        btnXemKhuyenMaiPanelKhuyenMai.setText("Xem Chương Trình Đã Kết Thúc");
        btnXemKhuyenMaiPanelKhuyenMai.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnXemKhuyenMaiPanelKhuyenMaiActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSeparator8)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(19, 19, 19)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel70)
                                    .addComponent(jLabel71)
                                    .addComponent(jLabel72)
                                    .addComponent(jLabel73))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(txtNgayKetThucPanelKhuyenMai, javax.swing.GroupLayout.DEFAULT_SIZE, 166, Short.MAX_VALUE)
                                    .addComponent(txtNgayBatDauPanelKhuyenMai, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(txtTenChuongTrinhPanelKhuyenMai)
                                    .addComponent(txtMaKhuyenMaiPanelKhuyenMai)))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel74)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(spnPhanTramKhuyenMaiPanelKhuyenMai, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(16, 16, 16)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnXoaFormPanelKhuyenMai, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(btnThemPanelKhuyenMai, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 79, Short.MAX_VALUE)
                                .addComponent(btnSuaPanelKhuyenMai, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(btnAnPanelKhuyenMai, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btnXemKhuyenMaiPanelKhuyenMai, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel70)
                    .addComponent(txtMaKhuyenMaiPanelKhuyenMai, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(36, 36, 36)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel71)
                    .addComponent(txtTenChuongTrinhPanelKhuyenMai, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(35, 35, 35)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel72)
                    .addComponent(txtNgayBatDauPanelKhuyenMai, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(41, 41, 41)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel73)
                    .addComponent(txtNgayKetThucPanelKhuyenMai, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(51, 51, 51)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel74)
                    .addComponent(spnPhanTramKhuyenMaiPanelKhuyenMai, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(46, 46, 46)
                .addComponent(jSeparator8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnThemPanelKhuyenMai)
                    .addComponent(btnSuaPanelKhuyenMai))
                .addGap(30, 30, 30)
                .addComponent(btnXoaFormPanelKhuyenMai)
                .addGap(18, 18, 18)
                .addComponent(btnAnPanelKhuyenMai)
                .addGap(18, 18, 18)
                .addComponent(btnXemKhuyenMaiPanelKhuyenMai)
                .addContainerGap(61, Short.MAX_VALUE))
        );

        jPanel23.setBackground(new java.awt.Color(246, 248, 250));
        jPanel23.setBorder(javax.swing.BorderFactory.createTitledBorder("Danh Sách Khuyến Mãi"));

        tblKhuyenMaiPanelKhuyenMai.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "STT", "Mã Khuyến Mãi", "Tên Chương Trình", "Ngày Bắt Đầu", "Ngày Kết Thúc", "Khuyến Mãi \"%\""
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblKhuyenMaiPanelKhuyenMai.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblKhuyenMaiPanelKhuyenMaiMouseClicked(evt);
            }
        });
        jScrollPane13.setViewportView(tblKhuyenMaiPanelKhuyenMai);
        if (tblKhuyenMaiPanelKhuyenMai.getColumnModel().getColumnCount() > 0) {
            tblKhuyenMaiPanelKhuyenMai.getColumnModel().getColumn(0).setPreferredWidth(30);
        }

        javax.swing.GroupLayout jPanel23Layout = new javax.swing.GroupLayout(jPanel23);
        jPanel23.setLayout(jPanel23Layout);
        jPanel23Layout.setHorizontalGroup(
            jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel23Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane13)
                .addContainerGap())
        );
        jPanel23Layout.setVerticalGroup(
            jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel23Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane13, javax.swing.GroupLayout.DEFAULT_SIZE, 240, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel24.setBackground(new java.awt.Color(246, 248, 250));
        jPanel24.setBorder(javax.swing.BorderFactory.createTitledBorder("Danh Sách Sản Phẩm"));

        tblSanPhamPanelKhuyenMai.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "STT", "Mã SP", "Tên SP", "Loại SP", "Màu Sắc", "Kích Cỡ", "Chất Liệu", "Chọn"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Boolean.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, true
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblSanPhamPanelKhuyenMai.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblSanPhamPanelKhuyenMaiMouseClicked(evt);
            }
        });
        jScrollPane20.setViewportView(tblSanPhamPanelKhuyenMai);
        if (tblSanPhamPanelKhuyenMai.getColumnModel().getColumnCount() > 0) {
            tblSanPhamPanelKhuyenMai.getColumnModel().getColumn(0).setPreferredWidth(30);
            tblSanPhamPanelKhuyenMai.getColumnModel().getColumn(7).setPreferredWidth(20);
        }

        btnChonTatCaPanelKhuyenMai.setText("Chọn Tất Cả");
        btnChonTatCaPanelKhuyenMai.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnChonTatCaPanelKhuyenMaiActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel24Layout = new javax.swing.GroupLayout(jPanel24);
        jPanel24.setLayout(jPanel24Layout);
        jPanel24Layout.setHorizontalGroup(
            jPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel24Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane20, javax.swing.GroupLayout.DEFAULT_SIZE, 807, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel24Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(btnChonTatCaPanelKhuyenMai)))
                .addContainerGap())
        );
        jPanel24Layout.setVerticalGroup(
            jPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel24Layout.createSequentialGroup()
                .addComponent(btnChonTatCaPanelKhuyenMai)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jScrollPane20, javax.swing.GroupLayout.PREFERRED_SIZE, 294, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(41, 41, 41))
        );

        javax.swing.GroupLayout panelKhuyenMaiLayout = new javax.swing.GroupLayout(panelKhuyenMai);
        panelKhuyenMai.setLayout(panelKhuyenMaiLayout);
        panelKhuyenMaiLayout.setHorizontalGroup(
            panelKhuyenMaiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelKhuyenMaiLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelKhuyenMaiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel23, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel24, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        panelKhuyenMaiLayout.setVerticalGroup(
            panelKhuyenMaiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelKhuyenMaiLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelKhuyenMaiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(panelKhuyenMaiLayout.createSequentialGroup()
                        .addComponent(jPanel23, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jPanel24, javax.swing.GroupLayout.PREFERRED_SIZE, 375, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        panelMain.add(panelKhuyenMai, "card6");

        panelNhanVien.setBackground(new java.awt.Color(246, 248, 250));

        buttonGroup3.add(rdoNamPanelNhanVien);
        rdoNamPanelNhanVien.setText("Nam");

        buttonGroup3.add(rdoNuPanelNhanVien);
        rdoNuPanelNhanVien.setText("Nữ");

        jLabel21.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel21.setText("Mã NV");

        jLabel22.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel22.setText("Họ");

        jLabel23.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel23.setText("Tên");

        jLabel24.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel24.setText("Giới Tính");

        jLabel25.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel25.setText("Ngày Sinh");

        jLabel26.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel26.setText("SDT");

        jLabel27.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel27.setText("Email");

        jLabel28.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel28.setText("Chức Vụ");

        txtMaNhanVienPanelNhanVien.setEditable(false);

        cbbChucVuPanelNhanVien.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Nhân Viên", "Quản Lý" }));

        txtDiaChiPanelNhanVien.setColumns(20);
        txtDiaChiPanelNhanVien.setRows(5);
        jScrollPane4.setViewportView(txtDiaChiPanelNhanVien);

        jLabel29.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel29.setText("Địa Chỉ");

        jLabel31.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel31.setText("Mật Khẩu");

        btnXemNhanVienDaNghiPanelNV.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnXemNhanVienDaNghiPanelNV.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/an.png"))); // NOI18N
        btnXemNhanVienDaNghiPanelNV.setText("Xem Nhân Viên Đã Nghỉ");
        btnXemNhanVienDaNghiPanelNV.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnXemNhanVienDaNghiPanelNVActionPerformed(evt);
            }
        });

        txtNgaySinhNhanVienPanelNhanVien.setDateFormatString("yyyy-MM-dd"); // NOI18N

        btnXoaFormPanelNhanVien.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnXoaFormPanelNhanVien.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/clear.png"))); // NOI18N
        btnXoaFormPanelNhanVien.setText("Xóa Form");
        btnXoaFormPanelNhanVien.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnXoaFormPanelNhanVienActionPerformed(evt);
            }
        });

        btnThemPanelNhanVien.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnThemPanelNhanVien.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/add.png"))); // NOI18N
        btnThemPanelNhanVien.setText("Thêm");
        btnThemPanelNhanVien.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnThemPanelNhanVienActionPerformed(evt);
            }
        });

        btnSuaPanelNhanVien.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnSuaPanelNhanVien.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/update.png"))); // NOI18N
        btnSuaPanelNhanVien.setText("Sửa");
        btnSuaPanelNhanVien.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSuaPanelNhanVienActionPerformed(evt);
            }
        });

        btnAnPanelNhanVien.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnAnPanelNhanVien.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/remove.png"))); // NOI18N
        btnAnPanelNhanVien.setText("Ẩn");
        btnAnPanelNhanVien.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAnPanelNhanVienActionPerformed(evt);
            }
        });

        tblNhanVienPanelNhanVien.setModel(new javax.swing.table.DefaultTableModel(
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
        tblNhanVienPanelNhanVien.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblNhanVienPanelNhanVienMouseClicked(evt);
            }
        });
        jScrollPane3.setViewportView(tblNhanVienPanelNhanVien);

        javax.swing.GroupLayout panelNhanVienLayout = new javax.swing.GroupLayout(panelNhanVien);
        panelNhanVien.setLayout(panelNhanVienLayout);
        panelNhanVienLayout.setHorizontalGroup(
            panelNhanVienLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelNhanVienLayout.createSequentialGroup()
                .addGroup(panelNhanVienLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelNhanVienLayout.createSequentialGroup()
                        .addGap(17, 17, 17)
                        .addGroup(panelNhanVienLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(panelNhanVienLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(jLabel23, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel22, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel21))
                            .addComponent(jLabel24))
                        .addGap(52, 52, 52)
                        .addGroup(panelNhanVienLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(panelNhanVienLayout.createSequentialGroup()
                                .addGroup(panelNhanVienLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txtHoPanelNhanVien, javax.swing.GroupLayout.PREFERRED_SIZE, 152, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtTenPanelNhanVien, javax.swing.GroupLayout.PREFERRED_SIZE, 152, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtMaNhanVienPanelNhanVien, javax.swing.GroupLayout.PREFERRED_SIZE, 152, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(34, 34, 34))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelNhanVienLayout.createSequentialGroup()
                                .addGap(10, 10, 10)
                                .addComponent(rdoNamPanelNhanVien)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(rdoNuPanelNhanVien)
                                .addGap(45, 45, 45)))
                        .addGap(102, 102, 102)
                        .addGroup(panelNhanVienLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel26)
                            .addComponent(jLabel27)
                            .addComponent(jLabel28)
                            .addComponent(jLabel25))
                        .addGap(48, 48, 48)
                        .addGroup(panelNhanVienLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtNgaySinhNhanVienPanelNhanVien, javax.swing.GroupLayout.DEFAULT_SIZE, 206, Short.MAX_VALUE)
                            .addComponent(txtSDTPanelNhanVien)
                            .addComponent(txtEmailPanelNhanVien)
                            .addComponent(cbbChucVuPanelNhanVien, 0, 206, Short.MAX_VALUE))
                        .addGap(45, 45, 45))
                    .addGroup(panelNhanVienLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(btnXoaFormPanelNhanVien)
                        .addGap(69, 69, 69)
                        .addComponent(btnThemPanelNhanVien)
                        .addGap(31, 31, 31)
                        .addComponent(btnSuaPanelNhanVien)
                        .addGap(22, 22, 22)
                        .addComponent(btnAnPanelNhanVien)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addGroup(panelNhanVienLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelNhanVienLayout.createSequentialGroup()
                        .addGap(224, 224, 224)
                        .addComponent(btnXemNhanVienDaNghiPanelNV))
                    .addGroup(panelNhanVienLayout.createSequentialGroup()
                        .addGroup(panelNhanVienLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel29)
                            .addComponent(jLabel31))
                        .addGap(41, 41, 41)
                        .addGroup(panelNhanVienLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 272, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtMatKhauPanelNhanVien, javax.swing.GroupLayout.PREFERRED_SIZE, 163, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(24, 24, 24))
            .addGroup(panelNhanVienLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelNhanVienLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane3)
                    .addComponent(jSeparator10))
                .addContainerGap())
        );
        panelNhanVienLayout.setVerticalGroup(
            panelNhanVienLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelNhanVienLayout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addGroup(panelNhanVienLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelNhanVienLayout.createSequentialGroup()
                        .addGroup(panelNhanVienLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(txtNgaySinhNhanVienPanelNhanVien, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(panelNhanVienLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel21)
                                .addComponent(jLabel25)
                                .addComponent(txtMaNhanVienPanelNhanVien, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(36, 36, 36)
                        .addGroup(panelNhanVienLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelNhanVienLayout.createSequentialGroup()
                                .addGroup(panelNhanVienLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel22)
                                    .addComponent(jLabel26)
                                    .addComponent(txtHoPanelNhanVien, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(22, 22, 22)
                                .addGroup(panelNhanVienLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel23)
                                    .addComponent(jLabel27)
                                    .addComponent(txtTenPanelNhanVien, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(26, 26, 26)
                                .addGroup(panelNhanVienLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel24)
                                    .addComponent(jLabel28)
                                    .addComponent(rdoNamPanelNhanVien)
                                    .addComponent(rdoNuPanelNhanVien)))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelNhanVienLayout.createSequentialGroup()
                                .addComponent(txtSDTPanelNhanVien, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(22, 22, 22)
                                .addGroup(panelNhanVienLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(txtEmailPanelNhanVien, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel31)
                                    .addComponent(txtMatKhauPanelNhanVien, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(26, 26, 26)
                                .addComponent(cbbChucVuPanelNhanVien, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(panelNhanVienLayout.createSequentialGroup()
                        .addGroup(panelNhanVienLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel29)
                            .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(110, 110, 110)
                        .addGroup(panelNhanVienLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnXemNhanVienDaNghiPanelNV)
                            .addComponent(btnXoaFormPanelNhanVien)
                            .addComponent(btnThemPanelNhanVien)
                            .addComponent(btnSuaPanelNhanVien)
                            .addComponent(btnAnPanelNhanVien))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 33, Short.MAX_VALUE)
                .addComponent(jSeparator10, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(33, 33, 33)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 378, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(43, 43, 43))
        );

        panelMain.add(panelNhanVien, "card9");

        panelKhachHang.setBackground(new java.awt.Color(246, 248, 250));

        tblKhachHangPanelKhachHang.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "STT", "Mã Khách Hàng", "Họ và Tên", "Email", "SDT"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblKhachHangPanelKhachHang.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblKhachHangPanelKhachHangMouseClicked(evt);
            }
        });
        jScrollPane12.setViewportView(tblKhachHangPanelKhachHang);

        jLabel64.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel64.setText("Mã KH");

        jLabel65.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel65.setText("Họ");

        jLabel66.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel66.setText("Tên");

        txtMaKhachHangPanelKhachHang.setEditable(false);

        jLabel67.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel67.setText("SDT");

        jLabel68.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel68.setText("Email");

        btnXoaFormPanelKhachHang.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnXoaFormPanelKhachHang.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/clear.png"))); // NOI18N
        btnXoaFormPanelKhachHang.setText("Xóa Form");
        btnXoaFormPanelKhachHang.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnXoaFormPanelKhachHangActionPerformed(evt);
            }
        });

        btnThemPanelKhachHang.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnThemPanelKhachHang.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/add.png"))); // NOI18N
        btnThemPanelKhachHang.setText("Thêm");
        btnThemPanelKhachHang.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnThemPanelKhachHangActionPerformed(evt);
            }
        });

        btnSuaPanelKhachHang.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnSuaPanelKhachHang.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/update.png"))); // NOI18N
        btnSuaPanelKhachHang.setText("Sửa");
        btnSuaPanelKhachHang.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSuaPanelKhachHangActionPerformed(evt);
            }
        });

        txtTimKiemKhachHangPanelKH.addCaretListener(new javax.swing.event.CaretListener() {
            public void caretUpdate(javax.swing.event.CaretEvent evt) {
                txtTimKiemKhachHangPanelKHCaretUpdate(evt);
            }
        });

        jLabel69.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel69.setText("Tìm Kiếm Bằng SDT");

        javax.swing.GroupLayout panelKhachHangLayout = new javax.swing.GroupLayout(panelKhachHang);
        panelKhachHang.setLayout(panelKhachHangLayout);
        panelKhachHangLayout.setHorizontalGroup(
            panelKhachHangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelKhachHangLayout.createSequentialGroup()
                .addGap(31, 31, 31)
                .addGroup(panelKhachHangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelKhachHangLayout.createSequentialGroup()
                        .addComponent(btnXoaFormPanelKhachHang)
                        .addGap(69, 69, 69)
                        .addComponent(btnThemPanelKhachHang)
                        .addGap(31, 31, 31)
                        .addComponent(btnSuaPanelKhachHang)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(panelKhachHangLayout.createSequentialGroup()
                        .addGroup(panelKhachHangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jLabel66, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel65, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel64))
                        .addGap(52, 52, 52)
                        .addGroup(panelKhachHangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtTenPanelKhachHang, javax.swing.GroupLayout.PREFERRED_SIZE, 152, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(panelKhachHangLayout.createSequentialGroup()
                                .addGroup(panelKhachHangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txtHoPanelKhachHang, javax.swing.GroupLayout.PREFERRED_SIZE, 152, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtMaKhachHangPanelKhachHang, javax.swing.GroupLayout.PREFERRED_SIZE, 152, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(80, 80, 80)
                                .addGroup(panelKhachHangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel67)
                                    .addComponent(jLabel68))
                                .addGap(48, 48, 48)
                                .addGroup(panelKhachHangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(txtSDTPanelKhachHang, javax.swing.GroupLayout.DEFAULT_SIZE, 171, Short.MAX_VALUE)
                                    .addComponent(txtEmailPanelKhachHang))))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 141, Short.MAX_VALUE)
                        .addComponent(jLabel69)
                        .addGap(18, 18, 18)
                        .addComponent(txtTimKiemKhachHangPanelKH, javax.swing.GroupLayout.PREFERRED_SIZE, 302, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(27, 27, 27))))
            .addGroup(panelKhachHangLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelKhachHangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane12)
                    .addComponent(jSeparator9))
                .addContainerGap())
        );
        panelKhachHangLayout.setVerticalGroup(
            panelKhachHangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelKhachHangLayout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addGroup(panelKhachHangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelKhachHangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel64)
                        .addComponent(txtMaKhachHangPanelKhachHang, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panelKhachHangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(txtSDTPanelKhachHang, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel67)
                        .addComponent(txtTimKiemKhachHangPanelKH, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel69)))
                .addGap(18, 18, 18)
                .addGroup(panelKhachHangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel65)
                    .addComponent(txtHoPanelKhachHang, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel68)
                    .addComponent(txtEmailPanelKhachHang, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(22, 22, 22)
                .addGroup(panelKhachHangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel66)
                    .addComponent(txtTenPanelKhachHang, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(panelKhachHangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnXoaFormPanelKhachHang)
                    .addComponent(btnThemPanelKhachHang)
                    .addComponent(btnSuaPanelKhachHang))
                .addGap(27, 27, 27)
                .addComponent(jSeparator9, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 32, Short.MAX_VALUE)
                .addComponent(jScrollPane12, javax.swing.GroupLayout.PREFERRED_SIZE, 457, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(39, 39, 39))
        );

        panelMain.add(panelKhachHang, "card8");

        panelDoiMatKhau.setBackground(new java.awt.Color(246, 248, 250));

        jLabel41.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel41.setText("Đổi Mật Khẩu");

        jLabel42.setText("Tài khoản");

        jLabel43.setText("Mật khẩu cũ");

        jLabel44.setText("Mật Khẩu Mới");

        jLabel45.setText("Xác Nhận Mật Khẩu");

        jLabel46.setText("Mã Captcha");

        txtCaptchaPanelDoiMatKhau.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N

        txtLoadCaptchaPanelDoiMatKhau.setEditable(false);
        txtLoadCaptchaPanelDoiMatKhau.setBackground(new java.awt.Color(0, 153, 0));
        txtLoadCaptchaPanelDoiMatKhau.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N

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

        btnThoatPanelDoiMatKhau.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnThoatPanelDoiMatKhau.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/dangxuat.png"))); // NOI18N
        btnThoatPanelDoiMatKhau.setText("Thoát");
        btnThoatPanelDoiMatKhau.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnThoatPanelDoiMatKhauActionPerformed(evt);
            }
        });

        jLabel59.setFont(new java.awt.Font("Segoe UI", 2, 12)); // NOI18N
        jLabel59.setText("Lưu ý: Mật khẩu ít nhất 8 ký tự");

        jLabel60.setFont(new java.awt.Font("Segoe UI", 2, 12)); // NOI18N
        jLabel60.setText("Mật khẩu có ký tự viết hoa, các chữ cái từ a-z, số 0-9");

        jLabel61.setFont(new java.awt.Font("Segoe UI", 2, 12)); // NOI18N
        jLabel61.setText("Và một số ký tự đặc biệt như \"@, !, #, *\"");

        javax.swing.GroupLayout panelDoiMatKhauLayout = new javax.swing.GroupLayout(panelDoiMatKhau);
        panelDoiMatKhau.setLayout(panelDoiMatKhauLayout);
        panelDoiMatKhauLayout.setHorizontalGroup(
            panelDoiMatKhauLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelDoiMatKhauLayout.createSequentialGroup()
                .addGap(81, 81, 81)
                .addGroup(panelDoiMatKhauLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelDoiMatKhauLayout.createSequentialGroup()
                        .addGroup(panelDoiMatKhauLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel44)
                            .addComponent(jLabel41))
                        .addGap(602, 602, 602))
                    .addGroup(panelDoiMatKhauLayout.createSequentialGroup()
                        .addGroup(panelDoiMatKhauLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel42)
                            .addComponent(jLabel43)
                            .addComponent(jLabel45)
                            .addComponent(jLabel46))
                        .addGap(55, 55, 55)
                        .addGroup(panelDoiMatKhauLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtTaiKhoanPanelDoiMatKhau)
                            .addComponent(txtMatKhauMoiPanelDoiMatKhau)
                            .addComponent(txtMatKhauCuPanelDoiMatKhau)
                            .addComponent(txtXNMatKhauPanelDoiMatKhau, javax.swing.GroupLayout.PREFERRED_SIZE, 270, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(panelDoiMatKhauLayout.createSequentialGroup()
                                .addComponent(txtCaptchaPanelDoiMatKhau, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(txtLoadCaptchaPanelDoiMatKhau, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(btnLoadCaptchaPanelDoiMatKhau)))
                        .addGap(177, 177, 177)
                        .addGroup(panelDoiMatKhauLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel60)
                            .addComponent(jLabel59)
                            .addComponent(jLabel61))
                        .addGap(0, 242, Short.MAX_VALUE))))
            .addGroup(panelDoiMatKhauLayout.createSequentialGroup()
                .addGap(114, 114, 114)
                .addComponent(btnLuuPanelDoiMatKhau)
                .addGap(130, 130, 130)
                .addComponent(btnThoatPanelDoiMatKhau)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        panelDoiMatKhauLayout.setVerticalGroup(
            panelDoiMatKhauLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelDoiMatKhauLayout.createSequentialGroup()
                .addGap(42, 42, 42)
                .addComponent(jLabel41)
                .addGap(52, 52, 52)
                .addGroup(panelDoiMatKhauLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel42)
                    .addComponent(txtTaiKhoanPanelDoiMatKhau, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(33, 33, 33)
                .addGroup(panelDoiMatKhauLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel43)
                    .addComponent(txtMatKhauCuPanelDoiMatKhau, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel59))
                .addGap(18, 18, 18)
                .addComponent(jLabel60)
                .addGap(3, 3, 3)
                .addGroup(panelDoiMatKhauLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel44)
                    .addComponent(txtMatKhauMoiPanelDoiMatKhau, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(1, 1, 1)
                .addComponent(jLabel61)
                .addGap(18, 18, 18)
                .addGroup(panelDoiMatKhauLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel45)
                    .addComponent(txtXNMatKhauPanelDoiMatKhau, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(39, 39, 39)
                .addGroup(panelDoiMatKhauLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnLoadCaptchaPanelDoiMatKhau, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(panelDoiMatKhauLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel46)
                        .addComponent(txtCaptchaPanelDoiMatKhau, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(txtLoadCaptchaPanelDoiMatKhau, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(55, 55, 55)
                .addGroup(panelDoiMatKhauLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnLuuPanelDoiMatKhau)
                    .addComponent(btnThoatPanelDoiMatKhau))
                .addContainerGap(267, Short.MAX_VALUE))
        );

        panelMain.add(panelDoiMatKhau, "card10");

        jLabel75.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel75.setText("Xin Chào");

        lblMaNhanVienPanelMain.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lblMaNhanVienPanelMain.setText(" ");

        lblHoTenNVPanelMain.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lblHoTenNVPanelMain.setText(" ");

        jLabel77.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel77.setText("-");

        javax.swing.GroupLayout panelTrangChuLayout = new javax.swing.GroupLayout(panelTrangChu);
        panelTrangChu.setLayout(panelTrangChuLayout);
        panelTrangChuLayout.setHorizontalGroup(
            panelTrangChuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelTrangChuLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelTrangChuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelTrangChuLayout.createSequentialGroup()
                        .addGap(418, 418, 418)
                        .addComponent(jLabel75)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(lblMaNhanVienPanelMain, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel77)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(lblHoTenNVPanelMain, javax.swing.GroupLayout.PREFERRED_SIZE, 402, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnExit)
                        .addGap(20, 20, 20))
                    .addGroup(panelTrangChuLayout.createSequentialGroup()
                        .addGroup(panelTrangChuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(btnThongKe, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btnSanPham, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btnHoaDon, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btnLichSu, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btnKhuyenMai, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btnNhanVien, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btnKhachHang, javax.swing.GroupLayout.DEFAULT_SIZE, 151, Short.MAX_VALUE)
                            .addComponent(btnDangXuat, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btnDoiMatKhau, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(panelMain, javax.swing.GroupLayout.DEFAULT_SIZE, 1201, Short.MAX_VALUE))))
        );
        panelTrangChuLayout.setVerticalGroup(
            panelTrangChuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelTrangChuLayout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addGroup(panelTrangChuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnExit)
                    .addComponent(jLabel75)
                    .addComponent(lblMaNhanVienPanelMain)
                    .addComponent(lblHoTenNVPanelMain)
                    .addComponent(jLabel77))
                .addGap(26, 26, 26)
                .addGroup(panelTrangChuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelTrangChuLayout.createSequentialGroup()
                        .addGap(34, 34, 34)
                        .addComponent(btnThongKe)
                        .addGap(30, 30, 30)
                        .addComponent(btnSanPham)
                        .addGap(30, 30, 30)
                        .addComponent(btnHoaDon)
                        .addGap(32, 32, 32)
                        .addComponent(btnLichSu)
                        .addGap(32, 32, 32)
                        .addComponent(btnKhuyenMai)
                        .addGap(34, 34, 34)
                        .addComponent(btnNhanVien)
                        .addGap(38, 38, 38)
                        .addComponent(btnKhachHang)
                        .addGap(44, 44, 44)
                        .addComponent(btnDoiMatKhau)
                        .addGap(43, 43, 43)
                        .addComponent(btnDangXuat)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelTrangChuLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 6, Short.MAX_VALUE)
                        .addComponent(panelMain, javax.swing.GroupLayout.PREFERRED_SIZE, 737, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(panelTrangChu, javax.swing.GroupLayout.PREFERRED_SIZE, 1366, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panelTrangChu, javax.swing.GroupLayout.DEFAULT_SIZE, 827, Short.MAX_VALUE)
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void btnExitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExitActionPerformed
        int confirm = JOptionPane.showConfirmDialog(this, "Bạn muốn thoát không ?", "Thông báo", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            dispose();
            webcam.close();
        }
    }//GEN-LAST:event_btnExitActionPerformed

    // Panel Thống Kê
    private void btnThongKeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnThongKeActionPerformed
        btnThongKe.setBackground(colorXanh);
        btnSanPham.setBackground(colorTrang);
        btnHoaDon.setBackground(colorTrang);
        btnLichSu.setBackground(colorTrang);
        btnKhuyenMai.setBackground(colorTrang);
        btnNhanVien.setBackground(colorTrang);
        btnKhachHang.setBackground(colorTrang);
        btnDoiMatKhau.setBackground(colorTrang);
        setPanel(panelThongKe);
        cbbNamDoanhThuPanelThongKe.removeAllItems();
        addCbbNamDoanhThu(billService.getYear());
        lblSumDonHang.setText(String.valueOf(sumBill(billService.getList())));
        lblSumDate.setText(sum(billDetailService.sumDate(getNgayHienTai())));
        lblSumMonth.setText(sum(billDetailService.sumMonth(getMonth())));
        lblSumYear.setText(sum(billDetailService.sumYear(getYear())));
        donHang();
    }//GEN-LAST:event_btnThongKeActionPerformed


    private void loadDataThongKe(List<BillDetailWithProductDetailCustomModel> list) {
        defaultTableModel = (DefaultTableModel) tblSanPhamPanelThongKe.getModel();
        defaultTableModel.setRowCount(0);
        int count = 1;
        for (BillDetailWithProductDetailCustomModel billDetailWithProductDetailCustomModel : list) {
            defaultTableModel.addRow(new Object[]{
                    count++,
                    billDetailWithProductDetailCustomModel.getMaSP(),
                    productDetailService.getByNameProduct(billDetailWithProductDetailCustomModel.getMaSP()),
                    productDetailService.getByNameType(billDetailWithProductDetailCustomModel.getMaSP()),
                    productDetailService.getByNameSize(billDetailWithProductDetailCustomModel.getMaSP()),
                    productDetailService.getByNameColor(billDetailWithProductDetailCustomModel.getMaSP()),
                    productDetailService.getByNameSubtance(billDetailWithProductDetailCustomModel.getMaSP()),
                    billDetailWithProductDetailCustomModel.getSoLuong()
            });
        }
    }

    private void btnTimKiemPanelThongKeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTimKiemPanelThongKeActionPerformed
        String ngayBatDau = ((JTextField) txtNgayBatDauPanelThongKe.getDateEditor().getUiComponent()).getText();
        String ngayKetThuc = ((JTextField) txtNgayKetThucPanelThongKe.getDateEditor().getUiComponent()).getText();
        loadDataThongKe(billDetailService.getListThongKe(ngayBatDau, ngayKetThuc));
    }//GEN-LAST:event_btnTimKiemPanelThongKeActionPerformed

    private void addCbbNamDoanhThu(List<Integer> list) {
        defaultComboBoxModel = (DefaultComboBoxModel) cbbNamDoanhThuPanelThongKe.getModel();
        for (Integer integer : list) {
            defaultComboBoxModel.addElement(integer);
        }
    }

    private void loadDataDoanhThu(List<Integer> list) {
        defaultTableModel = (DefaultTableModel) tblDoanhThuPanelThongKe.getModel();
        defaultTableModel.setRowCount(0);
        for (Integer integer : list) {
            defaultTableModel.addRow(new Object[]{
                    integer,
                    billDetailService.getSoLuongDoanhThu(integer),
                    billDetailService.getGiaBan(integer),
                    billDetailService.getGiaGiam(integer),
                    billDetailService.getGiaBan(integer) - billDetailService.getGiaGiam(integer)
            });
        }
    }

    private void cbbNamDoanhThuPanelThongKeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbbNamDoanhThuPanelThongKeActionPerformed
        int year = (int) cbbNamDoanhThuPanelThongKe.getSelectedItem();
        loadDataDoanhThu(billDetailService.getListDoanhThu(year));
    }//GEN-LAST:event_cbbNamDoanhThuPanelThongKeActionPerformed

    private void btnXuatFileExcelPanelThongKeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnXuatFileExcelPanelThongKeActionPerformed
        String ngayBatDau = ((JTextField) txtNgayBatDauPanelThongKe.getDateEditor().getUiComponent()).getText();
        String ngayKetThuc = ((JTextField) txtNgayKetThucPanelThongKe.getDateEditor().getUiComponent()).getText();
        exportExcel.excel(billDetailService.getListThongKe(ngayBatDau, ngayKetThuc), "Thống kê ngày " + ngayBatDau + " " + ngayKetThuc);
    }//GEN-LAST:event_btnXuatFileExcelPanelThongKeActionPerformed

    // Panel Sản Phẩm
    private void loadDataLoaiSanPham(List<ProductType> list) {
        defaultTableModel = (DefaultTableModel) tblThuocTinhPanelSanPham.getModel();
        defaultTableModel.setRowCount(0);
        int count = 1;
        for (ProductType productType : list) {
            defaultTableModel.addRow(new Object[]{count++, productType.getCode(), productType.getName()});
        }
    }

    private void loadDataMauSac(List<com.Models.Color> list) {
        defaultTableModel = (DefaultTableModel) tblThuocTinhPanelSanPham.getModel();
        defaultTableModel.setRowCount(0);
        int count = 1;
        for (com.Models.Color color : list) {
            defaultTableModel.addRow(new Object[]{count++, color.getCode(), color.getName()});
        }
    }

    private void loadDataKichCo(List<Size> list) {
        defaultTableModel = (DefaultTableModel) tblThuocTinhPanelSanPham.getModel();
        defaultTableModel.setRowCount(0);
        int count = 1;
        for (Size size : list) {
            defaultTableModel.addRow(new Object[]{count++, size.getCode(), size.getName()});
        }
    }

    private void loadDataChatLieu(List<Substance> list) {
        defaultTableModel = (DefaultTableModel) tblThuocTinhPanelSanPham.getModel();
        defaultTableModel.setRowCount(0);
        int count = 1;
        for (Substance substance : list) {
            defaultTableModel.addRow(new Object[]{count++, substance.getCode(), substance.getName()});
        }
    }

    private String codeProductTypeTangDan() {
        String code = "";
        List<ProductType> list = productTypeService.getList();
        if (list.isEmpty()) {
            code = "LP0001";
        } else {
            int max = 0;
            for (ProductType productType : list) {
                int so = Integer.parseInt(productType.getCode().substring(2));
                if (so > max) {
                    max = so;
                }
            }
            max++;
            if (max < 10) {
                code = "LP000" + max;
            } else if (max < 100) {
                code = "LP00" + max;
            } else if (max < 1000) {
                code = "LP0" + max;
            } else {
                code = "LP" + max;
            }
        }
        return code;
    }

    private String codeColorTangDan() {
        String code = "";
        List<com.Models.Color> list = colorService.getList();
        if (list.isEmpty()) {
            code = "MS0001";
        } else {
            int max = 0;
            for (com.Models.Color color : list) {
                int so = Integer.parseInt(color.getCode().substring(2));
                if (so > max) {
                    max = so;
                }
            }
            max++;
            if (max < 10) {
                code = "MS000" + max;
            } else if (max < 100) {
                code = "MS00" + max;
            } else if (max < 1000) {
                code = "MS0" + max;
            } else {
                code = "MS" + max;
            }
        }
        return code;
    }

    private String codeSizeTangDan() {
        String code = "";
        List<Size> list = sizeService.getList();
        if (list.isEmpty()) {
            code = "KC0001";
        } else {
            int max = 0;
            for (Size size : list) {
                int so = Integer.parseInt(size.getCode().substring(2));
                if (so > max) {
                    max = so;
                }
            }
            max++;
            if (max < 10) {
                code = "KC000" + max;
            } else if (max < 100) {
                code = "KC00" + max;
            } else if (max < 1000) {
                code = "KC0" + max;
            } else {
                code = "KC" + max;
            }
        }
        return code;
    }

    private String codeSubstanceTangDan() {
        String code = "";
        List<Substance> list = substanceService.getList();
        if (list.isEmpty()) {
            code = "CL0001";
        } else {
            int max = 0;
            for (Substance substance : list) {
                int so = Integer.parseInt(substance.getCode().substring(2));
                if (so > max) {
                    max = so;
                }
            }
            max++;
            if (max < 10) {
                code = "CL000" + max;
            } else if (max < 100) {
                code = "CL00" + max;
            } else if (max < 1000) {
                code = "CL0" + max;
            } else {
                code = "CL" + max;
            }
        }
        return code;
    }

    private ProductType getDataLoaiSanPham() {
        String ma = codeProductTypeTangDan();
        String ten = txtTenThuocTinhPanelSanPham.getText();

        if (ten.trim().equals("")) {
            JOptionPane.showMessageDialog(this, "Không được để trống");
        }
        return new ProductType(ma, ten);
    }

    private com.Models.Color getDataMauSac() {
        String ma = codeColorTangDan();
        String ten = txtTenThuocTinhPanelSanPham.getText();
        if (ten.trim().equals("")) {
            JOptionPane.showMessageDialog(this, "Không được để trống");
        }
        return new com.Models.Color(ma, ten);
    }

    private Size getDataKichCo() {
        String ma = codeSizeTangDan();
        String ten = txtTenThuocTinhPanelSanPham.getText();
        if (ten.trim().equals("")) {
            JOptionPane.showMessageDialog(this, "Không được để trống");
        }
        return new Size(ma, ten);
    }

    private Substance getDataChatLieu() {
        String ma = codeSubstanceTangDan();
        String ten = txtTenThuocTinhPanelSanPham.getText();
        if (ten.trim().equals("")) {
            JOptionPane.showMessageDialog(this, "Không được để trống");
        }
        return new Substance(ma, ten);
    }

    private void clearPanelThuocTinh() {
        txtMaThuocTinhPanelSanPham.setText("");
        txtTenThuocTinhPanelSanPham.setText("");
    }

    private void btnSanPhamActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSanPhamActionPerformed
        btnThongKe.setBackground(colorTrang);
        btnSanPham.setBackground(colorXanh);
        btnHoaDon.setBackground(colorTrang);
        btnLichSu.setBackground(colorTrang);
        btnKhuyenMai.setBackground(colorTrang);
        btnNhanVien.setBackground(colorTrang);
        btnKhachHang.setBackground(colorTrang);
        btnDoiMatKhau.setBackground(colorTrang);
        setPanel(panelSanPham);
        rdoLoaiSanPhamPanelSanPham.setSelected(true);

        cbbLoaiSPPanelSanPham.removeAllItems();
        addCbbLoaiSanPham(productTypeService.getList());

        cbbMauSacPanelSanPham.removeAllItems();
        addCbbMauSac(colorService.getList());

        cbbKichCoPanelSanPham.removeAllItems();
        addCbbKichCo(sizeService.getList());

        cbbChatLieuPanelSanPham.removeAllItems();
        addCbbChatLieu(substanceService.getList());

        loadDataLoaiSanPham(productTypeService.getList());

        loadDataSanPhamChiTiet(productDetailService.getListProductDetal());
    }//GEN-LAST:event_btnSanPhamActionPerformed

    private void rdoLoaiSanPhamPanelSanPhamActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoLoaiSanPhamPanelSanPhamActionPerformed
        loadDataLoaiSanPham(productTypeService.getList());
        clearPanelThuocTinh();
    }//GEN-LAST:event_rdoLoaiSanPhamPanelSanPhamActionPerformed

    private void rdoMauSacPanelSanPhamActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoMauSacPanelSanPhamActionPerformed
        loadDataMauSac(colorService.getList());
        clearPanelThuocTinh();
    }//GEN-LAST:event_rdoMauSacPanelSanPhamActionPerformed

    private void rdoKichCoPanelSanPhamActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoKichCoPanelSanPhamActionPerformed
        loadDataKichCo(sizeService.getList());
        clearPanelThuocTinh();
    }//GEN-LAST:event_rdoKichCoPanelSanPhamActionPerformed

    private void rdoChatLieuPanelSanPhamActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoChatLieuPanelSanPhamActionPerformed
        loadDataChatLieu(substanceService.getList());
        clearPanelThuocTinh();
    }//GEN-LAST:event_rdoChatLieuPanelSanPhamActionPerformed

    private void btnThemPanelSanPhamActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnThemPanelSanPhamActionPerformed
        if (rdoLoaiSanPhamPanelSanPham.isSelected()) {
            ProductType productType = getDataLoaiSanPham();
            if (productType == null) {
                return;
            }
            if (productTypeService.insert(productType)) {
                loadDataLoaiSanPham(productTypeService.getList());
                cbbLoaiSPPanelSanPham.removeAllItems();
                addCbbLoaiSanPham(productTypeService.getList());
                clearPanelThuocTinh();
                JOptionPane.showMessageDialog(this, "Thêm Thành Công");
            } else {
                JOptionPane.showMessageDialog(this, "Thêm Thất Bại");
            }
        } else if (rdoMauSacPanelSanPham.isSelected()) {
            com.Models.Color color = getDataMauSac();
            if (color == null) {
                return;
            }
            if (colorService.insert(color)) {
                loadDataMauSac(colorService.getList());
                cbbMauSacPanelSanPham.removeAllItems();
                addCbbMauSac(colorService.getList());
                clearPanelThuocTinh();
                JOptionPane.showMessageDialog(this, "Thêm Thành Công");
            } else {
                JOptionPane.showMessageDialog(this, "Thêm Thất Bại");
            }
        } else if (rdoKichCoPanelSanPham.isSelected()) {
            Size size = getDataKichCo();
            if (size == null) {
                return;
            }
            if (sizeService.insert(size)) {
                loadDataKichCo(sizeService.getList());
                cbbKichCoPanelSanPham.removeAllItems();
                addCbbKichCo(sizeService.getList());
                clearPanelThuocTinh();
                JOptionPane.showMessageDialog(this, "Thêm Thành Công");
            } else {
                JOptionPane.showMessageDialog(this, "Thêm Thất Bại");
            }
        } else if (rdoChatLieuPanelSanPham.isSelected()) {
            Substance substance = getDataChatLieu();
            if (substance == null) {
                return;
            }
            if (substanceService.insert(substance)) {
                loadDataChatLieu(substanceService.getList());
                cbbChatLieuPanelSanPham.removeAllItems();
                addCbbChatLieu(substanceService.getList());
                clearPanelThuocTinh();
                JOptionPane.showMessageDialog(this, "Thêm Thành Công");
            } else {
                JOptionPane.showMessageDialog(this, "Thêm Thất Bại");
            }
        }
    }//GEN-LAST:event_btnThemPanelSanPhamActionPerformed

    private void btnSuaPanelSanPhamActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSuaPanelSanPhamActionPerformed
        String code = txtMaThuocTinhPanelSanPham.getText();

        if (code.trim().length() == 0) {
            JOptionPane.showMessageDialog(this, "Chưa chọn thuộc tính nào trên table");
            return;
        }

        if (rdoLoaiSanPhamPanelSanPham.isSelected()) {
            ProductType productType = getDataLoaiSanPham();
            if (productType == null) {
                return;
            }
            if (productTypeService.update(productType, code)) {
                loadDataLoaiSanPham(productTypeService.getList());
                cbbLoaiSPPanelSanPham.removeAllItems();
                addCbbLoaiSanPham(productTypeService.getList());
                clearPanelThuocTinh();
                JOptionPane.showMessageDialog(this, "Cập Nhật Thành Công");
            } else {
                JOptionPane.showMessageDialog(this, "Cập Nhật Thất Bại");
            }
        } else if (rdoMauSacPanelSanPham.isSelected()) {
            com.Models.Color color = getDataMauSac();
            if (color == null) {
                return;
            }
            if (colorService.update(color, code)) {
                loadDataMauSac(colorService.getList());
                cbbMauSacPanelSanPham.removeAllItems();
                addCbbMauSac(colorService.getList());
                clearPanelThuocTinh();
                JOptionPane.showMessageDialog(this, "Cập Nhật Thành Công");
            } else {
                JOptionPane.showMessageDialog(this, "Cập Nhật Thất Bại");
            }
        } else if (rdoKichCoPanelSanPham.isSelected()) {
            Size size = getDataKichCo();
            if (size == null) {
                return;
            }
            if (sizeService.update(size, code)) {
                loadDataKichCo(sizeService.getList());
                cbbKichCoPanelSanPham.removeAllItems();
                addCbbKichCo(sizeService.getList());
                clearPanelThuocTinh();
                JOptionPane.showMessageDialog(this, "Cập Nhật Thành Công");
            } else {
                JOptionPane.showMessageDialog(this, "Cập Nhật Thất Bại");
            }
        } else if (rdoChatLieuPanelSanPham.isSelected()) {
            Substance substance = getDataChatLieu();
            if (substance == null) {
                return;
            }
            if (substanceService.update(substance, code)) {
                loadDataChatLieu(substanceService.getList());
                cbbChatLieuPanelSanPham.removeAllItems();
                addCbbChatLieu(substanceService.getList());
                clearPanelThuocTinh();
                JOptionPane.showMessageDialog(this, "Cập Nhật Thành Công");
            } else {
                JOptionPane.showMessageDialog(this, "Cập Nhật Thất Bại");
            }
        }
    }//GEN-LAST:event_btnSuaPanelSanPhamActionPerformed

    private void btnXoaPanelSanPhamActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnXoaPanelSanPhamActionPerformed
        String code = txtMaThuocTinhPanelSanPham.getText();

        if (code.trim().length() == 0) {
            JOptionPane.showMessageDialog(this, "Chưa chọn thuộc tính nào trên table");
            return;
        }

        if (rdoLoaiSanPhamPanelSanPham.isSelected()) {
            int confirm = JOptionPane.showConfirmDialog(this, "Bạn muốn xóa thuộc tính này không ?", "Thông báo", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                if (productTypeService.delete(code)) {
                    loadDataLoaiSanPham(productTypeService.getList());
                    cbbLoaiSPPanelSanPham.removeAllItems();
                    addCbbLoaiSanPham(productTypeService.getList());
                    clearPanelThuocTinh();
                    JOptionPane.showMessageDialog(this, "Xóa Thành Công");
                } else {
                    JOptionPane.showMessageDialog(this, "Xóa Thất Bại");
                }
            } else {
                return;
            }
        } else if (rdoMauSacPanelSanPham.isSelected()) {
            int confirm = JOptionPane.showConfirmDialog(this, "Bạn muốn xóa thuộc tính này không ?", "Thông báo", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                if (colorService.delete(code)) {
                    loadDataMauSac(colorService.getList());
                    cbbMauSacPanelSanPham.removeAllItems();
                    addCbbMauSac(colorService.getList());
                    clearPanelThuocTinh();
                    JOptionPane.showMessageDialog(this, "Xóa Thành Công");
                } else {
                    JOptionPane.showMessageDialog(this, "Xóa Thất Bại");
                }
            } else {
                return;
            }
        } else if (rdoKichCoPanelSanPham.isSelected()) {
            int confirm = JOptionPane.showConfirmDialog(this, "Bạn muốn xóa thuộc tính này không ?", "Thông báo", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                if (sizeService.delete(code)) {
                    loadDataKichCo(sizeService.getList());
                    cbbKichCoPanelSanPham.removeAllItems();
                    addCbbKichCo(sizeService.getList());
                    clearPanelThuocTinh();
                    JOptionPane.showMessageDialog(this, "Xóa Thành Công");
                } else {
                    JOptionPane.showMessageDialog(this, "Xóa Thất Bại");
                }
            } else {
                return;
            }
        } else if (rdoChatLieuPanelSanPham.isSelected()) {
            int confirm = JOptionPane.showConfirmDialog(this, "Bạn muốn xóa thuộc tính này không ?", "Thông báo", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                if (substanceService.delete(code)) {
                    loadDataChatLieu(substanceService.getList());
                    cbbChatLieuPanelSanPham.removeAllItems();
                    addCbbChatLieu(substanceService.getList());
                    clearPanelThuocTinh();
                    JOptionPane.showMessageDialog(this, "Xóa Thành Công");
                } else {
                    JOptionPane.showMessageDialog(this, "Xóa Thất Bại");
                }
            } else {
                return;
            }
        }
    }//GEN-LAST:event_btnXoaPanelSanPhamActionPerformed

    private void tblThuocTinhPanelSanPhamMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblThuocTinhPanelSanPhamMouseClicked
        int row = tblThuocTinhPanelSanPham.getSelectedRow();
        txtMaThuocTinhPanelSanPham.setText(tblThuocTinhPanelSanPham.getValueAt(row, 1).toString());
        txtTenThuocTinhPanelSanPham.setText(tblThuocTinhPanelSanPham.getValueAt(row, 2).toString());
    }//GEN-LAST:event_tblThuocTinhPanelSanPhamMouseClicked

    // Panel Sản Phẩm Chi Tiết
    private void addCbbLoaiSanPham(List<ProductType> list) {
        defaultComboBoxModel = (DefaultComboBoxModel) cbbLoaiSPPanelSanPham.getModel();
        for (ProductType productType : list) {
            defaultComboBoxModel.addElement(productType);
        }
    }

    private void addCbbMauSac(List<com.Models.Color> list) {
        defaultComboBoxModel = (DefaultComboBoxModel) cbbMauSacPanelSanPham.getModel();
        for (com.Models.Color color : list) {
            defaultComboBoxModel.addElement(color);
        }
    }

    private void addCbbKichCo(List<Size> list) {
        defaultComboBoxModel = (DefaultComboBoxModel) cbbKichCoPanelSanPham.getModel();
        for (Size size : list) {
            defaultComboBoxModel.addElement(size);
        }
    }

    private void addCbbChatLieu(List<Substance> list) {
        defaultComboBoxModel = (DefaultComboBoxModel) cbbChatLieuPanelSanPham.getModel();
        for (Substance substance : list) {
            defaultComboBoxModel.addElement(substance);
        }
    }

    private void loadDataSanPhamChiTiet(List<ProductDetailCustomModel> list) {
        defaultTableModel = (DefaultTableModel) tblChiTietSanPhamPanelSanPham.getModel();
        defaultTableModel.setRowCount(0);
        int count = 1;
        for (ProductDetailCustomModel productDetailCustomModel : list) {
            defaultTableModel.addRow(new Object[]{count++, productDetailCustomModel.getMaSP(), productDetailCustomModel.getTenSP(), productDetailCustomModel.getLoaiSP(), productDetailCustomModel.getKichCo(), productDetailCustomModel.getMauSac(), productDetailCustomModel.getChatLieu(), productDetailCustomModel.getDonGia(), productDetailCustomModel.getSoLuong(), productDetailCustomModel.getMoTa()});
        }
    }

    private void clearPanelSanPhamChiTiet() {
        txtMaSPPanelSanPham.setText("");
        txtTenSPPanelSanPham.setText("");
        txtGiaPanelSanPham.setText("");
        spnSoLuongPanelSanPham.setValue(0);
        cbbLoaiSPPanelSanPham.setSelectedIndex(0);
        cbbMauSacPanelSanPham.setSelectedIndex(0);
        cbbKichCoPanelSanPham.setSelectedIndex(0);
        cbbChatLieuPanelSanPham.setSelectedIndex(0);
        txtMoTaPanelSanPham.setText("");
        txtTimKiemSPPanelSanPham.setText("");
    }

    private String codeProductDetailTangDan() {
        String code = "";
        List<ProductDetails> list = productDetailService.getList();
        if (list.isEmpty()) {
            code = "SP0001";
        } else {
            int max = 0;
            for (ProductDetails productDetails : list) {
                int so = Integer.parseInt(productDetails.getCode().substring(2));
                if (so > max) {
                    max = so;
                }
            }
            max++;
            if (max < 10) {
                code = "SP000" + max;
            } else if (max < 100) {
                code = "SP00" + max;
            } else if (max < 1000) {
                code = "SP0" + max;
            } else {
                code = "SP" + max;
            }
        }
        return code;
    }

    private void setIndexCbbLoaiSPPanelSanPham(String ten) {
        int count = -1;
        List<ProductType> list = productTypeService.getList();
        for (ProductType productType : list) {
            count++;
            if (productType.getName().equals(ten)) {
                cbbLoaiSPPanelSanPham.setSelectedIndex(count);
            }
        }
    }

    private void setIndexCbbKichCoPanelSanPham(String ten) {
        int count = -1;
        List<Size> list = sizeService.getList();
        for (Size size : list) {
            count++;
            if (size.getName().equals(ten)) {
                cbbKichCoPanelSanPham.setSelectedIndex(count);
            }
        }
    }

    private void setIndexCbbMauSacPanelSanPham(String ten) {
        int count = -1;
        List<com.Models.Color> list = colorService.getList();
        for (com.Models.Color color : list) {
            count++;
            if (color.getName().equals(ten)) {
                cbbMauSacPanelSanPham.setSelectedIndex(count);
            }
        }
    }

    private void setIndexCbbChatLieuPanelSanPham(String ten) {
        int count = -1;
        List<Substance> list = substanceService.getList();
        for (Substance substance : list) {
            count++;
            if (substance.getName().equals(ten)) {
                cbbChatLieuPanelSanPham.setSelectedIndex(count);
            }
        }
    }

    private ProductDetails getDataSanPhamChiTiet() {
        String maSP = codeProductDetailTangDan();
        String tenSP = txtTenSPPanelSanPham.getText();
        String giaBan = txtGiaPanelSanPham.getText();
        int soluong = (int) spnSoLuongPanelSanPham.getValue();
        ProductType productType = (ProductType) cbbLoaiSPPanelSanPham.getSelectedItem();
        com.Models.Color color = (com.Models.Color) cbbMauSacPanelSanPham.getSelectedItem();
        Size size = (Size) cbbKichCoPanelSanPham.getSelectedItem();
        Substance substance = (Substance) cbbChatLieuPanelSanPham.getSelectedItem();
        String moTa = txtMoTaPanelSanPham.getText();
        if (tenSP.trim().equals("") || giaBan.trim().equals("") || moTa.trim().equals("")) {
            JOptionPane.showMessageDialog(this, "Không được để trống");
            return null;
        }
        try {
            if (Float.parseFloat(giaBan) < 0) {
                JOptionPane.showMessageDialog(this, "Giá bán phải lớn hơn 0");
                return null;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Giá bán phải là số");
            e.printStackTrace();
            return null;
        }
        if (String.valueOf(soluong).length() > 1000000000) {
            JOptionPane.showMessageDialog(this, "Bạn nhập số lượng quá lớn");
            return null;
        }
        return new ProductDetails(maSP, tenSP, substance, size, color, productType, moTa, soluong, Float.parseFloat(giaBan), 1);
    }

    private void btnXoaFormPanelSanPhamCTActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnXoaFormPanelSanPhamCTActionPerformed
        clearPanelSanPhamChiTiet();
        loadDataSanPhamChiTiet(productDetailService.getListProductDetal());
    }//GEN-LAST:event_btnXoaFormPanelSanPhamCTActionPerformed

    private void btnThemPanelSanPhamCTActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnThemPanelSanPhamCTActionPerformed
        ProductDetails productDetails = getDataSanPhamChiTiet();
        if (productDetails == null) {
            return;
        }
        logicUtil.taoMaQR(codeProductDetailTangDan());
        if (productDetailService.insert(productDetails)) {
            loadDataSanPhamChiTiet(productDetailService.getListProductDetal());
            clearPanelSanPhamChiTiet();
            JOptionPane.showMessageDialog(this, "Thêm thành công");
            insertKM(productDetailService.getList());
        } else {
            JOptionPane.showMessageDialog(this, "Thêm thất bại");
        }
    }//GEN-LAST:event_btnThemPanelSanPhamCTActionPerformed

    private void btnSuaPanelSanPhamCTActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSuaPanelSanPhamCTActionPerformed
        ProductDetails productDetails = getDataSanPhamChiTiet();
        if (productDetails == null) {
            return;
        }
        if (productDetailService.update(productDetails, txtMaSPPanelSanPham.getText())) {
            loadDataSanPhamChiTiet(productDetailService.getListProductDetal());
            clearPanelSanPhamChiTiet();
            JOptionPane.showMessageDialog(this, "Sửa thành công");
        } else {
            JOptionPane.showMessageDialog(this, "Sửa thất bại");
        }
    }//GEN-LAST:event_btnSuaPanelSanPhamCTActionPerformed

    private void btnAnPanelSanPhamCTActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAnPanelSanPhamCTActionPerformed
        int confirm = JOptionPane.showConfirmDialog(this, "Bạn có muốn ẩn sản phẩm này không?", "Thông báo", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            if (productDetailService.hideOrShow(txtMaSPPanelSanPham.getText(), 0)) {
                loadDataSanPhamChiTiet(productDetailService.getListProductDetal());
                clearPanelSanPhamChiTiet();
                JOptionPane.showMessageDialog(this, "Ẩn thành công");
            } else {
                JOptionPane.showMessageDialog(this, "Ẩn thất bại");
            }
        }
    }//GEN-LAST:event_btnAnPanelSanPhamCTActionPerformed

    private void btnXemAnPanelSanPhamCTActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnXemAnPanelSanPhamCTActionPerformed
        new XemSanPhamAn().setVisible(true);
    }//GEN-LAST:event_btnXemAnPanelSanPhamCTActionPerformed

    private void tblChiTietSanPhamPanelSanPhamMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblChiTietSanPhamPanelSanPhamMouseClicked
        int row = tblChiTietSanPhamPanelSanPham.getSelectedRow();
        if (row < 0) {
            return;
        }
        txtMaSPPanelSanPham.setText(tblChiTietSanPhamPanelSanPham.getValueAt(row, 1).toString());
        txtTenSPPanelSanPham.setText(tblChiTietSanPhamPanelSanPham.getValueAt(row, 2).toString());
        setIndexCbbLoaiSPPanelSanPham(tblChiTietSanPhamPanelSanPham.getValueAt(row, 3).toString());
        setIndexCbbKichCoPanelSanPham(tblChiTietSanPhamPanelSanPham.getValueAt(row, 4).toString());
        setIndexCbbMauSacPanelSanPham(tblChiTietSanPhamPanelSanPham.getValueAt(row, 5).toString());
        setIndexCbbChatLieuPanelSanPham(tblChiTietSanPhamPanelSanPham.getValueAt(row, 6).toString());
        txtGiaPanelSanPham.setText(tblChiTietSanPhamPanelSanPham.getValueAt(row, 7).toString());
        spnSoLuongPanelSanPham.setValue(tblChiTietSanPhamPanelSanPham.getValueAt(row, 8));
        txtMoTaPanelSanPham.setText(tblChiTietSanPhamPanelSanPham.getValueAt(row, 9).toString());
    }//GEN-LAST:event_tblChiTietSanPhamPanelSanPhamMouseClicked

    private void btnTimKiemPanelSanPhamActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTimKiemPanelSanPhamActionPerformed
        String timKiem = txtTimKiemSPPanelSanPham.getText();
        switch (cbbTimKiemSPPanelSanPham.getSelectedIndex()) {
            case 0:
                if (productDetailService.getListByCode(timKiem).isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Không tìm thấy sản phẩm");
                    return;
                } else {
                    loadDataSanPhamChiTiet(productDetailService.getListByCode(timKiem));
                }
                break;
            case 1:
                if (productDetailService.getListByName(timKiem).isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Không tìm thấy sản phẩm");
                    return;
                } else {
                    loadDataSanPhamChiTiet(productDetailService.getListByName(timKiem));
                }
                break;
            case 2:
                if (productDetailService.getListByProductType(timKiem).isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Không tìm thấy sản phẩm");
                    return;
                } else {
                    loadDataSanPhamChiTiet(productDetailService.getListByProductType(timKiem));
                }
                break;
            case 3:
                if (productDetailService.getListBySize(timKiem).isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Không tìm thấy sản phẩm");
                    return;
                } else {
                    loadDataSanPhamChiTiet(productDetailService.getListBySize(timKiem));
                }
                break;
            case 4:
                if (productDetailService.getListByColor(timKiem).isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Không tìm thấy sản phẩm");
                    return;
                } else {
                    loadDataSanPhamChiTiet(productDetailService.getListByColor(timKiem));
                }
                break;
            case 5:
                if (productDetailService.getListBySubstance(timKiem).isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Không tìm thấy sản phẩm");
                    return;
                } else {
                    loadDataSanPhamChiTiet(productDetailService.getListBySubstance(timKiem));
                }
                break;
            default:
                JOptionPane.showMessageDialog(this, "Không tìm thấy sản phẩm");
                break;
        }
    }//GEN-LAST:event_btnTimKiemPanelSanPhamActionPerformed

    // Panel Hóa Đơn
    private void loadDataSanPhamChiTietPanelHoaDon(List<ProductDetailCustomModel> list) {
        defaultTableModel = (DefaultTableModel) tblSanPhamPanelHoaDon.getModel();
        defaultTableModel.setRowCount(0);
        for (ProductDetailCustomModel productDetailCustomModel : list) {
            defaultTableModel.addRow(new Object[]{productDetailCustomModel.getMaSP(), productDetailCustomModel.getTenSP(), productDetailCustomModel.getLoaiSP(), productDetailCustomModel.getKichCo(), productDetailCustomModel.getMauSac(), productDetailCustomModel.getChatLieu(), productDetailCustomModel.getSoLuong(), productDetailCustomModel.getDonGia()});
        }
    }

    public void exportPDFBill(String path, List<Bill> listBill, String tongTien, String tienKhachDua, String tienThua) {
        try {
            BaseFont bf = BaseFont.createFont("C:\\Windows\\Fonts\\arial.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
            Font font = new Font(bf, 12, Font.NORMAL);
            Font font1 = new Font(bf, 12, Font.BOLD);
            Document document = new Document();
            PdfWriter.getInstance(document, new FileOutputStream(path));
            document.open();
            PdfWriter.getISOBytes("UTF-8");
            document.setPageSize(PageSize.NOTE);
            document.add(new Paragraph("BIll Bán Giày TTNam", font1));
            document.add(new Paragraph("----------------------------------------------------------------------------------------------------------------------------------"));
            for (Bill bill : listBill) {
                document.add(new Paragraph("Mã hoá đơn: " + bill.getCode(), font));
                System.out.println(new Paragraph("Mã hoá đơn: " + bill.getCode(), font));
                document.add(new Paragraph("Tên Khách hàng: " + bill.getNameCustomer(), font));
                document.add(new Paragraph("Số điện thoại: " + bill.getPhoneNumberCustomer(), font));
                document.add(new Paragraph("Địa chỉ: " + bill.getAddress(), font));
                document.add(new Paragraph("Ghi Chú: " + bill.getNote(), font));
                document.add(new Paragraph("Nhân Viên: " + bill.getStaff().getFirstName(), font));
                document.add(new Paragraph("Ngày Tạo: " + bill.getDateCreated(), font));
            }
            ;

            document.add(new Paragraph("----------------------------------------------------------------------------------------------------------------------------------"));
            document.add(new Paragraph(" "));
            PdfPTable table = new PdfPTable(6);
            PdfPCell cell = new PdfPCell(new Paragraph("Danh sách sản phẩm", font1));
            cell.setColspan(6);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
            table.addCell(cell);
            table.addCell(new Paragraph("STT", font1));
            table.addCell(new Paragraph("Tên sản phẩm", font));
            table.addCell(new Paragraph("Số lượng", font));
            table.addCell(new Paragraph("Đơn giá", font));
            table.addCell(new Paragraph("Khuyến mại", font));
            table.addCell(new Paragraph("Thành tiền", font));
            for (int i = 0; i < tblGioHangPanelHoaDon.getRowCount(); i++) {
                table.addCell(new Paragraph(tblGioHangPanelHoaDon.getValueAt(i, 0).toString(), font));
                table.addCell(new Paragraph(tblGioHangPanelHoaDon.getValueAt(i, 2).toString(), font));
                table.addCell(new Paragraph(tblGioHangPanelHoaDon.getValueAt(i, 3).toString(), font));
                table.addCell(new Paragraph(tblGioHangPanelHoaDon.getValueAt(i, 4).toString(), font));
                table.addCell(new Paragraph(tblGioHangPanelHoaDon.getValueAt(i, 5).toString(), font));
                table.addCell(new Paragraph(tblGioHangPanelHoaDon.getValueAt(i, 6).toString(), font));
            }
            document.add(table);
            document.add(new Paragraph("----------------------------------------------------------------------------------------------------------------------------------"));
            document.add(new Paragraph("Tổng tiền : " + tongTien, font));
            document.add(new Paragraph("Tiền khách đưa : " + tienKhachDua + ".VND", font));
            document.add(new Paragraph("Tiền thừa : " + tienThua, font));
            document.add(new Paragraph("----------------------------------------------------------------------------------------------------------------------------------"));
            document.add(new Paragraph("Cảm ơn quý khách đã mua hàng!", font1));
            document.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void clearFormBill() {
        lblTongTienPanelHoaDon.setText("");
        cbbHTTTPanelHoaDon.setSelectedIndex(0);
        txtTienKhachDuaPanelHoaDon.setText("");
        lblTienThuaPanelHoaDon.setText("");
        txtTenKhachHangPanelHoaDon.setText("");
        txtSDTPanelHoaDon.setText("");
        txtSDTPanelDatHang.setText("");
       lblTienThuaPanelDatHang.setText("");
        lblTongTienPanelDatHang.setText("");
        cbbHTTTPanelDatHang.setSelectedIndex(0);
        txtTenKhachHangPanelDatHang.setText("");
        lblTongTienPanelDatHang.setText("");
    }

    private String codeBillTangDan() {
        String code = "";
        List<Bill> list = billService.getList();
        if (list.isEmpty()) {
            code = "HD000001";
        } else {
            int max = 0;
            for (Bill bill : list) {
                int so = Integer.parseInt(bill.getCode().substring(2));
                if (so > max) {
                    max = so;
                }
            }
            max++;
            if (max < 10) {
                code = "HD00000" + max;
            } else if (max < 100) {
                code = "HD0000" + max;
            } else if (max < 1000) {
                code = "HD000" + max;
            } else if (max < 10000) {
                code = "HD00" + max;
            } else if (max < 100000) {
                code = "HD0" + max;
            } else {
                code = "HD" + max;
            }
        }
        return code;
    }

    private void loadDataHoaDonPanelHoaDon(List<BillCustomModel> list) {
        defaultTableModel = (DefaultTableModel) tblHoaDonPanelHoaDon.getModel();
        defaultTableModel.setRowCount(0);
        int count = 1;
        for (BillCustomModel billCustomModel : list) {
            defaultTableModel.addRow(new Object[]{count++, billCustomModel.getMaHD(), billCustomModel.getTenKH(), billCustomModel.getTenNV(), billCustomModel.getNgayTao(), billCustomModel.trangThaiHD(billCustomModel.getTrangThai())});
        }
    }

    private Bill getDataBill(Customer customer) {
        String tenKH = txtTenKhachHangPanelHoaDon.getText();
        String sdt = txtSDTPanelHoaDon.getText();
        String ghiChu = txtGhiChuHoaDon.getText();
        String diaChi = txtDiaChiKhachHang.getText();
        Staff staff = new Staff(staffService.getByID(lblMaNhanVienPanelMain.getText()));
        return new Bill(customer, staff, codeBillTangDan(), getNgayHienTai(), 0, diaChi, ghiChu, tenKH, sdt);
    }

    private void btnHoaDonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHoaDonActionPerformed
        btnThongKe.setBackground(colorTrang);
        btnSanPham.setBackground(colorTrang);
        btnHoaDon.setBackground(colorXanh);
        btnLichSu.setBackground(colorTrang);
        btnKhuyenMai.setBackground(colorTrang);
        btnNhanVien.setBackground(colorTrang);
        btnKhachHang.setBackground(colorTrang);
        btnDoiMatKhau.setBackground(colorTrang);
        setPanel(panelHoaDon);
        loadDataSanPhamChiTietPanelHoaDon(productDetailService.getListProductDetal());
        setResizable(false);
        this.initWebcam();
        loadDataHoaDonPanelHoaDon(billService.getListBill());
        clearFormBill();
    }//GEN-LAST:event_btnHoaDonActionPerformed

    // Panel Hóa Đơn

    private void btnTaoHoaDonPanelHoaDonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTaoHoaDonPanelHoaDonActionPerformed
        String sdt = txtSDTPanelHoaDon.getText();
        if (customerService.checkCustomer(sdt)) {
            Customer customer = new Customer(customerService.getByID(sdt));
            Bill bill = getDataBill(customer);
            if (bill == null) {
                return;
            }
            if (billService.insert(bill)) {
                JOptionPane.showMessageDialog(this, "Tạo hóa đơn thành công");
                loadDataHoaDonPanelHoaDon(billService.getListBill());
            } else {
                JOptionPane.showMessageDialog(this, "Tạo hóa đơn thất bại");
            }
            loadDataHoaDonPanelHoaDon(billService.getListBill());
        } else {
            String tenKH = txtTenKhachHangPanelHoaDon.getText();
            String ghiChu = txtGhiChuHoaDon.getText();

            Staff staff = new Staff(staffService.getByID(lblMaNhanVienPanelMain.getText()));

            Bill bill = new Bill(staff, codeBillTangDan(), getNgayHienTai(), 0, null, ghiChu, tenKH, sdt);

            if (billService.insert(bill)) {
                JOptionPane.showMessageDialog(this, "Tạo hóa đơn thành công");
                loadDataHoaDonPanelHoaDon(billService.getListBill());
            } else {
                JOptionPane.showMessageDialog(this, "Tạo hóa đơn thất bại");
            }
            loadDataHoaDonPanelHoaDon(billService.getListBill());
        }
    }//GEN-LAST:event_btnTaoHoaDonPanelHoaDonActionPerformed

    private void btnTimKiemKhachHangPanelHoaDonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTimKiemKhachHangPanelHoaDonActionPerformed
        String sdt = txtSDTPanelHoaDon.getText();
        if (!customerService.checkCustomer(sdt)) {
            JOptionPane.showMessageDialog(this, "Khách hàng không có trong hệ thống");
        }
        txtTenKhachHangPanelHoaDon.setText(customerService.getByName(sdt));
    }//GEN-LAST:event_btnTimKiemKhachHangPanelHoaDonActionPerformed

    private void cbbHTTTPanelHoaDonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbbHTTTPanelHoaDonActionPerformed
        if (cbbHTTTPanelHoaDon.getSelectedIndex() == 1) {
            String[] tongTien = lblTongTienPanelHoaDon.getText().split(".VND");
            txtTienKhachDuaPanelHoaDon.setText(tongTien[0]);
        }
    }//GEN-LAST:event_cbbHTTTPanelHoaDonActionPerformed

    private void btnThanhToanPanelHoaDonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnThanhToanPanelHoaDonActionPerformed
        String email = customerService.getByEmail(txtSDTPanelHoaDon.getText());
        int httt = cbbHTTTPanelHoaDon.getSelectedIndex();
        int row = tblHoaDonPanelHoaDon.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Chưa chọn hóa đơn");
            return;
        }
        String maHD = tblHoaDonPanelHoaDon.getValueAt(row, 1).toString();

        if (billDetailService.getListBill(maHD).isEmpty()) {
            JOptionPane.showMessageDialog(this, "Giỏ hàng không có sản phẩm");
        }
        String url = "D:\\PhanMemQuanLyBanQuanGiayTTNam\\Bill\\" + maHD + ".pdf";
        if (billDetailService.getListBill(maHD).isEmpty()) {
            JOptionPane.showMessageDialog(this, "Giỏ hàng chưa có sản phẩm!");
            return;
        }
        if (httt == 0) {
            if (txtTienKhachDuaPanelHoaDon.getText().trim().length() == 0) {
                JOptionPane.showMessageDialog(this, "Tiền khách đưa trống");
                return;
            } else {
                if (billService.updateThanhToan(maHD, 1, getNgayHienTai())) {
                    loadDataHoaDonPanelHoaDon(billService.getListBill());
                    JOptionPane.showMessageDialog(this, "Thanh toán thành công");
                    exportPDFBill(url, billService.getList(maHD), lblTongTienPanelHoaDon.getText(), txtTienKhachDuaPanelHoaDon.getText(), lblTienThuaPanelHoaDon.getText());
                    sendEmail.guiHoaDon(email, url);
                    clearFormBill();
                    lblTienThuaPanelDatHang.setText("");
                } 
                    
                
                else {
                    JOptionPane.showMessageDialog(this, "Thanh toán thành công");
                    clearFormBill();
                     
                }
            }
        } else if (httt == 1) {
            if (billService.updateThanhToan(maHD, 1, getNgayHienTai())) {
                loadDataHoaDonPanelHoaDon(billService.getListBill());
                JOptionPane.showMessageDialog(this, "Thanh toán thành công");
                exportPDFBill(url, billService.getList(maHD), lblTongTienPanelHoaDon.getText(), txtTienKhachDuaPanelHoaDon.getText(), lblTienThuaPanelHoaDon.getText());
                sendEmail.guiHoaDon(email, url);
                clearFormBill();
                 lblTienThuaPanelDatHang.setText("");
            } else {
                JOptionPane.showMessageDialog(this, "Thanh toán thành công");
                clearFormBill();
                 lblTienThuaPanelDatHang.setText("");
            }
        }
        else if( lblTienThuaPanelDatHang.getText().equals("<=0")){
            
             JOptionPane.showMessageDialog(this, "Thanh thất bại");
        }
        clearFormBill();
    }//GEN-LAST:event_btnThanhToanPanelHoaDonActionPerformed

    private void btnHuyPanelHoaDonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHuyPanelHoaDonActionPerformed
        String maHD = tblHoaDonPanelHoaDon.getValueAt(tblHoaDonPanelHoaDon.getSelectedRow(), 1).toString();
        if (!billService.checkStatus(maHD)) {
            JOptionPane.showMessageDialog(this, "Không thể huỷ hoá đơn!");
            return;
        }
        String input = JOptionPane.showInputDialog("Lý do muốn hủy hóa đơn ?");
        if (input.trim().length() == 0) return;
        if (billService.updateHuy(maHD, input, 2, getNgayHienTai())) {
            loadDataHoaDonPanelHoaDon(billService.getListBill());
            JOptionPane.showMessageDialog(this, "Hủy thành công");
        } else {
            JOptionPane.showMessageDialog(this, "Hủy thất bại");
        }
    }//GEN-LAST:event_btnHuyPanelHoaDonActionPerformed

    private void txtTienKhachDuaPanelHoaDonCaretUpdate(javax.swing.event.CaretEvent evt) {//GEN-FIRST:event_txtTienKhachDuaPanelHoaDonCaretUpdate
        String[] tongTien = lblTongTienPanelHoaDon.getText().split(".VND");
        for (String s : tongTien) {
            double tienKhachDua = Double.parseDouble(txtTienKhachDuaPanelHoaDon.getText());
            double tienThua = tienKhachDua - Double.parseDouble(s);
            int tien = (int) Math.round(tienThua);
            lblTienThuaPanelHoaDon.setText(tien + ".VND");
        }
    }//GEN-LAST:event_txtTienKhachDuaPanelHoaDonCaretUpdate

    private void tblHoaDonPanelHoaDonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblHoaDonPanelHoaDonMouseClicked
        String maHD = tblHoaDonPanelHoaDon.getValueAt(tblHoaDonPanelHoaDon.getSelectedRow(), 1).toString();
        loadDataGioHang(billDetailService.getListBill(maHD));
        lblTongTienPanelHoaDon.setText(0 + " VND");
        int sum = (int) Math.round(billDetailService.sumDonGia(maHD));
        lblTongTienPanelHoaDon.setText(sum + " VND");
        lblTongTienPanelDatHang.setText(sum + " VND");
    }//GEN-LAST:event_tblHoaDonPanelHoaDonMouseClicked

    private BillDetails getDataBillDetails(int soLuong, String codeSP, String codeHD, float donGia) {
        ProductDetails productDetails = new ProductDetails(productDetailService.getByID(codeSP));
        Bill bill = new Bill(billService.getByID(codeHD));
        Promotion promotion = new Promotion(promotionDetailService.getByID(productDetailService.getByID(codeSP)));

        return new BillDetails(bill, productDetails, soLuong, donGia, 1, promotion);
    }

    private void loadDataGioHang(List<BillDetailCustomModel> list) {
        defaultTableModel = (DefaultTableModel) tblGioHangPanelHoaDon.getModel();
        defaultTableModel.setRowCount(0);
        int count = 1;
        for (BillDetailCustomModel billDetailCustomModel : list) {
            defaultTableModel.addRow(new Object[]{
                    count++,
                    billDetailCustomModel.getMaSP(),
                    billDetailCustomModel.getTenSP(),
                    billDetailCustomModel.getSoLuong(),
                    billDetailCustomModel.getDonGia(),
                    (billDetailCustomModel.getDonGia() / 100) * billDetailCustomModel.getPhanTramKM(),
                    billDetailCustomModel.getSoLuong() * (billDetailCustomModel.getDonGia() - (billDetailCustomModel.getDonGia() / 100) * billDetailCustomModel.getPhanTramKM()),
                    false});
        }
    }

    private void tblSanPhamPanelHoaDonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblSanPhamPanelHoaDonMouseClicked
        String input = JOptionPane.showInputDialog("Hãy nhập số lượng sản phẩm", 1);

        String maSP = tblSanPhamPanelHoaDon.getValueAt(tblSanPhamPanelHoaDon.getSelectedRow(), 0).toString();
        String maHD = tblHoaDonPanelHoaDon.getValueAt(tblHoaDonPanelHoaDon.getSelectedRow(), 1).toString();

        if (!billService.checkStatus(maHD)) {
            JOptionPane.showMessageDialog(this, "Không thể thêm sản phẩm!");
            return;
        }

        int soLuong = productDetailService.getBySoLuong(maSP);
        float donGia = productDetailService.getByDonGia(maSP);
        int setSL = soLuong - Integer.parseInt(input);
        BillDetails billDetails = getDataBillDetails(Integer.parseInt(input), maSP, maHD, donGia);

        if (soLuong == 0) {
            JOptionPane.showMessageDialog(this, "Số lượng đã hết!");
            return;
        }
        if (Integer.parseInt(input) > soLuong) {
            JOptionPane.showMessageDialog(this, "Số lượng trong kho không đủ");
            return;
        }
        if (soLuong > 0) {
            productDetailService.updateSoLuong(maSP, setSL);
            loadDataSanPhamChiTietPanelHoaDon(productDetailService.getListProductDetal());
            if (!billDetailService.checkProducts(maSP, maHD)) {
                billDetailService.insert(billDetails);
                loadDataGioHang(billDetailService.getListBill(maHD));
            } else {
                int soLuongPanelGioHang = billDetailService.getSoLuong(maSP, maHD) + Integer.parseInt(input);
                int setSoLuong = soLuong - soLuongPanelGioHang;

                productDetailService.updateSoLuong(maSP, setSoLuong);
                String idSP = productDetailService.getByID(maSP);
                String idHD = billService.getByID(maHD);

                billDetailService.update(idSP, idHD, soLuongPanelGioHang);
                loadDataGioHang(billDetailService.getListBill(maHD));
            }
            lblTongTienPanelHoaDon.setText(billDetailService.sumDonGia(maHD) + " VND");
        }
    }//GEN-LAST:event_tblSanPhamPanelHoaDonMouseClicked

    private void btnXoaGioHangPanelHoaDonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnXoaGioHangPanelHoaDonActionPerformed
        String maHD = tblHoaDonPanelHoaDon.getValueAt(tblHoaDonPanelHoaDon.getSelectedRow(), 1).toString();
        if (!billService.checkStatus(maHD)) {
            JOptionPane.showMessageDialog(this, "Không thể xóa sản phẩm!");
            return;
        }
        String idHD = billService.getByID(maHD);
        defaultTableModel = (DefaultTableModel) tblGioHangPanelHoaDon.getModel();
        List<String> list = new ArrayList<>();
        for (int count = 0; count < defaultTableModel.getRowCount(); count++) {
            if ((boolean) defaultTableModel.getValueAt(count, 7) == true) {
                list.add((String) defaultTableModel.getValueAt(count, 1));
            }
        }
        for (String string : list) {
            int soLuong = productDetailService.getBySoLuong(string) + billDetailService.getSoLuong(string, maHD);
            productDetailService.updateSoLuong(string, soLuong);
            billDetailService.delete(idHD, productDetailService.getByID(string));
            loadDataGioHang(billDetailService.getListBill(maHD));
            loadDataSanPhamChiTietPanelHoaDon(productDetailService.getListProductDetal());
        }
    }//GEN-LAST:event_btnXoaGioHangPanelHoaDonActionPerformed

    private void tblGioHangPanelHoaDonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblGioHangPanelHoaDonMouseClicked


    }//GEN-LAST:event_tblGioHangPanelHoaDonMouseClicked

    private void btnSuaGioHangPanelHoaDonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSuaGioHangPanelHoaDonActionPerformed
        String maHD = tblHoaDonPanelHoaDon.getValueAt(tblHoaDonPanelHoaDon.getSelectedRow(), 1).toString();
        String maSP = tblGioHangPanelHoaDon.getValueAt(tblGioHangPanelHoaDon.getSelectedRow(), 1).toString();
        int soLuongBD = billDetailService.getSoLuong(maSP, maHD);
        int soLuongPD = productDetailService.getBySoLuong(maSP);
        String soLuongGH = tblGioHangPanelHoaDon.getValueAt(tblGioHangPanelHoaDon.getSelectedRow(), 3).toString();
        int soLuong = soLuongBD - Integer.parseInt(soLuongGH) + soLuongPD;


        productDetailService.updateSoLuong(maSP, soLuong);
        loadDataSanPhamChiTietPanelHoaDon(productDetailService.getListProductDetal());

        billDetailService.update(productDetailService.getByID(maSP), billService.getByID(maHD), Integer.parseInt(soLuongGH));
        loadDataGioHang(billDetailService.getListBill(maHD));
    }//GEN-LAST:event_btnSuaGioHangPanelHoaDonActionPerformed

    // Panel Đặt Hàng

    private void btnTimKiemPanelDatHangActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTimKiemPanelDatHangActionPerformed
        String sdt = txtSDTPanelDatHang.getText();
        if (!customerService.checkCustomer(sdt)) {
            JOptionPane.showMessageDialog(this, "Khách hàng không có trong hệ thống");
        }
        txtTenKhachHangPanelDatHang.setText(customerService.getByName(sdt));
    }//GEN-LAST:event_btnTimKiemPanelDatHangActionPerformed

    private void cbbHTTTPanelDatHangActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbbHTTTPanelDatHangActionPerformed
        if (cbbHTTTPanelDatHang.getSelectedIndex() == 1) {
            String[] tongTien = lblTongTienPanelDatHang.getText().split(".VND");
            txtTienKhachDuaPanelDatHang.setText(tongTien[0]);
        }
    }//GEN-LAST:event_cbbHTTTPanelDatHangActionPerformed

    private void btnTaoHoaDonPanelDatHangActionPerformed(ActionEvent evt) {//GEN-FIRST:event_btnTaoHoaDonPanelDatHangActionPerformed
        String sdt = txtSDTPanelDatHang.getText();
        if (customerService.checkCustomer(sdt)) {
            Customer customer = new Customer(customerService.getByID(sdt));
            Bill bill = getDataBill(customer);
            if (bill == null) {
                return;
            }
            if (billService.insert(bill)) {
                JOptionPane.showMessageDialog(this, "Tạo hóa đơn thành công");
                loadDataHoaDonPanelHoaDon(billService.getListBill());
            } else {
                JOptionPane.showMessageDialog(this, "Tạo hóa đơn thất bại");
            }
            loadDataHoaDonPanelHoaDon(billService.getListBill());
        } else {
            String tenKH = txtTenKhachHangPanelDatHang.getText();
            String ghiChu = txtGhiChuPanelDatHang.getText();
            String diaChi = txtDiaChiKhachHang.getText();
            Staff staff = new Staff(staffService.getByID(lblMaNhanVienPanelMain.getText()));

            Bill bill = new Bill(staff, codeBillTangDan(), getNgayHienTai(), 0, diaChi, ghiChu, tenKH, sdt);

            if (billService.insert(bill)) {
                JOptionPane.showMessageDialog(this, "Tạo hóa đơn thành công");
                loadDataHoaDonPanelHoaDon(billService.getListBill());
            } else {
                JOptionPane.showMessageDialog(this, "Tạo hóa đơn thất bại");
            }
            loadDataHoaDonPanelHoaDon(billService.getListBill());
        }
    }//GEN-LAST:event_btnTaoHoaDonPanelDatHangActionPerformed

    private void btnDangGiaoPanelGiaoHangActionPerformed(ActionEvent evt) {//GEN-FIRST:event_btnDangGiaoPanelGiaoHangActionPerformed
        String maHD = tblHoaDonPanelHoaDon.getValueAt(tblHoaDonPanelHoaDon.getSelectedRow(), 1).toString();
        if (billDetailService.getListBill(maHD).isEmpty()) {
            JOptionPane.showMessageDialog(this, "Giỏ hàng không có sản phẩm");
        }
        if (billService.updateGiaoHang(maHD, 3, getNgayHienTai())) {
            JOptionPane.showMessageDialog(this, "Đã cập nhật trạng thái đơn hàng");
            loadDataHoaDonPanelHoaDon(billService.getListBill());
        } else {
            JOptionPane.showMessageDialog(this, "Cập nhật trạng thái đơn hàng thất bại");
        }
    }//GEN-LAST:event_btnDangGiaoPanelGiaoHangActionPerformed

    private void btnDaGiaoPanelGiaoHangActionPerformed(ActionEvent evt) {//GEN-FIRST:event_btnDaGiaoPanelGiaoHangActionPerformed
        String email = customerService.getByEmail(txtSDTPanelDatHang.getText());
        String maHD = tblHoaDonPanelHoaDon.getValueAt(tblHoaDonPanelHoaDon.getSelectedRow(), 1).toString();
        if (billService.updateDaNhan(maHD, 1, getNgayHienTai())) {
            JOptionPane.showMessageDialog(this, "Đã cập nhật trạng thái đơn hàng");
            String url = "D:\\PhanMemQuanLyBanQuanGiayTTNam\\Bill" + maHD + ".pdf";
            exportPDFBill(url, billService.getList(maHD), lblTongTienPanelDatHang.getText(), txtTenKhachHangPanelDatHang.getText(), lblTienThuaPanelDatHang.getText());
            loadDataHoaDonPanelHoaDon(billService.getListBill());
            sendEmail.guiHoaDon(email, url);
        } else {
            JOptionPane.showMessageDialog(this, "Cập nhật trạng thái đơn hàng thất bại");
        }
        clearFormBill();
    }//GEN-LAST:event_btnDaGiaoPanelGiaoHangActionPerformed

    private void btnTraHangPanelGiaoHangActionPerformed(ActionEvent evt) {//GEN-FIRST:event_btnTraHangPanelGiaoHangActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnTraHangPanelGiaoHangActionPerformed

    private void txtTienKhachDuaPanelDatHangCaretUpdate(CaretEvent evt) {//GEN-FIRST:event_txtTienKhachDuaPanelDatHangCaretUpdate
        String[] tongTien = lblTongTienPanelDatHang.getText().split(".VND");
        for (String s : tongTien) {
            double tienKhachDua = Double.parseDouble(txtTienKhachDuaPanelDatHang.getText());
            double tienThua = tienKhachDua - Double.parseDouble(s);
            int tien = (int) Math.round(tienThua);
            lblTienThuaPanelDatHang.setText(tien + ".VND");
        }
    }//GEN-LAST:event_txtTienKhachDuaPanelDatHangCaretUpdate

    // Panel Lịch Sử
    private void loadDataHoaDonPanelLichSu(List<BillCustomModel> list) {
        defaultTableModel = (DefaultTableModel) tblHoaDonPanelLichSu.getModel();
        defaultTableModel.setRowCount(0);
        int count = 1;
        for (BillCustomModel billCustomModel : list) {
            defaultTableModel.addRow(new Object[]{count++, billCustomModel.getMaHD(), billCustomModel.getTenNV(), billCustomModel.getTenKH(), billCustomModel.getNgayTao(), billCustomModel.trangThaiHD(billCustomModel.getTrangThai())});
        }
    }

    private void loadDataSanPhamChiTietPanelLichSu(List<ProductDetailCustomModel> list) {
        defaultTableModel = (DefaultTableModel) tblSanPhamPanelLichSu.getModel();
        defaultTableModel.setRowCount(0);
        int count = 1;
        for (ProductDetailCustomModel productDetailCustomModel : list) {
            defaultTableModel.addRow(new Object[]{
                    count++,
                    productDetailCustomModel.getMaSP(), productDetailCustomModel.getTenSP(), productDetailCustomModel.getLoaiSP(), productDetailCustomModel.getKichCo(), productDetailCustomModel.getMauSac(), productDetailCustomModel.getChatLieu(), productDetailCustomModel.getSoLuong(), productDetailCustomModel.getDonGia(), productDetailCustomModel.getSoLuong() * productDetailCustomModel.getDonGia()});
        }
    }

    private void btnLichSuActionPerformed(ActionEvent evt) {//GEN-FIRST:event_btnLichSuActionPerformed
        btnThongKe.setBackground(colorTrang);
        btnSanPham.setBackground(colorTrang);
        btnHoaDon.setBackground(colorTrang);
        btnLichSu.setBackground(colorXanh);
        btnKhuyenMai.setBackground(colorTrang);
        btnNhanVien.setBackground(colorTrang);
        btnKhachHang.setBackground(colorTrang);
        btnDoiMatKhau.setBackground(colorTrang);
        setPanel(panelLichSu);
        loadDataHoaDonPanelLichSu(billService.getListBill());
    }//GEN-LAST:event_btnLichSuActionPerformed

    private void tblHoaDonPanelLichSuMouseClicked(MouseEvent evt) {//GEN-FIRST:event_tblHoaDonPanelLichSuMouseClicked
        String maHD = tblHoaDonPanelLichSu.getValueAt(tblHoaDonPanelLichSu.getSelectedRow(), 1).toString();
        loadDataSanPhamChiTietPanelLichSu(billDetailService.getListBillPanelLichSu(maHD));
        int sum = (int) Math.round(billDetailService.sumDonGia(maHD));
        List<Bill> listBill = billService.getList(maHD);
        for (Bill bill : listBill) {
            lblMaNVPanelLichSu.setText(bill.getStaff().getCode());
            lblTenNVPanelLichSu.setText(bill.getStaff().getFirstName() + " " + bill.getStaff().getLastName());
            lblTenKHPanelLichSu.setText(bill.getNameCustomer());
            lblSDTPanelLichSu.setText(bill.getPhoneNumberCustomer());
            txtDiaChiPanelLichSu.setText(bill.getAddress());
            lblTongTienPanelLichSu.setText(sum + ".VND");
            lblNgayTaoPanelLichSu.setText(String.valueOf(bill.getDateCreated()));
            txtMoTaPanelLichSu.setText(bill.getNote());
        }
    }//GEN-LAST:event_tblHoaDonPanelLichSuMouseClicked

    private void btnTimKiemMaHDPanelLichSuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTimKiemMaHDPanelLichSuActionPerformed
        String maHD = txtMaHoaDonPanelLichSua.getText();
        int sum = (int) Math.round(billDetailService.sumDonGia(maHD));
        if (billService.getList(maHD).isEmpty())
            JOptionPane.showMessageDialog(this, "Không tìm thấy mã hóa đơn");
        else {
            loadDataHoaDonPanelLichSu(billService.getListBill(maHD));
            tblHoaDonPanelLichSu.setRowSelectionInterval(0, 0);
            loadDataSanPhamChiTietPanelLichSu(billDetailService.getListBillPanelLichSu(maHD));
            List<Bill> listBill = billService.getList(maHD);
            for (Bill bill : listBill) {
                lblMaNVPanelLichSu.setText(bill.getStaff().getCode());
                lblTenNVPanelLichSu.setText(bill.getStaff().getLastName() + " " + bill.getStaff().getFirstName());
                lblTenKHPanelLichSu.setText(bill.getNameCustomer());
                lblSDTPanelLichSu.setText(bill.getPhoneNumberCustomer());
                txtDiaChiPanelLichSu.setText(bill.getAddress());
                lblTongTienPanelLichSu.setText(sum + ".VND");
                lblNgayTaoPanelLichSu.setText(String.valueOf(bill.getDateCreated()));
                txtMoTaPanelLichSu.setText(bill.getNote());
            }
        }
    }//GEN-LAST:event_btnTimKiemMaHDPanelLichSuActionPerformed

    // Panel Khuyến Mãi
    private void loadDataKhuyenMai(List<Promotion> list) {
        defaultTableModel = (DefaultTableModel) tblKhuyenMaiPanelKhuyenMai.getModel();
        defaultTableModel.setRowCount(0);
        int count = 1;
        for (Promotion promotion : list) {
            defaultTableModel.addRow(new Object[]{count++, promotion.getCode(), promotion.getName(), promotion.getStartDay(), promotion.getEndDay(), promotion.getDecreaseNumber()});
        }
    }

    private void clearPanelKhuyenMai() {
        txtMaKhuyenMaiPanelKhuyenMai.setText("");
        txtTenChuongTrinhPanelKhuyenMai.setText("");
        spnPhanTramKhuyenMaiPanelKhuyenMai.setValue(0);
    }

    private String codePromotionTangDan() {
        String code = "";
        List<Promotion> list = promotionService.getList();
        if (list.isEmpty()) {
            code = "KM0001";
        } else {
            int max = 0;
            for (Promotion promotion : list) {
                int so = Integer.parseInt(promotion.getCode().substring(2));
                if (so > max) {
                    max = so;
                }
            }
            max++;
            if (max < 10) {
                code = "KM000" + max;
            } else if (max < 100) {
                code = "KM00" + max;
            } else if (max < 1000) {
                code = "KM0" + max;
            } else {
                code = "KM" + max;
            }
        }
        return code;
    }

    private Promotion getPanelKhuyenMai() {
        String maKM = codePromotionTangDan();
        String tenCT = txtTenChuongTrinhPanelKhuyenMai.getText();
        String ngayBatDau = ((JTextField) txtNgayBatDauPanelKhuyenMai.getDateEditor().getUiComponent()).getText();
        String ngayKetThuc = ((JTextField) txtNgayKetThucPanelKhuyenMai.getDateEditor().getUiComponent()).getText();
        int phanTramKhuyenMai = (int) spnPhanTramKhuyenMaiPanelKhuyenMai.getValue();
        if (tenCT.trim().equals("")) {
            JOptionPane.showMessageDialog(this, "Không được để trống");
        }
        Date date1 = Date.valueOf(ngayBatDau);
        Date date2 = Date.valueOf(ngayKetThuc);

        return new Promotion(maKM, tenCT, phanTramKhuyenMai, date1, date2, 1);
    }

    private void btnKhuyenMaiActionPerformed(ActionEvent evt) {//GEN-FIRST:event_btnKhuyenMaiActionPerformed
        btnThongKe.setBackground(colorTrang);
        btnSanPham.setBackground(colorTrang);
        btnHoaDon.setBackground(colorTrang);
        btnLichSu.setBackground(colorTrang);
        btnKhuyenMai.setBackground(colorXanh);
        btnNhanVien.setBackground(colorTrang);
        btnKhachHang.setBackground(colorTrang);
        btnDoiMatKhau.setBackground(colorTrang);
        setPanel(panelKhuyenMai);
        loadDataKhuyenMai(promotionService.getListOn());
    }//GEN-LAST:event_btnKhuyenMaiActionPerformed

    private void btnXemKhuyenMaiPanelKhuyenMaiActionPerformed(ActionEvent evt) {//GEN-FIRST:event_btnXemKhuyenMaiPanelKhuyenMaiActionPerformed
        new XemKhuyenMaiKetThuc().setVisible(true);
    }//GEN-LAST:event_btnXemKhuyenMaiPanelKhuyenMaiActionPerformed

    private void btnXoaFormPanelKhuyenMaiActionPerformed(ActionEvent evt) {//GEN-FIRST:event_btnXoaFormPanelKhuyenMaiActionPerformed
        clearPanelKhuyenMai();
    }//GEN-LAST:event_btnXoaFormPanelKhuyenMaiActionPerformed

    private void btnThemPanelKhuyenMaiActionPerformed(ActionEvent evt) {//GEN-FIRST:event_btnThemPanelKhuyenMaiActionPerformed
        Promotion promotion = getPanelKhuyenMai();
        if (promotion == null) {
            return;
        }
        if (promotionService.insert(promotion)) {
            loadDataKhuyenMai(promotionService.getListOn());
            clearPanelKhuyenMai();
            JOptionPane.showMessageDialog(this, "Thêm Thành Công");
        } else {
            JOptionPane.showMessageDialog(this, "Thêm Thất Bại");
        }
    }//GEN-LAST:event_btnThemPanelKhuyenMaiActionPerformed

    private void btnSuaPanelKhuyenMaiActionPerformed(ActionEvent evt) {//GEN-FIRST:event_btnSuaPanelKhuyenMaiActionPerformed
        if (txtMaKhuyenMaiPanelKhuyenMai.getText().trim().length() == 0) {
            JOptionPane.showMessageDialog(this, "Chưa chọn khuyến mãi nào trên table");
            return;
        }
        Promotion promotion = getPanelKhuyenMai();
        if (promotion == null) {
            return;
        }
        if (promotionService.update(txtMaKhuyenMaiPanelKhuyenMai.getText(), promotion)) {
            loadDataKhuyenMai(promotionService.getListOn());
            clearPanelKhuyenMai();
            JOptionPane.showMessageDialog(this, "Cập Nhật Thành Công");
        } else {
            JOptionPane.showMessageDialog(this, "Cập Nhật Thất Bại");
        }
    }//GEN-LAST:event_btnSuaPanelKhuyenMaiActionPerformed

    private void btnAnPanelKhuyenMaiActionPerformed(ActionEvent evt) {//GEN-FIRST:event_btnAnPanelKhuyenMaiActionPerformed
        int confirm = JOptionPane.showConfirmDialog(this, "Bạn muốn kết thúc khuyến mãi này không ?", "Thông báo", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            if (promotionService.hideOrShow(txtMaKhuyenMaiPanelKhuyenMai.getText(), 0)) {
                loadDataKhuyenMai(promotionService.getListOn());
                clearPanelNhanVien();
                JOptionPane.showMessageDialog(this, "Kết Thúc Thành Công");
            } else {
                JOptionPane.showMessageDialog(this, "Kết Thúc Thất Bại");
            }
        } else {
            return;
        }
    }//GEN-LAST:event_btnAnPanelKhuyenMaiActionPerformed

    private void tblKhuyenMaiPanelKhuyenMaiMouseClicked(MouseEvent evt) {//GEN-FIRST:event_tblKhuyenMaiPanelKhuyenMaiMouseClicked
        int row = tblKhuyenMaiPanelKhuyenMai.getSelectedRow();
        txtMaKhuyenMaiPanelKhuyenMai.setText(tblKhuyenMaiPanelKhuyenMai.getValueAt(row, 1).toString());
        txtTenChuongTrinhPanelKhuyenMai.setText(tblKhuyenMaiPanelKhuyenMai.getValueAt(row, 2).toString());
        txtNgayBatDauPanelKhuyenMai.setDate(Date.valueOf(tblKhuyenMaiPanelKhuyenMai.getValueAt(row, 3).toString()));
        txtNgayKetThucPanelKhuyenMai.setDate(Date.valueOf(tblKhuyenMaiPanelKhuyenMai.getValueAt(row, 4).toString()));
        spnPhanTramKhuyenMaiPanelKhuyenMai.setValue(tblKhuyenMaiPanelKhuyenMai.getValueAt(row, 5));
        loadDataSanPhamPanelKhuyenMai(productDetailService.getListProductDetal());
    }//GEN-LAST:event_tblKhuyenMaiPanelKhuyenMaiMouseClicked

    private void loadDataSanPhamPanelKhuyenMai(List<ProductDetailCustomModel> list) {
        defaultTableModel = (DefaultTableModel) tblSanPhamPanelKhuyenMai.getModel();
        defaultTableModel.setRowCount(0);
        int count = 1;
        for (ProductDetailCustomModel productDetailCustomModel : list) {
            defaultTableModel.addRow(new Object[]{count++, productDetailCustomModel.getMaSP(), productDetailCustomModel.getTenSP(), productDetailCustomModel.getLoaiSP(), productDetailCustomModel.getMauSac(), productDetailCustomModel.getKichCo(), productDetailCustomModel.getChatLieu(), promotionDetailService.checkBoxSanPham(productDetailCustomModel.getMaSP(), txtMaKhuyenMaiPanelKhuyenMai.getText())});
        }
    }

    private PromotionDetails getDataPromotionDetail(String codeSP) {
        String codeKM = txtMaKhuyenMaiPanelKhuyenMai.getText();

        if (codeKM.trim().length() == 0) {
            JOptionPane.showMessageDialog(this, "Chưa chọn chương trình khuyến mại");
            return null;
        }

        Promotion promotion = new Promotion(promotionService.getByID(codeKM));
        ProductDetails productDetails = new ProductDetails(productDetailService.getByID(codeSP));

        return new PromotionDetails(promotion, productDetails, 1);
    }

    private void btnChonTatCaPanelKhuyenMaiActionPerformed(ActionEvent evt) {//GEN-FIRST:event_btnChonTatCaPanelKhuyenMaiActionPerformed
        for (int i = 0; i < tblSanPhamPanelKhuyenMai.getRowCount(); i++) {
            tblSanPhamPanelKhuyenMai.setValueAt(true, i, 7);
        }
        defaultTableModel = (DefaultTableModel) tblSanPhamPanelKhuyenMai.getModel();
        List<String> list = new ArrayList<>();
        for (int i = 0; i < tblSanPhamPanelKhuyenMai.getRowCount(); i++) {
            list.add((String) defaultTableModel.getValueAt(i, 1));
        }

        int count = 0;
        for (String string : list) {
            PromotionDetails promotionDetails = getDataPromotionDetail(string);
            if (promotionDetails == null) {
                return;
            }
            boolean chon = (boolean) defaultTableModel.getValueAt(count, 7);

            if (chon == true) {
                promotionDetailService.insert(promotionDetails);
                loadDataSanPhamPanelKhuyenMai(productDetailService.getListProductDetal());
            }
        }
    }//GEN-LAST:event_btnChonTatCaPanelKhuyenMaiActionPerformed

    private void tblSanPhamPanelKhuyenMaiMouseClicked(MouseEvent evt) {//GEN-FIRST:event_tblSanPhamPanelKhuyenMaiMouseClicked
        String maSP = tblSanPhamPanelKhuyenMai.getValueAt(tblSanPhamPanelKhuyenMai.getSelectedRow(), 1).toString();
        String maKM = txtMaKhuyenMaiPanelKhuyenMai.getText();

        PromotionDetails promotionDetails = getDataPromotionDetail(maSP);
        if (promotionDetails == null) {
            return;
        }
        boolean chon = (boolean) tblSanPhamPanelKhuyenMai.getValueAt(tblSanPhamPanelKhuyenMai.getSelectedRow(), 7);

        if (chon == true) {
            promotionDetailService.insert(promotionDetails);
            loadDataSanPhamPanelKhuyenMai(productDetailService.getListProductDetal());
        }
        if (chon == false) {
            String idSP = productDetailService.getByID(maSP);
            String idKM = promotionService.getByID(maKM);
            promotionDetailService.delete(idSP, idKM);
            loadDataSanPhamPanelKhuyenMai(productDetailService.getListProductDetal());
        }
    }//GEN-LAST:event_tblSanPhamPanelKhuyenMaiMouseClicked

    // Panel Nhân Viên
    public void loadDataNhanVienAn() {
        loadDataNhanVien(staffService.getList());
    }

    private void loadDataNhanVien(List<StaffCustomModel> list) {
        defaultTableModel = (DefaultTableModel) tblNhanVienPanelNhanVien.getModel();
        defaultTableModel.setRowCount(0);
        int count = 1;
        for (StaffCustomModel staffCustomModel : list) {
            defaultTableModel.addRow(new Object[]{count++, staffCustomModel.getMaNV(), staffCustomModel.getHoTen(), staffCustomModel.getNgaySinh(), staffCustomModel.getGioiTinh(), staffCustomModel.getSdt(), staffCustomModel.getEmail(), staffCustomModel.getDiaChi(), staffCustomModel.getChucVu() == 1 ? "Quản Lý" : "Nhân Viên"});
        }
    }

    private void clearPanelNhanVien() {
        txtMaNhanVienPanelNhanVien.setText("");
        txtHoPanelNhanVien.setText("");
        txtTenPanelNhanVien.setText("");
        rdoNamPanelNhanVien.setSelected(true);
        txtNgaySinhNhanVienPanelNhanVien.setDateFormatString("yyyy-MM-dd");
        txtSDTPanelNhanVien.setText("");
        txtEmailPanelNhanVien.setText("");
        cbbChucVuPanelNhanVien.setSelectedIndex(0);
        txtDiaChiPanelNhanVien.setText("");
        txtMatKhauPanelNhanVien.setText("");
    }

    private String codeStaffTangDan() {
        String code = "";
        List<StaffCustomModel> list = staffService.getList();
        if (list.isEmpty()) {
            code = "NV0001";
        } else {
            int max = 0;
            for (StaffCustomModel staffCustomModel : list) {
                int so = Integer.parseInt(staffCustomModel.getMaNV().substring(2));
                if (so > max) {
                    max = so;
                }
            }
            max++;
            if (max < 10) {
                code = "NV000" + max;
            } else if (max < 100) {
                code = "NV00" + max;
            } else if (max < 1000) {
                code = "NV0" + max;
            } else {
                code = "NV" + max;
            }
        }
        return code;
    }

    private Staff getPanelNhanVien() {
        String maNV = codeStaffTangDan();
        String ho = txtHoPanelNhanVien.getText();
        String ten = txtTenPanelNhanVien.getText();
        String gioiTinh = rdoNamPanelNhanVien.isSelected() ? "Nam" : "Nữ";
        String ngaySinh = ((JTextField) txtNgaySinhNhanVienPanelNhanVien.getDateEditor().getUiComponent()).getText();
        String sdt = txtSDTPanelNhanVien.getText();
        String email = txtEmailPanelNhanVien.getText();
        int chucVu = cbbChucVuPanelNhanVien.getSelectedIndex();
        String diaChi = txtDiaChiPanelNhanVien.getText();
        String matKhau = logicUtil.taoMaHoa(txtMatKhauPanelNhanVien.getText());

        if (ho.equals("") || ten.equals("") || ngaySinh.equals("") || sdt.equals("") || email.equals("") || diaChi.equals("") || matKhau.equals("")) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ thông tin");
            return null;
        } else if (email.matches("^[a-zA-Z0-9]+@[a-zA-Z0-9]+(\\.[a-zA-Z0-9]+)$") == false) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập đúng định dạng email");
            return null;
        } else if (sdt.matches("^[0-9]{10}$") == false) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập đúng định dạng số điện thoại");
            return null;
        } else if (ngaySinh.matches("^[0-9]{4}-[0-9]{2}-[0-9]{2}$") == false) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập đúng định dạng ngày sinh");
            return null;
        }

        Date date = Date.valueOf(ngaySinh);
        return new Staff("newid()", maNV, ten, ho, gioiTinh, date, diaChi, sdt, email, matKhau, 1, chucVu);
    }

    private void btnNhanVienActionPerformed(ActionEvent evt) {//GEN-FIRST:event_btnNhanVienActionPerformed
        btnThongKe.setBackground(colorTrang);
        btnSanPham.setBackground(colorTrang);
        btnHoaDon.setBackground(colorTrang);
        btnLichSu.setBackground(colorTrang);
        btnKhuyenMai.setBackground(colorTrang);
        btnNhanVien.setBackground(colorXanh);
        btnKhachHang.setBackground(colorTrang);
        btnDoiMatKhau.setBackground(colorTrang);
        setPanel(panelNhanVien);
        loadDataNhanVien(staffService.getList());
        rdoNamPanelNhanVien.setSelected(true);
    }//GEN-LAST:event_btnNhanVienActionPerformed

    private void btnXoaFormPanelNhanVienActionPerformed(ActionEvent evt) {//GEN-FIRST:event_btnXoaFormPanelNhanVienActionPerformed
        clearPanelNhanVien();
    }//GEN-LAST:event_btnXoaFormPanelNhanVienActionPerformed

    private void btnThemPanelNhanVienActionPerformed(ActionEvent evt) {//GEN-FIRST:event_btnThemPanelNhanVienActionPerformed
        Staff staff = getPanelNhanVien();
        if (staff == null) {
            return;
        }
        logicUtil.taoMaQR(codeStaffTangDan());
        if (staffService.insert(staff)) {
            loadDataNhanVien(staffService.getList());
            clearPanelNhanVien();
            JOptionPane.showMessageDialog(this, "Thêm Thành Công");
        } else {
            JOptionPane.showMessageDialog(this, "Thêm Thất Bại");
        }
    }//GEN-LAST:event_btnThemPanelNhanVienActionPerformed

    private void btnSuaPanelNhanVienActionPerformed(ActionEvent evt) {//GEN-FIRST:event_btnSuaPanelNhanVienActionPerformed
        if (txtMaNhanVienPanelNhanVien.getText().trim().length() == 0) {
            JOptionPane.showMessageDialog(this, "Chưa chọn nhân viên nào trên table");
            return;
        }
        Staff staff = getPanelNhanVien();
        if (staff == null) {
            return;
        }
        if (staffService.update(txtMaNhanVienPanelNhanVien.getText(), staff)) {
            loadDataNhanVien(staffService.getList());
            clearPanelNhanVien();
            JOptionPane.showMessageDialog(this, "Cập Nhật Thành Công");
        } else {
            JOptionPane.showMessageDialog(this, "Cập Nhật Thất Bại");
        }
    }//GEN-LAST:event_btnSuaPanelNhanVienActionPerformed

    private void btnAnPanelNhanVienActionPerformed(ActionEvent evt) {//GEN-FIRST:event_btnAnPanelNhanVienActionPerformed
        int confirm = JOptionPane.showConfirmDialog(this, "Bạn muốn ẩn Nhân Viên này không ?", "Thông báo", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            if (staffService.hideOrShow(txtMaNhanVienPanelNhanVien.getText(), 0)) {
                loadDataNhanVien(staffService.getList());
                clearPanelNhanVien();
                JOptionPane.showMessageDialog(this, "Ẩn Thành Công");
            } else {
                JOptionPane.showMessageDialog(this, "Ẩn Thất Bại");
            }
        } else {
            return;
        }
    }//GEN-LAST:event_btnAnPanelNhanVienActionPerformed

    private void btnXemNhanVienDaNghiPanelNVActionPerformed(ActionEvent evt) {//GEN-FIRST:event_btnXemNhanVienDaNghiPanelNVActionPerformed
        new XemNhanVienAn().setVisible(true);
    }//GEN-LAST:event_btnXemNhanVienDaNghiPanelNVActionPerformed

    private void tblNhanVienPanelNhanVienMouseClicked(MouseEvent evt) {//GEN-FIRST:event_tblNhanVienPanelNhanVienMouseClicked
        int row = tblNhanVienPanelNhanVien.getSelectedRow();
        if (row == -1) {
            return;
        }
        txtMaNhanVienPanelNhanVien.setText(tblNhanVienPanelNhanVien.getValueAt(row, 1).toString());
        txtHoPanelNhanVien.setText(staffService.getByLastName(tblNhanVienPanelNhanVien.getValueAt(row, 1).toString()));
        txtTenPanelNhanVien.setText(staffService.getByFisrtName(tblNhanVienPanelNhanVien.getValueAt(row, 1).toString()));
        txtNgaySinhNhanVienPanelNhanVien.setDate(Date.valueOf(tblNhanVienPanelNhanVien.getValueAt(row, 3).toString()));
        rdoNamPanelNhanVien.setSelected(tblNhanVienPanelNhanVien.getValueAt(row, 4).toString().equals("Nam"));
        rdoNuPanelNhanVien.setSelected(tblNhanVienPanelNhanVien.getValueAt(row, 4).toString().equals("Nữ"));
        txtSDTPanelNhanVien.setText(tblNhanVienPanelNhanVien.getValueAt(row, 5).toString());
        txtEmailPanelNhanVien.setText(tblNhanVienPanelNhanVien.getValueAt(row, 6).toString());
        txtDiaChiPanelNhanVien.setText(tblNhanVienPanelNhanVien.getValueAt(row, 7).toString());
        cbbChucVuPanelNhanVien.setSelectedItem(tblNhanVienPanelNhanVien.getValueAt(row, 8).toString());
    }//GEN-LAST:event_tblNhanVienPanelNhanVienMouseClicked

    // Panel Khách Hàng
    private void loadDataKhachHang(List<Customer> list) {
        defaultTableModel = (DefaultTableModel) tblKhachHangPanelKhachHang.getModel();
        defaultTableModel.setRowCount(0);
        int count = 1;
        for (Customer customer : list) {
            defaultTableModel.addRow(new Object[]{count++, customer.getCode(), customer.getLastName() + " " + customer.getFirstName(), customer.getEmail(), customer.getPhoneNumber()});
        }
    }

    private void clearPanelKhachHang() {
        txtMaKhachHangPanelKhachHang.setText("");
        txtHoPanelKhachHang.setText("");
        txtTenPanelKhachHang.setText("");
        txtEmailPanelKhachHang.setText("");
        txtSDTPanelKhachHang.setText("");
    }

    private String codeCustomerTangDan() {
        String code = "";
        List<Customer> list = customerService.getList();
        if (list.isEmpty()) {
            code = "KH0001";
        } else {
            int max = 0;
            for (Customer customer : list) {
                int so = Integer.parseInt(customer.getCode().substring(2));
                if (so > max) {
                    max = so;
                }
            }
            max++;
            if (max < 10) {
                code = "KH000" + max;
            } else if (max < 100) {
                code = "KH00" + max;
            } else if (max < 1000) {
                code = "KH0" + max;
            } else {
                code = "KH" + max;
            }
        }
        return code;
    }

    private Customer getPanelKhachHang() {
        String maKH = codeCustomerTangDan();
        String ho = txtHoPanelKhachHang.getText();
        String ten = txtTenPanelKhachHang.getText();
        String email = txtEmailPanelKhachHang.getText();
        String sdt = txtSDTPanelKhachHang.getText();

        if (ho.equals("") || ten.equals("") || sdt.equals("") || email.equals("")) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ thông tin");
            return null;
        } else if (email.matches("^[a-zA-Z0-9]+@[a-zA-Z0-9]+(\\.[a-zA-Z0-9]+)$") == false) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập đúng định dạng email");
            return null;
        } else if (sdt.matches("^[0-9]{10}$") == false) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập đúng định dạng số điện thoại");
            return null;
        }

        return new Customer(maKH, ho, ten, sdt, email);
    }


    private void btnKhachHangActionPerformed(ActionEvent evt) {//GEN-FIRST:event_btnKhachHangActionPerformed
        btnThongKe.setBackground(colorTrang);
        btnSanPham.setBackground(colorTrang);
        btnHoaDon.setBackground(colorTrang);
        btnLichSu.setBackground(colorTrang);
        btnKhuyenMai.setBackground(colorTrang);
        btnNhanVien.setBackground(colorTrang);
        btnKhachHang.setBackground(colorXanh);
        btnDoiMatKhau.setBackground(colorTrang);
        setPanel(panelKhachHang);
        loadDataKhachHang(customerService.getList());
    }//GEN-LAST:event_btnKhachHangActionPerformed

    private void btnXoaFormPanelKhachHangActionPerformed(ActionEvent evt) {//GEN-FIRST:event_btnXoaFormPanelKhachHangActionPerformed
        clearPanelKhachHang();
    }//GEN-LAST:event_btnXoaFormPanelKhachHangActionPerformed

    private void btnThemPanelKhachHangActionPerformed(ActionEvent evt) {//GEN-FIRST:event_btnThemPanelKhachHangActionPerformed
        Customer customer = getPanelKhachHang();
        if (customer == null) {
            return;
        }
        if (customerService.insert(customer)) {
            loadDataKhachHang(customerService.getList());
            clearPanelKhachHang();
            JOptionPane.showMessageDialog(this, "Thêm Thành Công");
        } else {
            JOptionPane.showMessageDialog(this, "Thêm Thất Bại");
        }
    }//GEN-LAST:event_btnThemPanelKhachHangActionPerformed

    private void btnSuaPanelKhachHangActionPerformed(ActionEvent evt) {//GEN-FIRST:event_btnSuaPanelKhachHangActionPerformed
        if (txtMaKhachHangPanelKhachHang.getText().trim().length() == 0) {
            JOptionPane.showMessageDialog(this, "Chưa chọn khách hàng nào trên table");
            return;
        }
        Customer customer = getPanelKhachHang();
        if (customer == null) {
            return;
        }
        if (customerService.update(customer, txtMaKhachHangPanelKhachHang.getText())) {
            loadDataKhachHang(customerService.getList());
            clearPanelKhachHang();
            JOptionPane.showMessageDialog(this, "Cập Nhật Thành Công");
        } else {
            JOptionPane.showMessageDialog(this, "Cập Nhật Thất Bại");
        }
    }//GEN-LAST:event_btnSuaPanelKhachHangActionPerformed

    private void txtTimKiemKhachHangPanelKHCaretUpdate(CaretEvent evt) {//GEN-FIRST:event_txtTimKiemKhachHangPanelKHCaretUpdate
        String sdtKH = txtTimKiemKhachHangPanelKH.getText();
        loadDataKhachHang(customerService.search(sdtKH));
        if (customerService.search(sdtKH).isEmpty()) {
            JOptionPane.showMessageDialog(this, "Không tìm thấy khách hàng");
        }
    }//GEN-LAST:event_txtTimKiemKhachHangPanelKHCaretUpdate

    private void tblKhachHangPanelKhachHangMouseClicked(MouseEvent evt) {//GEN-FIRST:event_tblKhachHangPanelKhachHangMouseClicked
        int row = tblKhachHangPanelKhachHang.getSelectedRow();
        txtMaKhachHangPanelKhachHang.setText(tblKhachHangPanelKhachHang.getValueAt(row, 1).toString());
        txtHoPanelKhachHang.setText(customerService.getByLastName(tblKhachHangPanelKhachHang.getValueAt(row, 1).toString()));
        txtTenPanelKhachHang.setText(customerService.getByFisrtName(tblKhachHangPanelKhachHang.getValueAt(row, 1).toString()));
        txtEmailPanelKhachHang.setText(tblKhachHangPanelKhachHang.getValueAt(row, 3).toString());
        txtSDTPanelKhachHang.setText(tblKhachHangPanelKhachHang.getValueAt(row, 4).toString());
    }//GEN-LAST:event_tblKhachHangPanelKhachHangMouseClicked

    // Panel Đổi mật khẩu
    private void btnDoiMatKhauActionPerformed(ActionEvent evt) {//GEN-FIRST:event_btnDoiMatKhauActionPerformed
        btnThongKe.setBackground(colorTrang);
        btnSanPham.setBackground(colorTrang);
        btnHoaDon.setBackground(colorTrang);
        btnLichSu.setBackground(colorTrang);
        btnKhuyenMai.setBackground(colorTrang);
        btnNhanVien.setBackground(colorTrang);
        btnKhachHang.setBackground(colorTrang);
        btnDoiMatKhau.setBackground(colorXanh);
        txtLoadCaptchaPanelDoiMatKhau.setText("");
        setPanel(panelDoiMatKhau);
    }//GEN-LAST:event_btnDoiMatKhauActionPerformed

    public void ranDomCaptcha() {
        Random random = new Random();
        int captcha = random.nextInt(100000, 1000000);
        txtLoadCaptchaPanelDoiMatKhau.setText(String.valueOf(captcha));
    }

    private void btnLoadCaptchaPanelDoiMatKhauActionPerformed(ActionEvent evt) {//GEN-FIRST:event_btnLoadCaptchaPanelDoiMatKhauActionPerformed
        ranDomCaptcha();
    }//GEN-LAST:event_btnLoadCaptchaPanelDoiMatKhauActionPerformed

    private void btnLuuPanelDoiMatKhauActionPerformed(ActionEvent evt) {//GEN-FIRST:event_btnLuuPanelDoiMatKhauActionPerformed
        String taiKhoan = txtTaiKhoanPanelDoiMatKhau.getText();
        String matKhauCu = logicUtil.taoMaHoa(txtMatKhauCuPanelDoiMatKhau.getText());
        String matKhauMoi = txtMatKhauMoiPanelDoiMatKhau.getText();
        String nhapLaiMatKhauMoi = txtXNMatKhauPanelDoiMatKhau.getText();
        String nhapCapcha = txtCaptchaPanelDoiMatKhau.getText();
        if (taiKhoan.equals("") || matKhauCu.equals("") || matKhauMoi.equals("") || nhapLaiMatKhauMoi.equals("") || nhapCapcha.equals("")) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ thông tin");
            ranDomCaptcha();
            return;
        } else {
            if (staffService.checkAccountStaff(taiKhoan)) {
                if (matKhauCu.equals(staffService.getByPassword(taiKhoan))) {
                } else {
                    JOptionPane.showMessageDialog(this, "Sai mật khẩu");
                    ranDomCaptcha();
                    return;
                }
            } else {
                JOptionPane.showMessageDialog(this, "Tài khoản này không có trong hệ thống");
                ranDomCaptcha();
                return;
            }
            if (!matKhauMoi.equals(nhapLaiMatKhauMoi)) {
                JOptionPane.showMessageDialog(this, "Mật khẩu mới không trùng khớp");
                ranDomCaptcha();
                return;
            }
            if (!nhapCapcha.equals(txtLoadCaptchaPanelDoiMatKhau.getText())) {
                JOptionPane.showMessageDialog(this, "Sai mã captcha");
                ranDomCaptcha();
                return;
            }
            if (matKhauCu.equals(staffService.getByPassword(taiKhoan)) && matKhauMoi.equals(nhapLaiMatKhauMoi) && nhapCapcha.equals(txtLoadCaptchaPanelDoiMatKhau.getText())) {
                staffService.updatePassword(taiKhoan, logicUtil.taoMaHoa(matKhauMoi));
                JOptionPane.showMessageDialog(this, "Đổi mật khẩu thành công");
            }
        }
    }//GEN-LAST:event_btnLuuPanelDoiMatKhauActionPerformed

    private void btnThoatPanelDoiMatKhauActionPerformed(ActionEvent evt) {//GEN-FIRST:event_btnThoatPanelDoiMatKhauActionPerformed
        btnThongKe.setBackground(colorXanh);
        btnSanPham.setBackground(colorTrang);
        btnHoaDon.setBackground(colorTrang);
        btnLichSu.setBackground(colorTrang);
        btnKhuyenMai.setBackground(colorTrang);
        btnNhanVien.setBackground(colorTrang);
        btnKhachHang.setBackground(colorTrang);
        btnDoiMatKhau.setBackground(colorTrang);
        setPanel(panelThongKe);
    }//GEN-LAST:event_btnThoatPanelDoiMatKhauActionPerformed

    // Panel Đăng xuất
    private void btnDangXuatActionPerformed(ActionEvent evt) {//GEN-FIRST:event_btnDangXuatActionPerformed
        int confirm = JOptionPane.showConfirmDialog(this, "Bạn muốn đăng xuất không ?", "Thông báo", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            new Login().setVisible(true);
            dispose();
            webcam.close();
        } else {
            return;
        }
    }//GEN-LAST:event_btnDangXuatActionPerformed




    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel Camera;
    private javax.swing.JButton btnAnPanelKhuyenMai;
    private javax.swing.JButton btnAnPanelNhanVien;
    private javax.swing.JButton btnAnPanelSanPhamCT;
    private javax.swing.JButton btnChonTatCaPanelKhuyenMai;
    private javax.swing.JButton btnDaGiaoPanelGiaoHang;
    private javax.swing.JButton btnDangGiaoPanelGiaoHang;
    private javax.swing.JButton btnDangXuat;
    private javax.swing.JButton btnDoiMatKhau;
    private javax.swing.JButton btnExit;
    private javax.swing.JButton btnHoaDon;
    private javax.swing.JButton btnHuyPanelHoaDon;
    private javax.swing.JButton btnKhachHang;
    private javax.swing.JButton btnKhuyenMai;
    private javax.swing.JButton btnLichSu;
    private javax.swing.JButton btnLoadCaptchaPanelDoiMatKhau;
    private javax.swing.JButton btnLuuPanelDoiMatKhau;
    private javax.swing.JButton btnNhanVien;
    private javax.swing.JButton btnSanPham;
    private javax.swing.JButton btnSuaGioHangPanelHoaDon;
    private javax.swing.JButton btnSuaPanelKhachHang;
    private javax.swing.JButton btnSuaPanelKhuyenMai;
    private javax.swing.JButton btnSuaPanelNhanVien;
    private javax.swing.JButton btnSuaPanelSanPham;
    private javax.swing.JButton btnSuaPanelSanPhamCT;
    private javax.swing.JButton btnTaoHoaDonPanelDatHang;
    private javax.swing.JButton btnTaoHoaDonPanelHoaDon;
    private javax.swing.JButton btnThanhToanPanelHoaDon;
    private javax.swing.JButton btnThemPanelKhachHang;
    private javax.swing.JButton btnThemPanelKhuyenMai;
    private javax.swing.JButton btnThemPanelNhanVien;
    private javax.swing.JButton btnThemPanelSanPham;
    private javax.swing.JButton btnThemPanelSanPhamCT;
    private javax.swing.JButton btnThoatPanelDoiMatKhau;
    private javax.swing.JButton btnThongKe;
    private javax.swing.JButton btnTimKiemKhachHangPanelHoaDon;
    private javax.swing.JButton btnTimKiemMaHDPanelLichSu;
    private javax.swing.JButton btnTimKiemPanelDatHang;
    private javax.swing.JButton btnTimKiemPanelSanPham;
    private javax.swing.JButton btnTimKiemPanelThongKe;
    private javax.swing.JButton btnTraHangPanelGiaoHang;
    private javax.swing.JButton btnXemAnPanelSanPhamCT;
    private javax.swing.JButton btnXemKhuyenMaiPanelKhuyenMai;
    private javax.swing.JButton btnXemNhanVienDaNghiPanelNV;
    private javax.swing.JButton btnXoaFormPanelKhachHang;
    private javax.swing.JButton btnXoaFormPanelKhuyenMai;
    private javax.swing.JButton btnXoaFormPanelNhanVien;
    private javax.swing.JButton btnXoaFormPanelSanPhamCT;
    private javax.swing.JButton btnXoaGioHangPanelHoaDon;
    private javax.swing.JButton btnXoaPanelSanPham;
    private javax.swing.JButton btnXuatFileExcelPanelThongKe;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.ButtonGroup buttonGroup2;
    private javax.swing.ButtonGroup buttonGroup3;
    private javax.swing.ButtonGroup buttonGroup4;
    private javax.swing.JComboBox<String> cbbChatLieuPanelSanPham;
    private javax.swing.JComboBox<String> cbbChucVuPanelNhanVien;
    private javax.swing.JComboBox<String> cbbHTTTPanelDatHang;
    private javax.swing.JComboBox<String> cbbHTTTPanelHoaDon;
    private javax.swing.JComboBox<String> cbbKichCoPanelSanPham;
    private javax.swing.JComboBox<String> cbbLoaiSPPanelSanPham;
    private javax.swing.JComboBox<String> cbbMauSacPanelSanPham;
    private javax.swing.JComboBox<String> cbbNamDoanhThuPanelThongKe;
    private javax.swing.JComboBox<String> cbbTimKiemSPPanelSanPham;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel36;
    private javax.swing.JLabel jLabel37;
    private javax.swing.JLabel jLabel38;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel40;
    private javax.swing.JLabel jLabel41;
    private javax.swing.JLabel jLabel42;
    private javax.swing.JLabel jLabel43;
    private javax.swing.JLabel jLabel44;
    private javax.swing.JLabel jLabel45;
    private javax.swing.JLabel jLabel46;
    private javax.swing.JLabel jLabel47;
    private javax.swing.JLabel jLabel48;
    private javax.swing.JLabel jLabel49;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel50;
    private javax.swing.JLabel jLabel51;
    private javax.swing.JLabel jLabel52;
    private javax.swing.JLabel jLabel53;
    private javax.swing.JLabel jLabel54;
    private javax.swing.JLabel jLabel55;
    private javax.swing.JLabel jLabel56;
    private javax.swing.JLabel jLabel57;
    private javax.swing.JLabel jLabel58;
    private javax.swing.JLabel jLabel59;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel60;
    private javax.swing.JLabel jLabel61;
    private javax.swing.JLabel jLabel62;
    private javax.swing.JLabel jLabel63;
    private javax.swing.JLabel jLabel64;
    private javax.swing.JLabel jLabel65;
    private javax.swing.JLabel jLabel66;
    private javax.swing.JLabel jLabel67;
    private javax.swing.JLabel jLabel68;
    private javax.swing.JLabel jLabel69;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel70;
    private javax.swing.JLabel jLabel71;
    private javax.swing.JLabel jLabel72;
    private javax.swing.JLabel jLabel73;
    private javax.swing.JLabel jLabel74;
    private javax.swing.JLabel jLabel75;
    private javax.swing.JLabel jLabel76;
    private javax.swing.JLabel jLabel77;
    private javax.swing.JLabel jLabel78;
    private javax.swing.JLabel jLabel79;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel80;
    private javax.swing.JLabel jLabel81;
    private javax.swing.JLabel jLabel82;
    private javax.swing.JLabel jLabel83;
    private javax.swing.JLabel jLabel84;
    private javax.swing.JLabel jLabel85;
    private javax.swing.JLabel jLabel87;
    private javax.swing.JLabel jLabel88;
    private javax.swing.JLabel jLabel89;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel15;
    private javax.swing.JPanel jPanel16;
    private javax.swing.JPanel jPanel17;
    private javax.swing.JPanel jPanel18;
    private javax.swing.JPanel jPanel19;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel20;
    private javax.swing.JPanel jPanel21;
    private javax.swing.JPanel jPanel22;
    private javax.swing.JPanel jPanel23;
    private javax.swing.JPanel jPanel24;
    private javax.swing.JPanel jPanel25;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane10;
    private javax.swing.JScrollPane jScrollPane11;
    private javax.swing.JScrollPane jScrollPane12;
    private javax.swing.JScrollPane jScrollPane13;
    private javax.swing.JScrollPane jScrollPane14;
    private javax.swing.JScrollPane jScrollPane15;
    private javax.swing.JScrollPane jScrollPane16;
    private javax.swing.JScrollPane jScrollPane17;
    private javax.swing.JScrollPane jScrollPane18;
    private javax.swing.JScrollPane jScrollPane19;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane20;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JScrollPane jScrollPane8;
    private javax.swing.JScrollPane jScrollPane9;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator10;
    private javax.swing.JSeparator jSeparator11;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JSeparator jSeparator4;
    private javax.swing.JSeparator jSeparator5;
    private javax.swing.JSeparator jSeparator6;
    private javax.swing.JSeparator jSeparator7;
    private javax.swing.JSeparator jSeparator8;
    private javax.swing.JSeparator jSeparator9;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTabbedPane jTabbedPane2;
    private javax.swing.JLabel lblHoTenNVPanelMain;
    private javax.swing.JLabel lblMaNVPanelLichSu;
    private javax.swing.JLabel lblMaNhanVienPanelMain;
    private javax.swing.JLabel lblNgayTaoPanelLichSu;
    private javax.swing.JLabel lblSDTPanelLichSu;
    private javax.swing.JLabel lblSumDate;
    private javax.swing.JLabel lblSumDonHang;
    private javax.swing.JLabel lblSumHangPanelThongKe;
    private javax.swing.JLabel lblSumHuyPanelThongKe;
    private javax.swing.JLabel lblSumMonth;
    private javax.swing.JLabel lblSumNamHPanelThongKe;
    private javax.swing.JLabel lblSumNamTCPanelThongKe;
    private javax.swing.JLabel lblSumNgayHPanelThongKe;
    private javax.swing.JLabel lblSumNgayTCPanelThongKe;
    private javax.swing.JLabel lblSumThangHPanelThongKe;
    private javax.swing.JLabel lblSumThangTCPanelThongKe;
    private javax.swing.JLabel lblSumYear;
    private javax.swing.JLabel lblTenKHPanelLichSu;
    private javax.swing.JLabel lblTenNVPanelLichSu;
    private javax.swing.JLabel lblTienThuaPanelDatHang;
    private javax.swing.JLabel lblTienThuaPanelHoaDon;
    private javax.swing.JLabel lblTongTienPanelDatHang;
    private javax.swing.JLabel lblTongTienPanelHoaDon;
    private javax.swing.JLabel lblTongTienPanelLichSu;
    private javax.swing.JPanel panelDoiMatKhau;
    private javax.swing.JPanel panelHoaDon;
    private javax.swing.JPanel panelKhachHang;
    private javax.swing.JPanel panelKhuyenMai;
    private javax.swing.JPanel panelLichSu;
    private javax.swing.JPanel panelMain;
    private javax.swing.JPanel panelNhanVien;
    private javax.swing.JPanel panelSanPham;
    private javax.swing.JPanel panelThongKe;
    private javax.swing.JPanel panelTrangChu;
    private javax.swing.JRadioButton rdoChatLieuPanelSanPham;
    private javax.swing.JRadioButton rdoKichCoPanelSanPham;
    private javax.swing.JRadioButton rdoLoaiSanPhamPanelSanPham;
    private javax.swing.JRadioButton rdoMauSacPanelSanPham;
    private javax.swing.JRadioButton rdoNamPanelNhanVien;
    private javax.swing.JRadioButton rdoNuPanelNhanVien;
    private javax.swing.JSpinner spnPhanTramKhuyenMaiPanelKhuyenMai;
    private javax.swing.JSpinner spnSoLuongPanelSanPham;
    private javax.swing.JTabbedPane tabbedPaneDatHang;
    private javax.swing.JTable tblChiTietSanPhamPanelSanPham;
    private javax.swing.JTable tblDoanhThuPanelThongKe;
    private javax.swing.JTable tblGioHangPanelHoaDon;
    private javax.swing.JTable tblHoaDonPanelHoaDon;
    private javax.swing.JTable tblHoaDonPanelLichSu;
    private javax.swing.JTable tblKhachHangPanelKhachHang;
    private javax.swing.JTable tblKhuyenMaiPanelKhuyenMai;
    private javax.swing.JTable tblNhanVienPanelNhanVien;
    private javax.swing.JTable tblSanPhamPanelHoaDon;
    private javax.swing.JTable tblSanPhamPanelKhuyenMai;
    private javax.swing.JTable tblSanPhamPanelLichSu;
    private javax.swing.JTable tblSanPhamPanelThongKe;
    private javax.swing.JTable tblThuocTinhPanelSanPham;
    private javax.swing.JTextField txtCaptchaPanelDoiMatKhau;
    private javax.swing.JTextArea txtDiaChiKhachHang;
    private javax.swing.JTextArea txtDiaChiPanelLichSu;
    private javax.swing.JTextArea txtDiaChiPanelNhanVien;
    private javax.swing.JTextField txtEmailPanelKhachHang;
    private javax.swing.JTextField txtEmailPanelNhanVien;
    private javax.swing.JTextArea txtGhiChuHoaDon;
    private javax.swing.JTextArea txtGhiChuPanelDatHang;
    private javax.swing.JTextField txtGiaPanelSanPham;
    private javax.swing.JTextField txtHoPanelKhachHang;
    private javax.swing.JTextField txtHoPanelNhanVien;
    private javax.swing.JTextField txtLoadCaptchaPanelDoiMatKhau;
    private javax.swing.JTextField txtMaHoaDonPanelLichSua;
    private javax.swing.JTextField txtMaKhachHangPanelKhachHang;
    private javax.swing.JTextField txtMaKhuyenMaiPanelKhuyenMai;
    private javax.swing.JTextField txtMaNhanVienPanelNhanVien;
    private javax.swing.JTextField txtMaSPPanelSanPham;
    private javax.swing.JTextField txtMaThuocTinhPanelSanPham;
    private javax.swing.JPasswordField txtMatKhauCuPanelDoiMatKhau;
    private javax.swing.JPasswordField txtMatKhauMoiPanelDoiMatKhau;
    private javax.swing.JPasswordField txtMatKhauPanelNhanVien;
    private javax.swing.JTextArea txtMoTaPanelLichSu;
    private javax.swing.JTextArea txtMoTaPanelSanPham;
    private com.toedter.calendar.JDateChooser txtNgayBatDauPanelKhuyenMai;
    private com.toedter.calendar.JDateChooser txtNgayBatDauPanelThongKe;
    private com.toedter.calendar.JDateChooser txtNgayKetThucPanelKhuyenMai;
    private com.toedter.calendar.JDateChooser txtNgayKetThucPanelThongKe;
    private com.toedter.calendar.JDateChooser txtNgaySinhNhanVienPanelNhanVien;
    private javax.swing.JTextField txtSDTPanelDatHang;
    private javax.swing.JTextField txtSDTPanelHoaDon;
    private javax.swing.JTextField txtSDTPanelKhachHang;
    private javax.swing.JTextField txtSDTPanelNhanVien;
    private javax.swing.JTextField txtTaiKhoanPanelDoiMatKhau;
    private javax.swing.JTextField txtTenChuongTrinhPanelKhuyenMai;
    private javax.swing.JTextField txtTenKhachHangPanelDatHang;
    private javax.swing.JTextField txtTenKhachHangPanelHoaDon;
    private javax.swing.JTextField txtTenPanelKhachHang;
    private javax.swing.JTextField txtTenPanelNhanVien;
    private javax.swing.JTextField txtTenSPPanelSanPham;
    private javax.swing.JTextField txtTenThuocTinhPanelSanPham;
    private javax.swing.JTextField txtTienKhachDuaPanelDatHang;
    private javax.swing.JTextField txtTienKhachDuaPanelHoaDon;
    private javax.swing.JTextField txtTimKiemKhachHangPanelKH;
    private javax.swing.JTextField txtTimKiemSPPanelSanPham;
    private javax.swing.JPasswordField txtXNMatKhauPanelDoiMatKhau;
    // End of variables declaration//GEN-END:variables
}
