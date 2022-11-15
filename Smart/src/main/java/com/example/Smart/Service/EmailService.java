package com.example.Smart.Service;


import com.sun.mail.smtp.SMTPTransport;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.net.Socket;
import java.util.Properties;


@Service
public class EmailService {
    public boolean sendEmail(String subject, String message, String to, String from) throws MessagingException {
        boolean f = false;
        String username = "phppeerbits@gmail.com";
        String password = "zzcsnrcttsllaozy";
        String host = "smtp.gmail.com";

        Properties properties = System.getProperties();
        System.out.println("properties: " + properties);
        properties.put("mail.smtps.host", host);
        properties.put("mail.smtps.port", 587);
        properties.put("mail.smtps.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        properties.put("mail.smtps.ssl.enable", "true");
        properties.put("mail.smtps.starttls.enable", "true");
        properties.put("mail.smtps.auth", "true");
        properties.put("mail.smtps.socketFactory.fallback", "false");
        properties.put("mail.transport.protocol", "smtps");
        properties.put("user.name", username);
        properties.put("user.password", password);
        properties.put("mail.smtps.ssl.trust", host);


        Session session = Session.getInstance(properties, new javax.mail.Authenticator() {

            protected PasswordAuthentication getPasswordAuthentication() {

                return new PasswordAuthentication("phppeerbits@gmail.com", password);

            }

        });
        session.setProtocolForAddress("rfc822", "smtps");
        session.setDebug(true);


        MimeMessage mimeMessage = new MimeMessage(session);
        mimeMessage.setFrom(new InternetAddress(from));
        mimeMessage.addRecipient(javax.mail.Message.RecipientType.TO, new InternetAddress(to));
        mimeMessage.setSubject(subject);
        mimeMessage.setContent(message,"text/html");

        Socket socket = null;
        try {
            socket = new Socket(host, 587);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        SMTPTransport t = (SMTPTransport)session.getTransport("smtps");
        t.connect(socket);
        t.sendMessage(mimeMessage, mimeMessage.getAllRecipients());

        f = true;

        return f;
    }
}
