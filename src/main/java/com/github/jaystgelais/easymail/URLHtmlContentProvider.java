package com.github.jaystgelais.easymail;

import com.google.common.base.Charsets;
import com.google.common.io.CharStreams;

import javax.activation.DataSource;
import javax.activation.URLDataSource;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;

/**
 * HtmlContentProvider implementation that fetches HTML from a given URL, such as a a fresource returned by
 * {@code Class.getResource(String pathToResource)}.
 *
 * @author jaystgelais
 */
public final class URLHtmlContentProvider implements HtmlContentProvider {

    private final URL resource;
    private final String htmlContent;

    /**
     * Constructs a URLHtmlContentProvider based on a supplied URL.
     *
     * @param resource URL specifying the HTML resource.
     * @throws IOException If any error occurs accessing the resource referenced by the supplied URL.
     */
    public URLHtmlContentProvider(final URL resource) throws IOException {
        this(resource, Charsets.UTF_8);
    }

    /**
     * Constructs a URLHtmlContentProvider based on a supplied URL and encoding.
     *
     * @param resource URL specifying the HTML resource.
     * @param encoding The encoding to use when reading HTML form the specified resource.
     * @throws IOException If any error occurs accessing the resource referenced by the supplied URL.
     */
    public URLHtmlContentProvider(final URL resource, final Charset encoding) throws IOException {
        this.resource = resource;
        htmlContent = readStringFromInputStream(resource.openStream(), encoding);
    }

    @Override
    public String getHtmlMessageContent() {
        return htmlContent;
    }

    @Override
    public DataSource getImageDataSource(final String relativeUrl) throws MalformedURLException {
        return new URLDataSource(new URL(resource, relativeUrl));
    }

    @Override
    public URL getBaseURL() {
        return resource;
    }

    private String readStringFromInputStream(final InputStream inputStream, final Charset encoding) throws IOException {
        String content = CharStreams.toString(new InputStreamReader(inputStream, encoding));
        inputStream.close();
        return content;
    }
}
