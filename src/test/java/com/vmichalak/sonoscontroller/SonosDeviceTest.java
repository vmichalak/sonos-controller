package com.vmichalak.sonoscontroller;

import com.vmichalak.sonoscontroller.exception.SonosControllerException;
import com.vmichalak.sonoscontroller.exception.UPnPSonosControllerException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(MockitoJUnitRunner.class)
public class SonosDeviceTest {

    @Test
    public void getPlayMode() throws IOException, SonosControllerException {
        SonosDevice sonosDevice = MockHelper.mockSonosDeviceSendCommand(
                "<s:Envelope xmlns:s=\"http://schemas.xmlsoap.org/soap/envelope/\" " +
                        "s:encodingStyle=\"http://schemas.xmlsoap.org/soap/encoding/\"><s:Body>" +
                        "<u:GetTransportSettingsResponse xmlns:u=\"urn:schemas-upnp-org:service:AVTransport:1\">" +
                        "<PlayMode>NORMAL</PlayMode><RecQualityMode>NOT_IMPLEMENTED</RecQualityMode>" +
                        "</u:GetTransportSettingsResponse></s:Body></s:Envelope>\n");
        assertEquals(PlayMode.NORMAL, sonosDevice.getPlayMode());
    }

    @Test
    public void getVolume() throws IOException, SonosControllerException {
        SonosDevice sonosDevice = MockHelper.mockSonosDeviceSendCommand(
                "<s:Envelope xmlns:s=\"http://schemas.xmlsoap.org/soap/envelope/\" " +
                        "s:encodingStyle=\"http://schemas.xmlsoap.org/soap/encoding/\"><s:Body>" +
                        "<u:GetVolumeResponse xmlns:u=\"urn:schemas-upnp-org:service:RenderingControl:1\">" +
                        "<CurrentVolume>18</CurrentVolume></u:GetVolumeResponse></s:Body></s:Envelope>\n");
        assertEquals(18, sonosDevice.getVolume());
    }

    @Test
    public void getMuteFalse() throws IOException, SonosControllerException {
        SonosDevice sonosDevice = MockHelper.mockSonosDeviceSendCommand(
                "<s:Envelope xmlns:s=\"http://schemas.xmlsoap.org/soap/envelope/\" " +
                        "s:encodingStyle=\"http://schemas.xmlsoap.org/soap/encoding/\"><s:Body>" +
                        "<u:GetMuteResponse xmlns:u=\"urn:schemas-upnp-org:service:RenderingControl:1\">" +
                        "<CurrentMute>0</CurrentMute></u:GetMuteResponse></s:Body></s:Envelope>\n");
        assertEquals(false, sonosDevice.getMute());
    }

    @Test
    public void getMuteTrue() throws IOException, SonosControllerException {
        SonosDevice sonosDevice = MockHelper.mockSonosDeviceSendCommand(
                "<s:Envelope xmlns:s=\"http://schemas.xmlsoap.org/soap/envelope/\" " +
                        "s:encodingStyle=\"http://schemas.xmlsoap.org/soap/encoding/\"><s:Body>" +
                        "<u:GetMuteResponse xmlns:u=\"urn:schemas-upnp-org:service:RenderingControl:1\">" +
                        "<CurrentMute>1</CurrentMute></u:GetMuteResponse></s:Body></s:Envelope>\n");
        assertEquals(true, sonosDevice.getMute());
    }

    @Test
    public void getBass() throws IOException, SonosControllerException {
        SonosDevice sonosDevice = MockHelper.mockSonosDeviceSendCommand("" +
                "<s:Envelope xmlns:s=\"http://schemas.xmlsoap.org/soap/envelope/\" " +
                "s:encodingStyle=\"http://schemas.xmlsoap.org/soap/encoding/\"><s:Body>" +
                "<u:GetBassResponse xmlns:u=\"urn:schemas-upnp-org:service:RenderingControl:1\">" +
                "<CurrentBass>0</CurrentBass></u:GetBassResponse></s:Body></s:Envelope>\n");
        assertEquals(0, sonosDevice.getBass());
    }

    @Test(expected = IllegalArgumentException.class)
    public void setInvalidBassValue() throws IOException, SonosControllerException {
        SonosDevice sonosDevice = MockHelper.mockSonosDeviceSendCommand("");
        sonosDevice.setBass(11);
    }

    @Test(expected = IllegalArgumentException.class)
    public void setInvalidNegativeBassValue() throws IOException, SonosControllerException {
        SonosDevice sonosDevice = MockHelper.mockSonosDeviceSendCommand("");
        sonosDevice.setBass(-12);
    }

    @Test
    public void setValidBassValue() throws IOException, SonosControllerException {
        SonosDevice sonosDevice = MockHelper.mockSonosDeviceSendCommand("");
        sonosDevice.setBass(0);
    }

