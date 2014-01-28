package com.github.jaystgelais.easymail;

import org.junit.Test;

import static junit.framework.Assert.assertNotNull;

/**
 * Created by jaystgelais on 1/28/14.
 */
public final class URLHtmlContentProviderTest {
    @Test
    public void testLoadOfHtmlFromURL() throws Exception {
        HtmlContentProvider contentProvider
                = new URLHtmlContentProvider(getClass().getResource("./EmbeddedImageTest.html"));
        HtmlAssert.assertTagPresent(contentProvider.getHtmlMessageContent(), "html");
    }


    @Test
    public void testLookupOfRelativeImages() throws Exception {
        HtmlContentProvider contentProvider
                = new URLHtmlContentProvider(getClass().getResource("./EmbeddedImageTest.html"));
        assertNotNull(contentProvider.getImageDataSource("./sampleImage.jpg"));
    }
}
