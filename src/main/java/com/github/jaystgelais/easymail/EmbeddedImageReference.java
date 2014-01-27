package com.github.jaystgelais.easymail;

import javax.activation.DataSource;

/**
 * Immutable object that provides access to images to be embedded in HTML email messages.
 *
 * @author jaystgelais
 */
public final class EmbeddedImageReference {

    private final String contentId;
    private final DataSource dataSource;

    /**
     * Constructs a new EmbeddedImageReference with the specified content ID and data source.
     *
     * @param contentId The reference ID used to refer to the image.
     * @param dataSource Data soruce provided the image content.
     */
    public EmbeddedImageReference(final String contentId, final DataSource dataSource) {
        this.contentId = contentId;
        this.dataSource = dataSource;
    }

    /**
     * Returns the contentId assigned to this embedded image.
     *
     * @return ContentId assigned to this embedded image.
     */
    public String getContentId() {
        return contentId;
    }

    /**
     * Returns a data source providing access to the contents of this embedded image.
     *
     * @return Datasource Data source providing access to the contents of this embedded image.
     */
    public DataSource getImageDataSource() {
        return dataSource;
    }
}
