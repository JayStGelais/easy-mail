package com.github.jaystgelais.easymail;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.net.URL;

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
public final class StyleInliner {
    private StyleInliner() {

    }

    /**
     * Calculates the effective styles of each HTML element and produces HTML output stripped of CSS classes and
     * effective style inlined as 'style' attributes.
     *
     * @param contentProvider Input to style inlining transformation.
     * @return HTML as String as product of style inlining transformation.
     * @throws HtmlTransformationException If any errors occur preventing the transformation of the inputted HTML
     *                                     document.
     */
    public static String inlineStyle(final HtmlContentProvider contentProvider) throws HtmlTransformationException {
        DocumentSource docSource = null;
        try {
            docSource = newDocumentSource(contentProvider);
            Document doc = parseHtml(docSource);

            applyEffectiveStylesToStyleAttributes(doc, docSource.getURL());
            removeStyleElements(doc);
            removeClassAttributes(doc);

            return getHtmlAsString(doc);
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
        //Parse the input document
        DOMSource parser = new DefaultDOMSource(docSource);
        return parser.parse();
    }

    private static void applyEffectiveStylesToStyleAttributes(final Document doc, final URL relativeUrl) {
        //Create the CSS analyzer
        DOMAnalyzer da = new DOMAnalyzer(doc, relativeUrl);
        da.attributesToStyles(); //convert the HTML presentation attributes to inline styles
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
