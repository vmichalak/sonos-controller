package com.vmichalak.sonoscontroller;

import com.vmichalak.sonoscontroller.exception.SonosControllerException;
import com.vmichalak.sonoscontroller.exception.UPnPSonosControllerException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

@RunWith(MockitoJUnitRunner.class)
public class SonosDeviceTest {

    @Test
    public void getPlayMode() throws IOException, SonosControllerException {
        SonosDevice sonosDevice = MockHelper.mockSonosDeviceSendCommand("<PlayMode>NORMAL</PlayMode>"); //TODO: Add True Data
        assertEquals(PlayMode.NORMAL, sonosDevice.getPlayMode());
    }

    @Test
    public void getVolume() throws IOException, SonosControllerException {
        SonosDevice sonosDevice = MockHelper.mockSonosDeviceSendCommand("<CurrentVolume>10</CurrentVolume>"); //TODO: Add True Data
        assertEquals(10, sonosDevice.getVolume());
    }

    @Test(expected = UPnPSonosControllerException.class)
    public void errorParsing() throws IOException, SonosControllerException {
        SonosDevice sonosDevice = new SonosDevice("127.0.0.1");

        sonosDevice.handleError("<?xml version=\"1.0\"?>\n" +
                " <s:Envelope\n" +
                "   xmlns:s=\"http://schemas.xmlsoap.org/soap/envelope/\"\n" +
                "   s:encodingStyle=\"http://schemas.xmlsoap.org/soap/encoding/\">\n" +
                "   <s:Body>\n" +
                "       <s:Fault>\n" +
                "           <faultcode>s:Client</faultcode>\n" +
                "           <faultstring>UPnPError</faultstring>\n" +
                "           <detail>\n" +
                "               <UPnPError xmlns=\"urn:schemas-upnp-org:control-1-0\">\n" +
                "                   <errorCode>403</errorCode>\n" +
                "               </UPnPError>\n" +
                "           </detail>\n" +
                "       </s:Fault>\n" +
                "   </s:Body>\n" +
                " </s:Envelope>");
    }
}
