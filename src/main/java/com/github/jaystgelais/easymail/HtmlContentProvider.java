package com.github.jaystgelais.easymail;

import javax.activation.DataSource;
import java.net.MalformedURLException;
import java.net.URL;

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
     * Provides a DataSoruce for accessing image content based on a relative URL from this HTML content.
     *
     * @param relativeUrl URL referencing the image.
     * @return DataSource providing image data.
     * @throws MalformedURLException if the relative URL cannot be converted into a full URL to locate the resource.
     */
    DataSource getImageDataSource(String relativeUrl) throws MalformedURLException;

    /**
     * Returns a URL for determining the location of relative resources linked form HTML document.
     *
     * @return URL to use for loading relative resources or {@code null} if a URL is not available.
     */
    URL getBaseURL();

}