    @Test
    public void getLoudnessFalse() throws IOException, SonosControllerException {
        SonosDevice sonosDevice = MockHelper.mockSonosDeviceSendCommand(
                "<s:Envelope xmlns:s=\"http://schemas.xmlsoap.org/soap/envelope/\" " +
                        "s:encodingStyle=\"http://schemas.xmlsoap.org/soap/encoding/\"><s:Body>" +
                        "<u:GetLoudnessResponse xmlns:u=\"urn:schemas-upnp-org:service:RenderingControl:1\">" +
                        "<CurrentLoudness>0</CurrentLoudness></u:GetLoudnessResponse></s:Body></s:Envelope>\n");
        assertEquals(false, sonosDevice.getLoudness());
    }

    @Test
    public void getLoudnessTrue() throws IOException, SonosControllerException {
        SonosDevice sonosDevice = MockHelper.mockSonosDeviceSendCommand(
                "<s:Envelope xmlns:s=\"http://schemas.xmlsoap.org/soap/envelope/\" " +
                        "s:encodingStyle=\"http://schemas.xmlsoap.org/soap/encoding/\"><s:Body>" +
                        "<u:GetLoudnessResponse xmlns:u=\"urn:schemas-upnp-org:service:RenderingControl:1\">" +
                        "<CurrentLoudness>1</CurrentLoudness></u:GetLoudnessResponse></s:Body></s:Envelope>\n");
        assertEquals(true, sonosDevice.getLoudness());
    }

    @Test
    public void getLedStateFalse() throws IOException, SonosControllerException {
        String data = "<?xml version=\"1.0\"?>" +
                "<s:Envelope xmlns:s=\"http://schemas.xmlsoap.org/soap/envelope/\""+
                " s:encodingStyle=\"http://schemas.xmlsoap.org/soap/encoding/\">"+
                "<s:Body>"+
                "<u:GetLEDStateResponse "+
                "xmlns:u=\"urn:schemas-upnp-org:service:DeviceProperties:1\">"+
                "<CurrentLEDState>Off</CurrentLEDState>"+
                "<Unicode>data</Unicode>"+
                "</u:GetLEDStateResponse>"+
                "</s:Body>"+
                "</s:Envelope>";
        
        SonosDevice sonosDevice = MockHelper.mockSonosDeviceSendCommand(data);
        assertEquals(false, sonosDevice.getLedState());
    }

    @Test
    public void getLedStateTrue() throws IOException, SonosControllerException {
        String data = "<?xml version=\"1.0\"?>" +
                "<s:Envelope xmlns:s=\"http://schemas.xmlsoap.org/soap/envelope/\""+
                " s:encodingStyle=\"http://schemas.xmlsoap.org/soap/encoding/\">"+
                "<s:Body>"+
                "<u:GetLEDStateResponse "+
                "xmlns:u=\"urn:schemas-upnp-org:service:DeviceProperties:1\">"+
                "<CurrentLEDState>On</CurrentLEDState>"+
                "<Unicode>data</Unicode>"+
                "</u:GetLEDStateResponse>"+
                "</s:Body>"+
                "</s:Envelope>";

        SonosDevice sonosDevice = MockHelper.mockSonosDeviceSendCommand(data);
        assertEquals(true, sonosDevice.getLedState());
    }

    @Test
    public void getZoneGroupState() throws IOException, SonosControllerException {
        SonosDevice sonosDevice = MockHelper.mockSonosDeviceSendCommand(
                "<CurrentZoneGroupName>Salon</CurrentZoneGroupName>" +
                "<CurrentZoneGroupID>X</CurrentZoneGroupID>" +
                "<CurrentZonePlayerUUIDsInGroup>A,B,X</CurrentZonePlayerUUIDsInGroup>"); //TODO: Add True Data
        SonosZoneInfo zoneInfo = sonosDevice.getZoneGroupState();
        assertEquals("X", zoneInfo.getId());
        assertEquals("Salon", zoneInfo.getName());
        List<String> inGroup = new ArrayList<String>();
        inGroup.add("A");
        inGroup.add("B");
        inGroup.add("X");
        assertEquals(inGroup, zoneInfo.getZonePlayerUIDInGroup());
    }

