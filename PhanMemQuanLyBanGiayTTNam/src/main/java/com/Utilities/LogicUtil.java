package com.Utilities;

import static com.Utilities.CreateQR.generateQRcode;

import com.Views.TrangChu;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Hashtable;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;

import jakarta.mail.Authenticator;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.bind.DatatypeConverter;

import org.apache.commons.codec.digest.DigestUtils;

public class LogicUtil {

    //Gửi mã về email
    public void codeVerification(String email, String random) {
        Properties prop = new Properties();
        prop.put("mail.smtp.host", "smtp.gmail.com");
        prop.put("mail.smtp.port", "587");
        prop.put("mail.smtp.auth", "true");
        prop.put("mail.smtp.starttls.enable", "true");

        Session session = Session.getInstance(prop,
                new Authenticator() {
                    @Override
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication("ytthanhsociu@gmail.com", "dangthanh181203");
                    }
                });
        try {

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress("Google"));
            message.setRecipients(
                    Message.RecipientType.TO,
                    InternetAddress.parse(email)
            );

            message.setSubject("Mã xác nhận mật khẩu [" + random + "]");
            message.setText("Mã xác nhận quên mật khẩu của Phần Mềm Quản Lý Bán Bán Giày TTNam ");
            Transport.send(message);
            System.out.println("Done");
        } catch (MessagingException e) {
            e.printStackTrace(System.out);
        }
    }

    // Mã hóa password
    public static String getMD5(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] messageDigest = md.digest(input.getBytes());
            String myHash = DatatypeConverter.printHexBinary(messageDigest).toUpperCase();
            return myHash;
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }


    public String taoMaHoa(String password) {
        String md5Hex = DigestUtils.md5Hex(password).toUpperCase();
        return md5Hex;
    }

    public void taoMaQR(String code) {

        //path where we want to get QR Code  
        String path = "D:\\PhanMemQuanLyBanQuanGiayTTNam\\QRCodeStaff\\\\" + code + ".png";
        //Encoding charset to be used  
        String charset = "UTF-8";
        Map<EncodeHintType, ErrorCorrectionLevel> hashMap = new HashMap<EncodeHintType, ErrorCorrectionLevel>();
        //generates QR code with Low level(L) error correction capability  
        hashMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);
        try {
            //invoking the user-defined method that creates the QR code
            generateQRcode(code, path, charset, hashMap, 200, 200);//increase or decrease height and width accodingly   
        } catch (WriterException | IOException ex) {
            Logger.getLogger(TrangChu.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
