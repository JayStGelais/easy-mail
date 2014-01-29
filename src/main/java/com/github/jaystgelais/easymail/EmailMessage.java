package com.github.jaystgelais.easymail;

import javax.activation.DataHandler;
import javax.mail.Address;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Immutable wrapper around {@link javax.mail.Message} that provides transformation of HTML content for cross
 * client compatibility.
 *
 * @author jaystgelais
 */
public final class EmailMessage {
    private final Address from;
    private final Set<Address> to;
    private final Set<Address> cc;
    private final Set<Address> bcc;

    private final String subject;
    private final String messageBody;
    private final Collection<EmbeddedImageReference> imageDataSources;

    private EmailMessage(final Builder builder) {
        from = builder.from;
        subject = builder.subject;
        messageBody = builder.messageBody.getHtmlMessage();
        to = Collections.unmodifiableSet(builder.to);
        cc = Collections.unmodifiableSet(builder.cc);
        bcc = Collections.unmodifiableSet(builder.bcc);
        imageDataSources = Collections.unmodifiableCollection(builder.messageBody.getEmbeddedImages());
    }

    /**
     * The email address representing this message's sender.
     *
     * @return The email address representing this message's sender.
     */
    public Address getFrom() {
        return from;
    }

    /**
     * A collection of unique primary (to:) recipients.
     *
     * @return Set containing the email address of primary (to:) recipients.
     */
    public Set<Address> getTo() {
        return to;
    }

    /**
     * A collection of unique carbon copy (cc:) recipients.
     *
     * @return Set containing the email address of carbon copy (cc:) recipients.
     */
    public Set<Address> getCc() {
        return cc;
    }

    /**
     * A collection of unique blind carbon copy (cc:) recipients.
     *
     * @return Set containing the email address of blind carbon copy (cc:) recipients.
     */
    public Set<Address> getBcc() {
        return bcc;
    }

    /**
     * The subject line of this email message.
     *
     * @return The subject line of this email message.
     */
    public String getSubject() {
        return subject;
    }

    /**
     * The HTML document that will be sent as the message body of this email.
     *
     * @return String representing the HTML document that will be sent as the message body of this email.
     */
    public String getMessageBody() {
        return messageBody;
    }

    /**
     * Send this email message.
     *
     * @param session Java Mail session providing connectivity to the outgoing mal server.
     * @throws MessagingException If any errors occur that prevent the sending of this message.
     */
    public void send(final Session session) throws MessagingException {
        Message message = new MimeMessage(session);
        message.setFrom(from);
        message.setRecipients(Message.RecipientType.TO, to.toArray(new Address[0]));
        message.setRecipients(Message.RecipientType.CC, cc.toArray(new Address[0]));
        message.setRecipients(Message.RecipientType.BCC, bcc.toArray(new Address[0]));
        message.setSubject(subject);
        if (imageDataSources.isEmpty()) {
            message.setContent(messageBody, "text/html");
        } else {
            Multipart multipart = new MimeMultipart("related");

            BodyPart htmlPart = new MimeBodyPart();
            htmlPart.setContent(messageBody, "text/html");
            multipart.addBodyPart(htmlPart);

            for (EmbeddedImageReference imageReference : imageDataSources) {
                BodyPart imgPart = new MimeBodyPart();
                imgPart.setDataHandler(new DataHandler(imageReference.getImageDataSource()));
                imgPart.setHeader("Content-ID", imageReference.getContentId());
                multipart.addBodyPart(imgPart);
            }

            message.setContent(multipart);
        }

        Transport.send(message);
    }

    /**
     * Builder for instantiating new EmailMessages.
     */
    public static final class Builder {
        private final Address from;
        private final Set<Address> to;
        private final Set<Address> cc;
        private final Set<Address> bcc;
        private final String subject;
        private final EmailMessageContent messageBody;

        /**
         * Creates a new EmailMessage Builder.
         *
         * @param from email address for the sender of this message
         * @param subject Email Subject
         * @param contentProvider content provider supplying untransformed HTML input
         *
         * @throws HtmlTransformationException If an error is encountered transforming Html content for inclusion in
         *                                     this email.
         * @throws IllegalArgumentException If the email address supplied for {@code from} is not a valid
         *                                            email address.
         */
        public Builder(final String from, final String subject,
                       final HtmlContentProvider contentProvider) throws HtmlTransformationException {
            try {
                this.from = new InternetAddress(from);
            } catch (AddressException e) {
                throw new IllegalArgumentException("Unable to parse [" + from + "] as a valid email address", e);
            }
            this.subject = subject;
            this.messageBody = HtmlProcessor.process(contentProvider);
            this.to = new HashSet<Address>();
            this.cc = new HashSet<Address>();
            this.bcc = new HashSet<Address>();
        }

        /**
         * creates a new EmailMessage Builder.
         *
         * @param from email address for the sender of this message
         * @param subject Email Subject
         * @param htmlContent HTML content for message body
         *
         * @throws HtmlTransformationException If an error is encountered transforming Html content for inclusion in
         *                                     this email.
         * @throws IllegalArgumentException If the email address supplied for {@code from} is not a valid
         *                                            email address.
         */
        public Builder(final String from, final String subject,
                       final String htmlContent) throws HtmlTransformationException {
            this(from, subject, new StaticHtmlContentProvider(htmlContent));
        }

        /**
         * Adds email address to the set of primary (to:) recipients.
         *
         * @param address Email address to add to list of primary (to:) recipients.
         * @return This Builder
         * @throws IllegalArgumentException If address cannot be parsed into a valid email address.
         */
        public Builder addTo(final String address) {
            try {
                to.add(new InternetAddress(address));
            } catch (AddressException e) {
                throw new IllegalArgumentException("Unable to parse [" + address + "] as a valid email address", e);
            }
            return this;
        }

        /**
         * Adds email address to the set of carbon copy (cc:) recipients.
         *
         * @param address Email address to add to list of carbon copy (cc:) recipients.
         * @return This Builder
         * @throws IllegalArgumentException If address cannot be parsed into a valid email address.
         */
        public Builder addCC(final String address)  {
            try {
                cc.add(new InternetAddress(address));
            } catch (AddressException e) {
                throw new IllegalArgumentException("Unable to parse [" + address + "] as a valid email address", e);
            }
            return this;
        }

        /**
         * Adds email address to the set of blind carbon copy (bcc:) recipients.
         *
         * @param address Email address to add to list of blind carbon copy (bcc:) recipients.
         * @return This Builder
         * @throws IllegalArgumentException If address cannot be parsed into a valid email address.
         */
        public Builder addBCC(final String address) {
            try {
                bcc.add(new InternetAddress(address));
            } catch (AddressException e) {
                throw new IllegalArgumentException("Unable to parse [" + address + "] as a valid email address", e);
            }
            return this;
        }

        /**
         * Returns an immutable EmailMessage initialized from data supplied to this builder.
         *
         * @return An immutable EmailMessage initialized from data supplied to this builder.
         */
        public EmailMessage build() {
            if (to.isEmpty() && cc.isEmpty() && bcc.isEmpty()) {
                throw new IllegalStateException("At least one of TO, CC and BCC must be a non-empty set.");
            }
            return new EmailMessage(this);
        }
    }

}