    @Test
    public void getSpeakerInfo() throws IOException, SonosControllerException {
        Mockito.mock(SonosDevice.class);
        SonosDevice sonosDevice = Mockito.spy(new SonosDevice("127.0.0.1"));
        Mockito.doReturn("<?xml-stylesheet type=\"text/xsl\" href=\"/xml/review.xsl\"?><ZPSupportInfo><ZPInfo><ZoneName>" +
                "Bedroom</ZoneName><ZoneIcon>x-rincon-roomicon:den</ZoneIcon><Configuration>1</Configuration>" +
                "<LocalUID>RINCON_99999999AAAAAAAAA</LocalUID><SerialNumber>99-99-99-99-99-99:3</SerialNumber>" +
                "<SoftwareVersion>34.7-35162c</SoftwareVersion><SoftwareDate>2016-12-01 18:24:56.626597</SoftwareDate>" +
                "<SoftwareScm>288075</SoftwareScm><MinCompatibleVersion>33.0-00000</MinCompatibleVersion>" +
                "<LegacyCompatibleVersion>25.0-00000</LegacyCompatibleVersion>" +
                "<HardwareVersion>1.20.1.6-2</HardwareVersion><DspVersion>0.25.3</DspVersion>" +
                "<HwFlags>0x30</HwFlags><HwFeatures>0x0</HwFeatures><Variant>2</Variant>" +
                "<GeneralFlags>0x0</GeneralFlags><IPAddress>127.0.0.1</IPAddress>" +
                "<MACAddress>99:99:99:99:99:99</MACAddress>" +
                "<Copyright>Â© 2004-2015 Sonos, Inc. All Rights Reserved.</Copyright><ExtraInfo></ExtraInfo>" +
                "<HTAudioInCode>0</HTAudioInCode><IdxTrk></IdxTrk><MDP2Ver>5</MDP2Ver>" +
                "<MDP3Ver>2</MDP3Ver><RelBuild>1</RelBuild><WhitelistBuild>0x0</WhitelistBuild><ProdUnit>1</ProdUnit>" +
                "<FuseCfg>OK</FuseCfg><RevokeFuse>0x1</RevokeFuse><AuthFlags>0x0</AuthFlags>" +
                "<SwFeatures>0x0</SwFeatures><RegState>3</RegState><CustomerID>111111111</CustomerID>" +
                "</ZPInfo></ZPSupportInfo>").when(sonosDevice).downloadSpeakerInfo();

        SonosSpeakerInfo speakerInfo = sonosDevice.getSpeakerInfo();
        assertEquals("Bedroom",                                         speakerInfo.getZoneName());
        assertEquals("x-rincon-roomicon:den",                           speakerInfo.getZoneIcon());
        assertEquals("1",                                               speakerInfo.getConfiguration());
        assertEquals("RINCON_99999999AAAAAAAAA",                        speakerInfo.getLocalUID());
        assertEquals("99-99-99-99-99-99:3",                             speakerInfo.getSerialNumber());
        assertEquals("34.7-35162c",                                     speakerInfo.getSoftwareVersion());
        assertEquals("2016-12-01 18:24:56.626597",                      speakerInfo.getSoftwareDate());
        assertEquals("288075",                                          speakerInfo.getSoftwareScm());
        assertEquals("33.0-00000",                                      speakerInfo.getMinCompatibleVersion());
        assertEquals("25.0-00000",                                      speakerInfo.getLegacyCompatibleVersion());
        assertEquals("1.20.1.6-2",                                      speakerInfo.getHardwareVersion());
        assertEquals("0.25.3",                                          speakerInfo.getDspVersion());
        assertEquals("0x30",                                            speakerInfo.getHwFlags());
        assertEquals("0x0",                                             speakerInfo.getHwFeatures());
        assertEquals("2",                                               speakerInfo.getVariant());
        assertEquals("0x0",                                             speakerInfo.getGeneralFlags());
        assertEquals("127.0.0.1",                                       speakerInfo.getIpAddress());
        assertEquals("99:99:99:99:99:99",                               speakerInfo.getMacAddress());
        assertEquals("Â© 2004-2015 Sonos, Inc. All Rights Reserved.",   speakerInfo.getCopyright());
        assertEquals("",                                                speakerInfo.getExtraInfo());
        assertEquals("0",                                               speakerInfo.getHtAudioInCode());
        assertEquals("",                                                speakerInfo.getIdxTrk());
        assertEquals("5",                                               speakerInfo.getMdp2Ver());
        assertEquals("2",                                               speakerInfo.getMdp3Ver());
        assertEquals("1",                                               speakerInfo.getRelBuild());
        assertEquals("0x0",                                             speakerInfo.getWhitelistBuild());
        assertEquals("1",                                               speakerInfo.getProdUnit());
        assertEquals("OK",                                              speakerInfo.getFuseCfg());
        assertEquals("0x1",                                             speakerInfo.getRevokeFuse());
        assertEquals("0x0",                                             speakerInfo.getAuthFlags());
        assertEquals("0x0",                                             speakerInfo.getSwFeatures());
        assertEquals("3",                                               speakerInfo.getRegState());
        assertEquals("111111111",                                       speakerInfo.getCustomerID());
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

    @Test
    public void noErrorParsing() throws IOException, SonosControllerException {
        SonosDevice sonosDevice = new SonosDevice("127.0.0.1");
        sonosDevice.handleError("");
    }

    @Test
    public void errorParsingErrorCode() throws SonosControllerException {
        SonosDevice sonosDevice = new SonosDevice("127.0.0.1");

        try {
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
        } catch (UPnPSonosControllerException e) {
            assertEquals(403, e.getErrorCode());
        }
    }

    @Test
    public void errorParsingResponse() throws SonosControllerException {
        SonosDevice sonosDevice = new SonosDevice("127.0.0.1");

        String response = "<?xml version=\"1.0\"?>\n" +
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
                " </s:Envelope>";

        try {
            sonosDevice.handleError(response);
        } catch (UPnPSonosControllerException e) {
            assertEquals(response, e.getResponse());
        }
    }

    @Test
    public void toStringTest() {
        SonosDevice sonosDevice = new SonosDevice("127.0.0.1");
        assertEquals("SonosDevice{ip='127.0.0.1'}", sonosDevice.toString());
    }
}
