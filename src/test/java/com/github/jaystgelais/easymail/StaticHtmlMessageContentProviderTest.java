package com.github.jaystgelais.easymail;

import org.junit.Test;
import static junit.framework.Assert.assertEquals;

/**
 * Created by jgelais on 1/23/14.
 */
public final class StaticHtmlMessageContentProviderTest {
    @Test
    public void testStringBasedStaticHtmlMessageContent() {
        HtmlMessageContentProvider contentProvider = new StaticHtmlMessageContentProvider();
        assertEquals("Unexpected content", null, contentProvider.getHtmlMessageContent());
    }
}
