package com.github.jaystgelais.easymail;

import org.junit.Test;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;
import static junit.framework.Assert.fail;

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
    public void testExceptionForUnsupportedOperations() throws Exception {
        boolean hasUnsupportedOperationExceptionBeenCaught = false;
        try {
            HtmlContentProvider contentProvider = new StaticHtmlContentProvider(TEST_HTML);
            contentProvider.getImageDataSource("./someUrl");
            fail("Should have thrown an excpetion");
        } catch (UnsupportedOperationException e) {
            hasUnsupportedOperationExceptionBeenCaught = true;
        }
        assertTrue("Failed to catch UnsupportedOperationException.", hasUnsupportedOperationExceptionBeenCaught);
    }
}
