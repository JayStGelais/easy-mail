package com.github.jaystgelais.easymail;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.net.MalformedURLException;
import java.net.URL;

import org.fit.cssbox.css.CSSNorm;
import org.fit.cssbox.css.DOMAnalyzer;
import org.fit.cssbox.io.DOMSource;
import org.fit.cssbox.io.DefaultDOMSource;
import org.fit.cssbox.io.DocumentSource;
import org.fit.cssbox.io.StreamDocumentSource;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;

/**
 * Utility class that provides style inlining transformation for HTML documents.
 *
 * @author jaystgelais
 */
public final class HtmlProcessor {
    private HtmlProcessor() { }

    /**
     * Produces EmailMessage content based on a number of transformation on teh supplied HtmlContentProvider to
     * increase it's cross client rendering compatibility.
     *
     * Transformations include:
     * <ol>
     *     <li>Calculate effective styles of all elements and write them to their style attribute.</li>
     *     <li>Remove style declarations from head of document.</li>
     *     <li>Remove class attributes from all elements.</li>
     *     <li>Configure Embedded image references for all images pointing to relative URLs.</li>
     * </ol>
     *
     * @param contentProvider Input to sHTML processing transformations.
     * @return MessageContent containing processed HTML and embedded images.
     * @throws HtmlTransformationException If any errors occur preventing the transformation of the inputted HTML
     *                                     document.
     */
    public static EmailMessageContent process(final HtmlContentProvider contentProvider)
            throws HtmlTransformationException {
        DocumentSource docSource = null;
        EmailMessageContent emailMessageContent = new EmailMessageContent(contentProvider);
        try {
            docSource = newDocumentSource(contentProvider);
            Document doc = parseHtml(docSource);

            applyEffectiveStylesToStyleAttributes(doc, contentProvider.getBaseURL());
            removeStyleElements(doc);
            removeClassAttributes(doc);
            configureEmbeddedImages(doc, emailMessageContent);

            emailMessageContent.setHtmlMessage(getHtmlAsString(doc));
            return emailMessageContent;
        } catch (Exception e) {
            throw new HtmlTransformationException("Error occurred transforming HTML to use inline styles.", e);
        } finally {
            if (docSource != null) {
                try {
                    docSource.close();
                } catch (Exception e) {
                    throw new HtmlTransformationException("Error occurred transforming HTML to use inline styles.", e);
                }
            }
        }
    }

    private static void configureEmbeddedImages(final Document doc, final EmailMessageContent emailMessageContent)
            throws MalformedURLException {
        NodeList imgNodes = doc.getElementsByTagName("img");
        for (int x = 0; x < imgNodes.getLength(); x++) {
            Node imgNode = imgNodes.item(x);
            String url = getAttributeValue(imgNode, "src");
            if (isRelativeUrl(url)) {
                setAttributeValue(imgNode, "src", "cid:" + emailMessageContent.addEmbeddedImage(url).getContentId());
            }
        }
    }

    private static void setAttributeValue(final Node node, final String attributeName, final String value) {
        if (node.getAttributes() != null && node.getAttributes().getNamedItem(attributeName) != null) {
            node.getAttributes().getNamedItem(attributeName).setTextContent(value);
        }
    }

    private static String getAttributeValue(final Node node, final String attributeName) {
        if (node.getAttributes() == null || node.getAttributes().getNamedItem(attributeName) == null) {
            return null;
        }

        return node.getAttributes().getNamedItem(attributeName).getTextContent();
    }

    private static boolean isRelativeUrl(final String url) {
        return !url.toLowerCase().startsWith("http://");
    }

    private static void removeClassAttributes(final Node node) {
        if (node.getAttributes() != null && node.getAttributes().getNamedItem("class") != null) {
            node.getAttributes().removeNamedItem("class");
        }
        NodeList children = node.getChildNodes();
        for (int x = 0; x < children.getLength(); x++) {
            removeClassAttributes(children.item(x));
        }
    }

    private static String getHtmlAsString(final Document doc) throws TransformerException {
        javax.xml.transform.dom.DOMSource domSource = new javax.xml.transform.dom.DOMSource(doc);
        StringWriter writer = new StringWriter();
        StreamResult result = new StreamResult(writer);
        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer transformer = tf.newTransformer();
        transformer.transform(domSource, result);
        return writer.toString();
    }

    private static StreamDocumentSource newDocumentSource(final HtmlContentProvider contentProvider)
            throws IOException {
        return new StreamDocumentSource(new ByteArrayInputStream(contentProvider.getHtmlMessageContent().getBytes()),
                null, "text/html");
    }

    private static Document parseHtml(final DocumentSource docSource) throws SAXException, IOException {
        DOMSource parser = new DefaultDOMSource(docSource);
        return parser.parse();
    }

    private static void applyEffectiveStylesToStyleAttributes(final Document doc, final URL relativeUrl) {
        DOMAnalyzer da = new DOMAnalyzer(doc, relativeUrl);
        da.attributesToStyles(); //convert the HTML presentation attributes to inline styles
        da.addStyleSheet(null, CSSNorm.stdStyleSheet(), DOMAnalyzer.Origin.AGENT); //use the standard style sheet
        da.addStyleSheet(null, CSSNorm.userStyleSheet(), DOMAnalyzer.Origin.AGENT); //use the additional style sheet
        da.getStyleSheets(); //load the author style sheets
        da.stylesToDomInherited();
    }

    private static void removeStyleElements(final Document doc) {
        NodeList styleElements = doc.getElementsByTagName("style");
        for (int x = 0; x < styleElements.getLength(); x++) {
            styleElements.item(x).getParentNode().removeChild(styleElements.item(x));
        }
    }
}
