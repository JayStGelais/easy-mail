package com.github.jaystgelais.easymail;

/**
 * Interface that provides access to source HTML content for input into transformation operations.
 *
 * @author jaystgelais
 */
public interface HtmlContentProvider {
    /**
     * Returns HTML content as a string.
     *
     * @return HTML content as a string.
     */
    String getHtmlMessageContent();
}
