package com.vmichalak.sonoscontroller;

import com.vmichalak.sonoscontroller.exception.SonosControllerException;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class CommandBuilderTest {

    @Test
    public void putParametersAndGetBody() {
        assertEquals("<a>b</a>", CommandBuilder.contentDirectory("test").put("a", "b").getBody());
        assertEquals("<ai>NO</ai><h>h</h>", CommandBuilder.device("test").put("ai", "NO").put("h", "h").getBody());
    }

    @Test
    public void alreadyEscaped() {
        String original = "x-sonosapi-radio:vy_wPy0RZBSA?sid=151&flags=8300&sn=3";
        String escaped  = "x-sonosapi-radio:vy_wPy0RZBSA?sid=151&amp;flags=8300&amp;sn=3";
        String expected = "<a>" + escaped + "</a>";

        assertEquals(expected, CommandBuilder.transport("test").put("a", original).getBody());
        assertEquals(expected, CommandBuilder.transport("test").put("a", escaped).getBody());
    }

    @Test
    public void otherAlreadyEscapedTest() {
        assertEquals("<a>aa&lt;bbcc</a>", CommandBuilder.transport("test").put("a", "aa&lt;bbcc").getBody());
        assertEquals("<a>aadze&gt;bbcc</a>", CommandBuilder.transport("test").put("a", "aadze&gt;bbcc").getBody());
        assertEquals("<a>sssaa&quot;bbcc</a>", CommandBuilder.transport("test").put("a", "sssaa&quot;bbcc").getBody());
        assertEquals("<a>sqsda&apos;bbqdc</a>", CommandBuilder.transport("test").put("a", "sqsda&apos;bbqdc").getBody());
    }

    @Test
    public void unescape() throws IOException, SonosControllerException {
        MockWebServer server = new MockWebServer();
        server.enqueue(new MockResponse().setBody("hello &amp;quot;world&amp;quot; !"));
        server.start(1400);
        String s = CommandBuilder.transport("test").put("a", "a").executeOn("127.0.0.1");
        assertEquals("hello \"world\" !", s);
        server.shutdown();
    }
}
