package com.csu.bakery.service;

import com.csu.bakery.config.MailUtil;
import com.csu.bakery.config.RandomUtil;
import com.sun.mail.util.MailSSLSocketFactory;
import jakarta.mail.MessagingException;
import jakarta.mail.NoSuchProviderException;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.MimeMessage;

import java.security.GeneralSecurityException;
import java.util.Properties;

public class EmailService {
    public String html(String recieverMail,String username,String code) {

        String html = "Hey " +username+" !<br/>"+
                "Verify this email is yours<br/>"+
                recieverMail+"<br/>"+
                "This email address was recently entered to verify your email address.<br/>" +
                "You can use this code to verify that this email belongs to you: <h3 style='color:red;'>"+code+"</h3><br/>";
        return html;
    }

    public String sendEmail(String email, String username) {
        MailUtil.setReceiverName(username);
        MailUtil.setReceiverMail(email);

        Properties props = new Properties();
        props.setProperty("mail.debug", "true");
        props.setProperty("mail.transport.protocol", "smtp");
        props.setProperty("mail.host", MailUtil.getEmailSMTPHost());
        props.setProperty("mail.auth", "true");

        try {
            MailSSLSocketFactory sf=new MailSSLSocketFactory();
            sf.setTrustAllHosts(true);
            props.put("mail.smtp.ssl.enable", "true");
            props.put("mail.smtp.ssl.socketFactory", sf);
        } catch (GeneralSecurityException e) {
            throw new RuntimeException(e);
        }

        Session session=Session.getInstance(props);
        session.setDebug(true);
        String code= RandomUtil.getRandom();
        String html= html(email,username,code);
        MimeMessage mimeMessage = MailUtil.creatMimeMessage(session,html);
        try {
            Transport transport=session.getTransport();
            transport.connect(MailUtil.getSenderMail(),MailUtil.getAuthCode());
            transport.sendMessage(mimeMessage,mimeMessage.getAllRecipients());
            transport.close();
            return code;
        } catch (NoSuchProviderException e) {
            throw new RuntimeException(e);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }
}
