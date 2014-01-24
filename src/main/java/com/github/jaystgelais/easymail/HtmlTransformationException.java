package com.github.jaystgelais.easymail;

/**
 * Created by jgelais on 1/24/14.
 */
public class HtmlTransformationException extends Exception {
    /**
     * Exception representing a failure ot transform an html document.
     *
     * @param msg Exception message
     * @param e root cause
     */
    public HtmlTransformationException(final String msg, final Exception e) {
        super(msg, e);
    }
}
