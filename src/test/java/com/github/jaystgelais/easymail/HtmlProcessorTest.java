package com.github.jaystgelais.easymail;

import org.junit.Test;

import javax.activation.DataSource;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;
import static junit.framework.Assert.fail;

/**
 * Created by jaystgelais on 1/24/14.
 */
public final class HtmlProcessorTest {
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
        String htmlOutout = HtmlProcessor.process(contentProvider).getHtmlMessage();
        HtmlAssert.assertTagNotPresent(htmlOutout, "style");
    }

    @Test
    public void testStylesAppliesToElementByType() throws Exception {
        HtmlContentProvider contentProvider = new StaticHtmlContentProvider(TEST_HTML_STYLE_APPLIED_TO_DIV_TAGS);
        String htmlOutout = HtmlProcessor.process(contentProvider).getHtmlMessage();
        HtmlAssert.assertElementHasStyle(htmlOutout, "thediv", "font-weight: bold;");
    }

    @Test
    public void testStylesAppliesToElementById() throws Exception {
        HtmlContentProvider contentProvider = new StaticHtmlContentProvider(TEST_HTML_STYLE_APPLIED_TO_ID);
        String htmlOutout = HtmlProcessor.process(contentProvider).getHtmlMessage();
        HtmlAssert.assertElementHasStyle(htmlOutout, "thediv", "font-weight: bold;");
    }

    @Test
    public void testStylesAppliesToElementByClass() throws Exception {
        HtmlContentProvider contentProvider = new StaticHtmlContentProvider(TEST_HTML_STYLE_APPLIED_TO_CLASS);
        String htmlOutout = HtmlProcessor.process(contentProvider).getHtmlMessage();
        HtmlAssert.assertElementHasStyle(htmlOutout, "thediv", "font-weight: bold;");
    }

    @Test
    public void testStylesConflictHandledCorrectly() throws Exception {
        HtmlContentProvider contentProvider = new StaticHtmlContentProvider(TEST_HTML_STYLE_CONFLICT);
        String htmlOutout = HtmlProcessor.process(contentProvider).getHtmlMessage();
        HtmlAssert.assertElementHasStyle(htmlOutout, "thediv", "font-weight: normal;");
        HtmlAssert.assertElementDoesNotHaveStyle(htmlOutout, "thediv", "font-weight: bold;");
    }

    @Test
    public void testEmbeddingOfImage() throws Exception {
        HtmlContentProvider contentProvider
                = new URLHtmlContentProvider(getClass().getResource("./EmbeddedImageTest.html"));
        EmailMessageContent messageContent = HtmlProcessor.process(contentProvider);
        assertEquals(1, messageContent.getEmbeddedImages().size());
    }

    @Test
    public void testExceptionHandling() {
        boolean hasHtmlTransformationExceptionBeenCaught = false;
        try {
            HtmlProcessor.process(new HtmlContentProvider() {
                @Override
                public String getHtmlMessageContent() {
                    throw new RuntimeException();
                }

                @Override
                public DataSource getImageDataSource(final String relativeUrl) {
                    return null;
                }
            });
            fail("Should have thrown an excpetion");
        } catch (HtmlTransformationException e) {
            hasHtmlTransformationExceptionBeenCaught = true;
        } catch (Exception e) {
            fail("Caught the Wrong Exception:" + e.getClass().toString());
        }
        assertTrue("Failed to catch HtmlTransformationException.", hasHtmlTransformationExceptionBeenCaught);
    }
}
