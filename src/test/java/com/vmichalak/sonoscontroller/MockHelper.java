package com.vmichalak.sonoscontroller;

import com.vmichalak.sonoscontroller.exception.SonosControllerException;
import org.mockito.Mockito;

import java.io.IOException;

import static org.mockito.Matchers.anyString;

public class MockHelper {
    public static SonosDevice mockSonosDeviceSendCommand(String output) throws IOException, SonosControllerException {
        Mockito.mock(SonosDevice.class);
        SonosDevice sonosDevice = Mockito.spy(new SonosDevice("127.0.0.1"));
        Mockito.doReturn(output).when(sonosDevice).sendCommand(anyString(), anyString(), anyString(), anyString());
        return sonosDevice;
    }
}
