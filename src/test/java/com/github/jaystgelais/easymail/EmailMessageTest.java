package com.github.jaystgelais.easymail;

import org.junit.Test;
import org.jvnet.mock_javamail.Mailbox;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import java.util.List;
import java.util.Properties;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;
import static junit.framework.Assert.fail;

/**
 * Created by jaystgelais on 1/25/14.
 */
public final class EmailMessageTest {

    public static final String EMAIL_SENDER = "sender@fakemail.net";
    public static final String EMAIL_SUBJECT = "This is a test email!";
    public static final String HTML
            = "<html><head><title>Sample Message</title><style>body {font-weight: bold;}</style></head>"
            + "<body>Hello World!</html>";
    public static final String EMAIL_RECIPIENT = "recipient@fakemail.com";
    public static final String EMAIL_CC = "keepmeintheloop@fakemail.com";
    public static final String EMAIL_BCC = "ispyyourmail@fakemail.com";
    public static final String MALFORMED_ADDRESS = "malformedaddress<";

    @Test
    public void testSendingOfMessage() throws Exception {
        Mailbox.clearAll();
        EmailMessage message
                = new EmailMessage.Builder(EMAIL_SENDER, EMAIL_SUBJECT, new StaticHtmlContentProvider(HTML))
                .addTo(EMAIL_RECIPIENT)
                .addCC(EMAIL_CC)
                .addBCC(EMAIL_BCC)
                .build();
        message.send(getMockSession());

        List<Message> inbox = Mailbox.get(EMAIL_RECIPIENT);
        assertTrue("Inbox size is actually: " + inbox.size(), inbox.size() == 1);
        assertTrue("From size is actually: " + inbox.get(0).getFrom().length, inbox.get(0).getFrom().length == 1);
        assertEquals(new InternetAddress(EMAIL_SENDER), inbox.get(0).getFrom()[0]);
        assertTrue("To size is actually: " + inbox.get(0).getRecipients(Message.RecipientType.TO).length,
                                             inbox.get(0).getRecipients(Message.RecipientType.TO).length == 1);
        assertEquals(new InternetAddress(EMAIL_RECIPIENT), inbox.get(0).getRecipients(Message.RecipientType.TO)[0]);
        assertTrue("CC size is actually: " + inbox.get(0).getRecipients(Message.RecipientType.CC).length,
                inbox.get(0).getRecipients(Message.RecipientType.CC).length == 1);
        assertEquals(new InternetAddress(EMAIL_CC), inbox.get(0).getRecipients(Message.RecipientType.CC)[0]);
        assertTrue("BCC size is actually: " + inbox.get(0).getRecipients(Message.RecipientType.BCC).length,
                inbox.get(0).getRecipients(Message.RecipientType.BCC).length == 1);
        assertEquals(new InternetAddress(EMAIL_BCC), inbox.get(0).getRecipients(Message.RecipientType.BCC)[0]);
        assertEquals(EMAIL_SUBJECT, inbox.get(0).getSubject());
        assertEquals(StyleInliner.inlineStyle(new StaticHtmlContentProvider(HTML)), inbox.get(0).getContent());
    }

    @Test
    public void testBuilderAddToExceptionHandling() {
        EmailMessage.Builder builder;
        try {
            builder = new EmailMessage.Builder(EMAIL_SENDER, EMAIL_SUBJECT, new StaticHtmlContentProvider(HTML));
        } catch (Exception e) {
            fail("Exception Occured in construction.");
            return;
        }

        boolean hasIllegalArgumentExceptionBeenCaught = false;
        try {
            builder.addTo(MALFORMED_ADDRESS);
            fail("Should have thrown IllegalArgumentException on malformed address.");
        } catch (IllegalArgumentException e) {
            hasIllegalArgumentExceptionBeenCaught = true;
        }
        assertTrue("Never Caught IllegalArgumentException", hasIllegalArgumentExceptionBeenCaught);
    }



    @Test
    public void testBuilderAddCCExceptionHandling() {
        EmailMessage.Builder builder;
        try {
            builder = new EmailMessage.Builder(EMAIL_SENDER, EMAIL_SUBJECT, new StaticHtmlContentProvider(HTML));
        } catch (Exception e) {
            fail("Exception Occured in construction.");
            return;
        }

        boolean hasIllegalArgumentExceptionBeenCaught = false;
        try {
            builder.addCC(MALFORMED_ADDRESS);
            fail("Should have thrown IllegalArgumentException on malformed address.");
        } catch (IllegalArgumentException e) {
            hasIllegalArgumentExceptionBeenCaught = true;
        }
        assertTrue("Never Caught IllegalArgumentException", hasIllegalArgumentExceptionBeenCaught);
    }



    @Test
    public void testBuilderAddBCCExceptionHandling() {
        EmailMessage.Builder builder;
        try {
            builder = new EmailMessage.Builder(EMAIL_SENDER, EMAIL_SUBJECT, new StaticHtmlContentProvider(HTML));
        } catch (Exception e) {
            fail("Exception Occured in construction.");
            return;
        }

        boolean hasIllegalArgumentExceptionBeenCaught = false;
        try {
            builder.addBCC(MALFORMED_ADDRESS);
            fail("Should have thrown IllegalArgumentException on malformed address.");
        } catch (IllegalArgumentException e) {
            hasIllegalArgumentExceptionBeenCaught = true;
        }
        assertTrue("Never Caught IllegalArgumentException", hasIllegalArgumentExceptionBeenCaught);
    }


    private Session getMockSession() {
        Properties properties = System.getProperties();
        properties.setProperty("mail.smtp.host", "testmail.localhost");
        return Session.getDefaultInstance(properties);
    }
}