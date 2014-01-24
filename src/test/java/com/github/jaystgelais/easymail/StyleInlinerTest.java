package com.github.jaystgelais.easymail;

import org.junit.Test;

import static junit.framework.Assert.*;

/**
 * Created by jaystgelais on 1/24/14.
 */
public final class StyleInlinerTest {
    private static final String TEST_HTML
            = "<html><head><title>Hello World!</title></head><body>Hello World!</body></html>";

    @Test
    public void testInlineStyle() throws HtmlTransformationException {
        HtmlContentProvider contentProvider = new StaticHtmlContentProvider(TEST_HTML);
        String htmlOutout = StyleInliner.inlineStyle(contentProvider);
        assertEquals(TEST_HTML, htmlOutout);
    }
}
