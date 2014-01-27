package com.github.jaystgelais.easymail;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by jaystgelais on 1/27/14.
 */
public final class EmailMessageContent {


    private String html;
    private Map<String, EmbeddedImageReference> imageMap = new HashMap<String, EmbeddedImageReference>();

    /**
     * Returns the HTML message as a String.
     *
     * @return The HTML message as a String.
     */
    public String getHtmlMessage() {
        return html;
    }

    /**
     * Returns the collection of images to be embedded in the HTML body.
     *
     * @return The collection of images to be embedded in the HTML body.
     */
    public Iterable<EmbeddedImageReference> getEmbeddedImages() {
        return imageMap.values();
    }

    /**
     * Adds an image to this message content. Returns {@code true} if this image has not yet been added. (Returns
     * {@code false} if this image has already been embedded in this message content.)
     *
     * @param contentProvider Content provider that knows how to generate image data form the specified relative URL.
     * @param relativeUrl Relative URL pointing to this image.
     * @return {@code true} if this image has not yet been added. (Returns  {@code false} if this image has already
     *         been embedded in this message content.)
     */
    public boolean addEmbeddedImage(final HtmlContentProvider contentProvider, final String relativeUrl) {
        if (imageMap.keySet().contains(relativeUrl)) {
            return false;
        }

        imageMap.put(relativeUrl, new EmbeddedImageReference(getNextContentId(),
                contentProvider.getImageDataSource(relativeUrl)));
        return true;
    }


    private String getNextContentId() {
        return "img-" + (imageMap.size() + 1);
    }

}
