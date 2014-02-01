package com.github.jaystgelais.easymail;

import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import java.io.IOException;
import java.util.Properties;

/**
 * Created by jaystgelais on 1/30/14.
 */
public final class Demo {
    public static final void main(String[] args) {
        if (args.length != 2) {
            throw new IllegalArgumentException("Usage: java com.github.jaystgelais.easymail.Demo gmailAddress gmailAppKey");
        }
        final String gmailAddress = args[0];
        final String gmailAppKey = args[1];
        Session mailSession = getSession(gmailAddress, gmailAppKey);
        HtmlContentProvider contentProvider = null;
        try {
            contentProvider = new URLHtmlContentProvider(Demo.class.getResource("/demo.html"));
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        EmailMessage message = null;
        try {
            message = new EmailMessage.Builder(gmailAddress,
                    "Hey check out easy-mail!", contentProvider)
                    .addTo(gmailAddress)
                    .build();
        } catch (HtmlTransformationException e) {
            e.printStackTrace();
            return;
        }

        try {
            message.send(mailSession);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    private static Session getSession(final String username, final String password) {
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                });

        return session;
    }
}
