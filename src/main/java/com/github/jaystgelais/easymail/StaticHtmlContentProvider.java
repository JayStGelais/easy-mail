package com.github.jaystgelais.easymail;

import com.google.common.base.Charsets;
import com.google.common.io.CharStreams;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

/**
 * Simple HtmlContentProvider implementation that will provide static HTML supplied from a String, Input Stream or File.
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

    /**
     * Creates a StaticHtmlContentProvider that will return the provided html String.
     *
     * @param inputStream InputStream providing input HTML.
     * @param encoding Sting encoding to use.
     * @throws IOException If an error occurs reading inputStream
     */
    public StaticHtmlContentProvider(final InputStream inputStream, final Charset encoding) throws IOException {
        htmlContent = readStringFromInputStream(inputStream, encoding);
    }

    /**
     * Creates a StaticHtmlContentProvider that will return the provided html String.
     *
     * @param inputStream InputStream providing input HTML.
     * @throws IOException If an error occurs reading inputStream
     */
    public StaticHtmlContentProvider(final InputStream inputStream) throws IOException {
        this(inputStream, Charsets.UTF_8);
    }

    private String readStringFromInputStream(final InputStream inputStream, final Charset encoding) throws IOException {
        String content = CharStreams.toString(new InputStreamReader(inputStream, encoding));
        inputStream.close();
        return content;
    }

    @Override
    public String getHtmlMessageContent() {
        return htmlContent;
    }
}
