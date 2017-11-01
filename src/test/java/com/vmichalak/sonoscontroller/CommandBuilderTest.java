package com.vmichalak.sonoscontroller;

import org.junit.Test;

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

}
