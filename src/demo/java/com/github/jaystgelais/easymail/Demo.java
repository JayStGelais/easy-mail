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
        Session mailSession = getSession(args[0], args[1]);
        HtmlContentProvider contentProvider = null;
        try {
            contentProvider = new URLHtmlContentProvider(Demo.class.getResource("/demo.html"));
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        EmailMessage message = null;
        try {
            message = new EmailMessage.Builder("JayStGelais@gmail.com",
                    "Hey check out easy-mail!", contentProvider)
                    .addTo("jstg@commercehub.com")
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
