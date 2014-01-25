package com.github.jaystgelais.easymail;

import org.junit.Test;

/**
 * Created by jaystgelais on 1/24/14.
 */
public final class StyleInlinerTest {
    private static final String TEST_HTML_STYLE_APPLIED_TO_DIV_TAGS
            = "<html><head><title>Sample Input</title><style>div {font-weight: bold;}</style></head>"
            + "<body><div id=\"thediv\"></div></body></html>";

    private static final String TEST_HTML_STYLE_APPLIED_TO_ID
            = "<html><head><title>Sample Input</title><style>#thediv {font-weight: bold;}</style></head>"
            + "<body><div id=\"thediv\"></div></body></html>";


    private static final String TEST_HTML_STYLE_APPLIED_TO_CLASS
            = "<html><head><title>Sample Input</title><style>.divStyle {font-weight: bold;}</style></head>"
            + "<body><div id=\"thediv\" class=\"divStyle\"></div></body></html>";

    private static final String TEST_HTML_STYLE_CONFLICT
            = "<html><head><title>Sample Input</title>"
            + "<style>.divStyle {font-weight: bold;} #thediv {font-weight: normal;}</style></head>"
            + "<body><div id=\"thediv\" class=\"divStyle\"></div></body></html>";

    @Test
    public void testStyleBlocksAreRemovedFromOutput() throws Exception {
        HtmlContentProvider contentProvider = new StaticHtmlContentProvider(TEST_HTML_STYLE_APPLIED_TO_DIV_TAGS);
        String htmlOutout = StyleInliner.inlineStyle(contentProvider);
        HtmlAssert.assertTagNotPresent(htmlOutout, "style");
    }

    @Test
    public void testStylesAppliesToElementByType() throws Exception {
        HtmlContentProvider contentProvider = new StaticHtmlContentProvider(TEST_HTML_STYLE_APPLIED_TO_DIV_TAGS);
        String htmlOutout = StyleInliner.inlineStyle(contentProvider);
        HtmlAssert.assertElementHasStyle(htmlOutout, "thediv", "font-weight: bold;");
    }

    @Test
    public void testStylesAppliesToElementById() throws Exception {
        HtmlContentProvider contentProvider = new StaticHtmlContentProvider(TEST_HTML_STYLE_APPLIED_TO_ID);
        String htmlOutout = StyleInliner.inlineStyle(contentProvider);
        HtmlAssert.assertElementHasStyle(htmlOutout, "thediv", "font-weight: bold;");
    }

    @Test
    public void testStylesAppliesToElementByClass() throws Exception {
        HtmlContentProvider contentProvider = new StaticHtmlContentProvider(TEST_HTML_STYLE_APPLIED_TO_CLASS);
        String htmlOutout = StyleInliner.inlineStyle(contentProvider);
        HtmlAssert.assertElementHasStyle(htmlOutout, "thediv", "font-weight: bold;");
    }

    @Test
    public void testStylesConflictHandledCorrectly() throws Exception {
        HtmlContentProvider contentProvider = new StaticHtmlContentProvider(TEST_HTML_STYLE_CONFLICT);
        String htmlOutout = StyleInliner.inlineStyle(contentProvider);
        HtmlAssert.assertElementHasStyle(htmlOutout, "thediv", "font-weight: normal;");
        HtmlAssert.assertElementDoesNotHaveStyle(htmlOutout, "thediv", "font-weight: bold;");
    }
}
