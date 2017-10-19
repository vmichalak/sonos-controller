package com.vmichalak.sonoscontroller;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class CommandBuilderTest {

    @Test
    public void putParametersAndGetBody() {
        assertEquals("<a>b</a>", CommandBuilder.contentDirectory("test").put("a", "b").getBody());
        assertEquals("<ai>NO</ai><h>h</h>", CommandBuilder.device("test").put("ai", "NO").put("h", "h").getBody());
    }

}
