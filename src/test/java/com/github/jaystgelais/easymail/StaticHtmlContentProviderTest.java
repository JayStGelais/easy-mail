package com.github.jaystgelais.easymail;

import com.google.common.base.Charsets;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import static junit.framework.Assert.assertEquals;

/**
 * Created by jgelais on 1/23/14.
 */
public final class StaticHtmlContentProviderTest {
    private static final String TEST_HTML
            = "<html><head><title>Hello World!</title></head><body>Hello World!</body></html>";
    @Test
    public void testStringBasedStaticHtmlMessageContent() {
        HtmlContentProvider contentProvider = new StaticHtmlContentProvider(TEST_HTML);
        assertEquals("Content from String Based StaticHtmlContentProvider did not match expected value.",
                TEST_HTML, contentProvider.getHtmlMessageContent());
    }

    @Test
    public void testInputStreamBasedStaticHtmlMessageContent() throws IOException {
        HtmlContentProvider contentProvider
                = new StaticHtmlContentProvider(new ByteArrayInputStream(TEST_HTML.getBytes(Charsets.UTF_8)));
        assertEquals("Content from String Based StaticHtmlContentProvider did not match expected value.",
                TEST_HTML, contentProvider.getHtmlMessageContent());
    }
}
