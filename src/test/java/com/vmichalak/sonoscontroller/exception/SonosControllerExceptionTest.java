package com.vmichalak.sonoscontroller.exception;

import org.junit.Test;

public class SonosControllerExceptionTest {

    @Test(expected = SonosControllerException.class)
    public void SonosControllerException() throws SonosControllerException {
        throw new SonosControllerException("Test");
    }

}
