package com.github.jaystgelais.easymail;

/**
 * Created by jgelais on 1/23/14.
 */
public interface HtmlContentProvider {
    /**
     * Returns message content as a string of HTML.
     *
     * @return message content as a string of HTML.
     */
    String getHtmlMessageContent();
}
