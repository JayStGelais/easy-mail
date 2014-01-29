package com.github.jaystgelais.easymail;

import java.net.MalformedURLException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by jaystgelais on 1/27/14.
 */
public final class EmailMessageContent {


    private String html;
    private Map<String, EmbeddedImageReference> imageMap = new HashMap<String, EmbeddedImageReference>();
    private final HtmlContentProvider contentProvider;

    /**
     * Constructs a new EmailMessageContent.
     *
     * @param contentProvider Content Provided to use to retrieve embedded images.
     */
    public EmailMessageContent(final HtmlContentProvider contentProvider) {
        this.contentProvider = contentProvider;
    }

    /**
     * Returns the HTML message as a String.
     *
     * @return The HTML message as a String.
     */
    public String getHtmlMessage() {
        return html;
    }

    /**
     * Sets the HTML Message.
     *
     * @param html The HTML Message.
     */
    public void setHtmlMessage(final String html) {
        this.html = html;
    }

    /**
     * Returns the collection of images to be embedded in the HTML body.
     *
     * @return The collection of images to be embedded in the HTML body.
     */
    public Collection<EmbeddedImageReference> getEmbeddedImages() {
        return imageMap.values();
    }

    /**
     * Adds an image to this message content. Returns the EmbeddedImageReference generated to represent this image.
     *
     * @param relativeUrl Relative URL pointing to this image.
     * @return EmbeddedImageReference generated to represent this image.
     * @throws MalformedURLException if the relative URL cannot be converted into a full URL to locate the resource.
     */
    public EmbeddedImageReference addEmbeddedImage(final String relativeUrl) throws MalformedURLException {
        if (imageMap.keySet().contains(relativeUrl)) {
            return imageMap.get(relativeUrl);
        }

        EmbeddedImageReference embeddedImageReference = new EmbeddedImageReference(getNextContentId(),
                contentProvider.getImageDataSource(relativeUrl));
        imageMap.put(relativeUrl, embeddedImageReference);
        return embeddedImageReference;
    }


    private String getNextContentId() {
        return "img-" + (imageMap.size() + 1);
    }
}
