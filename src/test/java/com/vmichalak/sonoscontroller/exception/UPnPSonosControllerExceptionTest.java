package com.vmichalak.sonoscontroller.exception;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class UPnPSonosControllerExceptionTest {

    @Test
    public void dataGetter() {
        try {
            throw new UPnPSonosControllerException("message", 403, "description", "");
        } catch (UPnPSonosControllerException e) {
            assertEquals("message", e.getMessage());
            assertEquals(403, e.getErrorCode());
            assertEquals("description", e.getErrorDescription());
            assertEquals("", e.getResponse());
        }
    }

}
