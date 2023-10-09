package com.Utilities;


import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

public class SendEmail {

    public void guiHoaDon(String email, String duongDan) {

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

            message.setSubject("Hoá đơn của bạn tại cửa hàng Bán Giày TTNam");
            Multipart multipart = new MimeMultipart();
            MimeBodyPart body = new MimeBodyPart();
            body.setText("Cảm ơn quý khách đã ủng hộ cửa hàng!");
            body.setText("Chúng tôi mong quý khách có trải nghiệm tốt nhất về dịch vụ và sản phẩm");
            MimeBodyPart att = new MimeBodyPart();
            att.attachFile(new File(duongDan));
            multipart.addBodyPart(body);
            multipart.addBodyPart(att);
            message.setContent(multipart);
            Transport.send(message);
            System.out.println("Done");
        } catch (MessagingException e) {
            e.printStackTrace(System.out);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

