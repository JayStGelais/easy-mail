package com.github.jaystgelais.easymail;

import javax.activation.DataSource;

/**
 * Interface that provides access to source HTML content for input into transformation operations.
 *
 * @author jaystgelais
 */
public interface HtmlContentProvider {
    /**
     * Returns HTML content as a string.
     *
     * @return HTML content as a string.
     */
    String getHtmlMessageContent();

    /**
     * Provides a DataSoruce for accessing eimage content based on a relative URL from this HTML content.
     *
     * @param relativeUrl URL referencing the image.
     * @return DataSource providing image data.
     */
    DataSource getImageDataSource(String relativeUrl);

}
