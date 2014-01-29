package com.github.jaystgelais.easymail;

import javax.activation.DataSource;
import java.net.URL;

/**
 * Simple HtmlContentProvider implementation that will provide static HTML supplied from a String or Input Stream.
 *
 * @author jaystgelais
 */
public final class StaticHtmlContentProvider implements HtmlContentProvider {
    private final String htmlContent;

    /**
     * Creates a StaticHtmlContentProvider that will return the provided html String.
     *
     * @param html HTML String to provide.
     */
    public StaticHtmlContentProvider(final String html) {
        htmlContent = html;
    }

    @Override
    public String getHtmlMessageContent() {
        return htmlContent;
    }

    @Override
    public DataSource getImageDataSource(final String relativeUrl) {
        throw new UnsupportedOperationException("StaticHtmlContentProvider does not support embedding images "
                + "from relative URLs. All images myst be fully qualified URLs.");
    }

    @Override
    public URL getBaseURL() {
        return null;
    }
}
