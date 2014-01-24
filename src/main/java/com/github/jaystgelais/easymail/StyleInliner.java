package com.github.jaystgelais.easymail;

import java.io.ByteArrayInputStream;
import java.io.StringWriter;

import org.fit.cssbox.css.DOMAnalyzer;
import org.fit.cssbox.io.DOMSource;
import org.fit.cssbox.io.DefaultDOMSource;
import org.fit.cssbox.io.DocumentSource;
import org.fit.cssbox.io.StreamDocumentSource;
import org.w3c.dom.Document;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;

/**
 * Created by jaystgelais on 1/23/14.
 */
public final class StyleInliner {
    private StyleInliner() {

    }

    /**
     * Calculates teh effective styles of each HTML element and produces HTML output stripped of CSS classes and
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
            //Open the network connection
            docSource = new StreamDocumentSource(
                    new ByteArrayInputStream(contentProvider.getHtmlMessageContent().getBytes()), null, "text/html");

            //Parse the input document
            DOMSource parser = new DefaultDOMSource(docSource);
            Document doc = parser.parse();

            //Create the CSS analyzer
            DOMAnalyzer da = new DOMAnalyzer(doc, docSource.getURL());
            da.attributesToStyles(); //convert the HTML presentation attributes to inline styles
            da.getStyleSheets(); //load the author style sheets
            da.stylesToDomInherited();

            javax.xml.transform.dom.DOMSource domSource = new javax.xml.transform.dom.DOMSource(doc);
            StringWriter writer = new StringWriter();
            StreamResult result = new StreamResult(writer);
            TransformerFactory tf = TransformerFactory.newInstance();
            Transformer transformer = tf.newTransformer();
            transformer.transform(domSource, result);
            return writer.toString();
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
}
