package com.github.jaystgelais.easymail;

import org.junit.Test;
import static junit.framework.Assert.assertEquals;

/**
 * Created by jgelais on 1/23/14.
 */
public final class StaticHtmlContentProviderTest {
    @Test
    public void testStringBasedStaticHtmlMessageContent() {
        HtmlContentProvider contentProvider = new StaticHtmlContentProvider();
        assertEquals("Unexpected content", null, contentProvider.getHtmlMessageContent());
    }
}
