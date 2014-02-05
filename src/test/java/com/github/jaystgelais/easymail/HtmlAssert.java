package com.github.jaystgelais.easymail;

import org.fit.cssbox.io.DOMSource;
import org.fit.cssbox.io.DefaultDOMSource;
import org.fit.cssbox.io.DocumentSource;
import org.fit.cssbox.io.StreamDocumentSource;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.ByteArrayInputStream;
import java.io.IOException;

import static junit.framework.Assert.*;

/**
 * Utility class that contains assertion methods for inspecting teh content of HTML documents.
 */
public final class HtmlAssert {
    private HtmlAssert() {

    }

    public static void assertTagNotPresent(final String html, final String tag) throws IOException, SAXException {
        assertFalse(isTagPresent(html, tag));
    }

    public static void assertTagPresent(final String html, final String tag) throws IOException, SAXException {
        assertTrue(isTagPresent(html, tag));
    }

    public static void assertElementHasStyle(final String html, final String elementId, final String style)
            throws IOException, SAXException, XPathExpressionException {
        assertTrue("Style [" + style + "] missing on element [ID: " + elementId + "]\nhtml:\n" + html,
                isStylePresentOnElement(html, elementId, style));
    }

    public static void assertElementDoesNotHaveStyle(final String html, final String elementId, final String style)
            throws IOException, SAXException, XPathExpressionException {
        assertFalse(isStylePresentOnElement(html, elementId, style));
    }

    public static boolean isStylePresentOnElement(final String html, final String elementId, final String style)
            throws IOException, SAXException, XPathExpressionException {
        Document doc = parseHtml(html);
        Element element =  getElementById(doc, elementId);
        if (element == null) {
            return false;
        }
        return element.getAttribute("style").replaceAll("\\s+", "").toUpperCase()
                .contains(style.replaceAll("\\s+", "").toUpperCase());
    }

    private static boolean isTagPresent(final String html, final String tag) throws IOException, SAXException {
        Document doc = parseHtml(html);
        return doc.getElementsByTagName(tag).getLength() > 0;
    }

    private static Document parseHtml(final String html) throws IOException, SAXException {
        DocumentSource docSource = new StreamDocumentSource(
                new ByteArrayInputStream(html.getBytes()), null, "text/html");
        DOMSource parser = new DefaultDOMSource(docSource);
        return parser.parse();
    }

    private static Element getElementById(final Document doc, final String id) throws XPathExpressionException {
        String path = String.format("//*[@id = '%1$s' or @Id = '%1$s' or @ID = '%1$s' or @iD = '%1$s' ]", id);
        XPath xPath = XPathFactory.newInstance().newXPath();
        NodeList nodes = (NodeList) xPath.evaluate(path, doc, XPathConstants.NODESET);

        return (Element) nodes.item(0);
    }

}
