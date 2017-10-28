package com.vmichalak.sonoscontroller;

import com.vmichalak.sonoscontroller.exception.SonosControllerException;
import com.vmichalak.sonoscontroller.exception.UPnPSonosControllerException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(PowerMockRunner.class)
@PrepareForTest({CommandBuilder.class, SonosDevice.class})
public class SonosDeviceTest {

    @Test
    public void getPlayMode() throws Exception {
        MockHelper.mockCommandBuilder("<s:Envelope xmlns:s=\"http://schemas.xmlsoap.org/soap/envelope/\" " +
                "s:encodingStyle=\"http://schemas.xmlsoap.org/soap/encoding/\"><s:Body>" +
                "<u:GetTransportSettingsResponse xmlns:u=\"urn:schemas-upnp-org:service:AVTransport:1\">" +
                "<PlayMode>NORMAL</PlayMode><RecQualityMode>NOT_IMPLEMENTED</RecQualityMode>" +
                "</u:GetTransportSettingsResponse></s:Body></s:Envelope>\n");
        assertEquals(PlayMode.NORMAL, new SonosDevice("127.0.0.1").getPlayMode());
    }

    @Test
    public void getVolume() throws Exception {
        MockHelper.mockCommandBuilder(
                "<s:Envelope xmlns:s=\"http://schemas.xmlsoap.org/soap/envelope/\" " +
                        "s:encodingStyle=\"http://schemas.xmlsoap.org/soap/encoding/\"><s:Body>" +
                        "<u:GetVolumeResponse xmlns:u=\"urn:schemas-upnp-org:service:RenderingControl:1\">" +
                        "<CurrentVolume>18</CurrentVolume></u:GetVolumeResponse></s:Body></s:Envelope>\n");
        assertEquals(18, new SonosDevice("127.0.0.1").getVolume());
    }

    @Test
    public void getMuteFalse() throws Exception {
        MockHelper.mockCommandBuilder(
                "<s:Envelope xmlns:s=\"http://schemas.xmlsoap.org/soap/envelope/\" " +
                        "s:encodingStyle=\"http://schemas.xmlsoap.org/soap/encoding/\"><s:Body>" +
                        "<u:GetMuteResponse xmlns:u=\"urn:schemas-upnp-org:service:RenderingControl:1\">" +
                        "<CurrentMute>0</CurrentMute></u:GetMuteResponse></s:Body></s:Envelope>\n");
        assertEquals(false, new SonosDevice("127.0.0.1").isMuted());
    }

    @Test
    public void getMuteTrue() throws Exception {
        MockHelper.mockCommandBuilder(
                "<s:Envelope xmlns:s=\"http://schemas.xmlsoap.org/soap/envelope/\" " +
                        "s:encodingStyle=\"http://schemas.xmlsoap.org/soap/encoding/\"><s:Body>" +
                        "<u:GetMuteResponse xmlns:u=\"urn:schemas-upnp-org:service:RenderingControl:1\">" +
                        "<CurrentMute>1</CurrentMute></u:GetMuteResponse></s:Body></s:Envelope>\n");
        assertEquals(true, new SonosDevice("127.0.0.1").isMuted());
    }

    @Test
    public void getBass() throws Exception {
        MockHelper.mockCommandBuilder("" +
                "<s:Envelope xmlns:s=\"http://schemas.xmlsoap.org/soap/envelope/\" " +
                "s:encodingStyle=\"http://schemas.xmlsoap.org/soap/encoding/\"><s:Body>" +
                "<u:GetBassResponse xmlns:u=\"urn:schemas-upnp-org:service:RenderingControl:1\">" +
                "<CurrentBass>0</CurrentBass></u:GetBassResponse></s:Body></s:Envelope>\n");
        assertEquals(0, new SonosDevice("127.0.0.1").getBass());
    }

    @Test
    public void getTreble() throws Exception {
        MockHelper.mockCommandBuilder(
                "<s:Envelope xmlns:s=\"http://schemas.xmlsoap.org/soap/envelope/\" " +
                        "s:encodingStyle=\"http://schemas.xmlsoap.org/soap/encoding/\"><s:Body>" +
                        "<u:GetTrebleResponse xmlns:u=\"urn:schemas-upnp-org:service:RenderingControl:1\">" +
                        "<CurrentTreble>-1</CurrentTreble></u:GetTrebleResponse></s:Body></s:Envelope>");
        assertEquals(-1, new SonosDevice("127.0.0.1").getTreble());
    }

    @Test(expected = IllegalArgumentException.class)
    public void setInvalidTrebleValue() throws Exception {
        MockHelper.mockCommandBuilder("");
        new SonosDevice("127.0.0.1").setTreble(11);
    }

    @Test(expected = IllegalArgumentException.class)
    public void setInvalidNegativeTrebleValue() throws Exception {
        MockHelper.mockCommandBuilder("");
        new SonosDevice("127.0.0.1").setTreble(-12);
    }

    @Test(expected = IllegalArgumentException.class)
    public void setInvalidBassValue() throws Exception {
        MockHelper.mockCommandBuilder("");
        new SonosDevice("127.0.0.1").setBass(11);
    }

    @Test(expected = IllegalArgumentException.class)
    public void setInvalidNegativeBassValue() throws Exception {
        MockHelper.mockCommandBuilder("");
        new SonosDevice("127.0.0.1").setBass(-12);
    }

    @Test
    public void setValidBassValue() throws Exception {
        MockHelper.mockCommandBuilder("");
        new SonosDevice("127.0.0.1").setBass(0);
    }

    @Test
    public void getLoudnessFalse() throws Exception {
        MockHelper.mockCommandBuilder(
                "<s:Envelope xmlns:s=\"http://schemas.xmlsoap.org/soap/envelope/\" " +
                        "s:encodingStyle=\"http://schemas.xmlsoap.org/soap/encoding/\"><s:Body>" +
                        "<u:GetLoudnessResponse xmlns:u=\"urn:schemas-upnp-org:service:RenderingControl:1\">" +
                        "<CurrentLoudness>0</CurrentLoudness></u:GetLoudnessResponse></s:Body></s:Envelope>\n");
        assertEquals(false, new SonosDevice("127.0.0.1").isLoudnessActivated());
    }

    @Test
    public void getLoudnessTrue() throws Exception {
        MockHelper.mockCommandBuilder(
                "<s:Envelope xmlns:s=\"http://schemas.xmlsoap.org/soap/envelope/\" " +
                        "s:encodingStyle=\"http://schemas.xmlsoap.org/soap/encoding/\"><s:Body>" +
                        "<u:GetLoudnessResponse xmlns:u=\"urn:schemas-upnp-org:service:RenderingControl:1\">" +
                        "<CurrentLoudness>1</CurrentLoudness></u:GetLoudnessResponse></s:Body></s:Envelope>\n");
        assertEquals(true, new SonosDevice("127.0.0.1").isLoudnessActivated());
    }

    @Test
    public void getLedStateFalse() throws Exception {
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
        
        MockHelper.mockCommandBuilder(data);
        assertEquals(false, new SonosDevice("127.0.0.1").getLedState());
    }

    @Test
    public void isNightModeActivated() throws Exception {
        String data = "<s:Envelope xmlns:s=\"http://schemas.xmlsoap.org/soap/envelope/\" s:encodingStyle=\"" +
                "http://schemas.xmlsoap.org/soap/encoding/\"><s:Body><u:GetEQResponse xmlns:u=" +
                "\"urn:schemas-upnp-org:service:RenderingControl:1\"><CurrentValue>0</CurrentValue>" +
                "</u:GetEQResponse></s:Body></s:Envelope>\n";
        MockHelper.mockCommandBuilder(data);
        assertEquals(false, new SonosDevice("127.0.0.1").isNightModeActivated());

        data = "<s:Envelope xmlns:s=\"http://schemas.xmlsoap.org/soap/envelope/\" s:encodingStyle=\"" +
                "http://schemas.xmlsoap.org/soap/encoding/\"><s:Body><u:GetEQResponse xmlns:u=" +
                "\"urn:schemas-upnp-org:service:RenderingControl:1\"><CurrentValue>1</CurrentValue>" +
                "</u:GetEQResponse></s:Body></s:Envelope>\n";
        MockHelper.mockCommandBuilder(data);
        assertEquals(true, new SonosDevice("127.0.0.1").isNightModeActivated());
    }

    @Test
    public void getLedStateTrue() throws Exception {
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

        MockHelper.mockCommandBuilder(data);
        assertEquals(true, new SonosDevice("127.0.0.1").getLedState());
    }

    @Test
    public void getZoneGroupState() throws Exception {
        MockHelper.mockCommandBuilder(
                "<CurrentZoneGroupName>Salon</CurrentZoneGroupName>" +
                "<CurrentZoneGroupID>X</CurrentZoneGroupID>" +
                "<CurrentZonePlayerUUIDsInGroup>A,B,X</CurrentZonePlayerUUIDsInGroup>"); //TODO: Add True Data
        SonosDevice sonosDevice = new SonosDevice("127.0.0.1");
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
    public void isJoined() throws Exception {
        MockHelper.mockCommandBuilder(
                "<CurrentZoneGroupName>Salon</CurrentZoneGroupName>" +
                        "<CurrentZoneGroupID>X</CurrentZoneGroupID>" +
                        "<CurrentZonePlayerUUIDsInGroup>A,B,X</CurrentZonePlayerUUIDsInGroup>"); //TODO: Add True Data
        SonosDevice sonosDevice = new SonosDevice("127.0.0.1");
        assertEquals(true, sonosDevice.isJoined());
    }

    @Test
    public void isNotJoined() throws Exception {
        MockHelper.mockCommandBuilder(
                "<CurrentZoneGroupName>Salon</CurrentZoneGroupName>" +
                        "<CurrentZoneGroupID>X</CurrentZoneGroupID>" +
                        "<CurrentZonePlayerUUIDsInGroup>X</CurrentZonePlayerUUIDsInGroup>"); //TODO: Add True Data
        SonosDevice sonosDevice = new SonosDevice("127.0.0.1");
        assertEquals(false, sonosDevice.isJoined());
    }


    @Test
    public void getSpeakerInfo() throws Exception {
        MockHelper.mockCommandBuilderDownloadSpeakerInfo(
                "<?xml-stylesheet type=\"text/xsl\" href=\"/xml/review.xsl\"?><ZPSupportInfo><ZPInfo><ZoneName>" +
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
                "</ZPInfo></ZPSupportInfo>");

        SonosSpeakerInfo speakerInfo = new SonosDevice("127.0.0.1").getSpeakerInfo();
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

    @Test
    /**
     * Issue #1 - Danish Zone Name Parsing Problem
     */
    public void getDanishZoneName() throws Exception {
        MockHelper.mockCommandBuilderDownloadSpeakerInfo("<?xml version=\"1.0\" ?>\n" +
                "<?xml-stylesheet type=\"text/xsl\" href=\"/xml/review.xsl\"?><ZPSupportInfo><ZPInfo>" +
                "<ZoneName>0. KÃ¸kken</ZoneName><ZoneIcon>x-rincon-roomicon:kitchen</ZoneIcon>" +
                "<Configuration>1</Configuration><LocalUID>RINCON_B8E937568F5A01400</LocalUID>" +
                "<SerialNumber>XX-XX-XX-XX-XX-XX:0</SerialNumber><SoftwareVersion>35.3-37210</SoftwareVersion>" +
                "<SoftwareDate>2017-01-21 22:51:23.323408</SoftwareDate><SoftwareScm>296936</SoftwareScm>" +
                "<MinCompatibleVersion>34.0-00000</MinCompatibleVersion>" +
                "<LegacyCompatibleVersion>25.0-00000</LegacyCompatibleVersion>" +
                "<HardwareVersion>1.8.3.7-2</HardwareVersion><DspVersion>0.25.3</DspVersion><HwFlags>0x0</HwFlags>" +
                "<HwFeatures>0x0</HwFeatures><Variant>0</Variant><GeneralFlags>0x0</GeneralFlags>" +
                "<IPAddress>192.168.10.110</IPAddress><MACAddress>XX-XX-XX-XX-XX-XX</MACAddress>" +
                "<Copyright>Â© 2003-2017, Sonos, Inc. All rights reserved.</Copyright>" +
                "<ExtraInfo>OTP: </ExtraInfo><HTAudioInCode>0</HTAudioInCode><IdxTrk></IdxTrk><MDP2Ver>0</MDP2Ver>" +
                "<MDP3Ver>0</MDP3Ver><RegState>3</RegState><CustomerID>XXXX</CustomerID>" +
                "</ZPInfo></ZPSupportInfo>");

        assertEquals("0. KÃ¸kken", new SonosDevice("127.0.0.1").getZoneName());
    }

    @Test(expected = UPnPSonosControllerException.class)
    public void errorParsing() throws IOException, SonosControllerException {
        SonosDevice sonosDevice = new SonosDevice("127.0.0.1");

        CommandBuilder.handleError("127.0.0.1", "<?xml version=\"1.0\"?>\n" +
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
        CommandBuilder.handleError("127.0.0.1", "");
    }

    @Test
    public void errorParsingErrorCode() throws SonosControllerException {
        SonosDevice sonosDevice = new SonosDevice("127.0.0.1");

        try {
            CommandBuilder.handleError("127.0.0.1", "<?xml version=\"1.0\"?>\n" +
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
            CommandBuilder.handleError("127.0.0.1", response);
        } catch (UPnPSonosControllerException e) {
            assertEquals(response, e.getResponse());
        }
    }

    @Test
    public void getStoppedPlayState() throws Exception {
        String response = "<s:Envelope xmlns:s=\"http://schemas.xmlsoap.org/soap/envelope/\" " +
                "s:encodingStyle=\"http://schemas.xmlsoap.org/soap/encoding/\"><s:Body>" +
                "<u:GetTransportInfoResponse xmlns:u=\"urn:schemas-upnp-org:service:AVTransport:1\">" +
                "<CurrentTransportState>STOPPED</CurrentTransportState><CurrentTransportStatus>OK" +
                "</CurrentTransportStatus><CurrentSpeed>1</CurrentSpeed></u:GetTransportInfoResponse>" +
                "</s:Body></s:Envelope>";
        MockHelper.mockCommandBuilder(response);
        SonosDevice sonosDevice = new SonosDevice("127.0.0.1");
        assertEquals(PlayState.STOPPED, sonosDevice.getPlayState());
    }

    @Test
    public void getPlayingPlayState() throws Exception {
        String response = "<s:Envelope xmlns:s=\"http://schemas.xmlsoap.org/soap/envelope/\" " +
                "s:encodingStyle=\"http://schemas.xmlsoap.org/soap/encoding/\"><s:Body>" +
                "<u:GetTransportInfoResponse xmlns:u=\"urn:schemas-upnp-org:service:AVTransport:1\">" +
                "<CurrentTransportState>PLAYING</CurrentTransportState><CurrentTransportStatus>OK" +
                "</CurrentTransportStatus><CurrentSpeed>1</CurrentSpeed></u:GetTransportInfoResponse>" +
                "</s:Body></s:Envelope>";
        MockHelper.mockCommandBuilder(response);
        SonosDevice sonosDevice = new SonosDevice("127.0.0.1");
        assertEquals(PlayState.PLAYING, sonosDevice.getPlayState());
    }

    @Test
    public void getPausedPlayState() throws Exception {
        String response = "<s:Envelope xmlns:s=\"http://schemas.xmlsoap.org/soap/envelope/\" " +
                "s:encodingStyle=\"http://schemas.xmlsoap.org/soap/encoding/\"><s:Body>" +
                "<u:GetTransportInfoResponse xmlns:u=\"urn:schemas-upnp-org:service:AVTransport:1\">" +
                "<CurrentTransportState>PAUSED_PLAYBACK</CurrentTransportState><CurrentTransportStatus>OK" +
                "</CurrentTransportStatus><CurrentSpeed>1</CurrentSpeed></u:GetTransportInfoResponse>" +
                "</s:Body></s:Envelope>";
        MockHelper.mockCommandBuilder(response);
        SonosDevice sonosDevice = new SonosDevice("127.0.0.1");
        assertEquals(PlayState.PAUSED_PLAYBACK, sonosDevice.getPlayState());
    }

    @Test
    public void getErrorPlayState() throws Exception {
        String response = "<s:Envelope xmlns:s=\"http://schemas.xmlsoap.org/soap/envelope/\" " +
                "s:encodingStyle=\"http://schemas.xmlsoap.org/soap/encoding/\"><s:Body>" +
                "<u:GetTransportInfoResponse xmlns:u=\"urn:schemas-upnp-org:service:AVTransport:1\">" +
                "<CurrentTransportState>ERROR</CurrentTransportState><CurrentTransportStatus>OK" +
                "</CurrentTransportStatus><CurrentSpeed>1</CurrentSpeed></u:GetTransportInfoResponse>" +
                "</s:Body></s:Envelope>";
        MockHelper.mockCommandBuilder(response);
        SonosDevice sonosDevice = new SonosDevice("127.0.0.1");
        assertEquals(PlayState.ERROR, sonosDevice.getPlayState());
    }

    @Test
    public void getTransitioningPlayState() throws Exception {
        String response = "<s:Envelope xmlns:s=\"http://schemas.xmlsoap.org/soap/envelope/\" " +
                "s:encodingStyle=\"http://schemas.xmlsoap.org/soap/encoding/\"><s:Body>" +
                "<u:GetTransportInfoResponse xmlns:u=\"urn:schemas-upnp-org:service:AVTransport:1\">" +
                "<CurrentTransportState>TRANSITIONING</CurrentTransportState><CurrentTransportStatus>OK" +
                "</CurrentTransportStatus><CurrentSpeed>1</CurrentSpeed></u:GetTransportInfoResponse>" +
                "</s:Body></s:Envelope>";
        MockHelper.mockCommandBuilder(response);
        SonosDevice sonosDevice = new SonosDevice("127.0.0.1");
        assertEquals(PlayState.TRANSITIONING, sonosDevice.getPlayState());
    }


    @Test
    public void getDesactivatedDialogMode() throws Exception {
        String response = "<s:Envelope xmlns:s=\"http://schemas.xmlsoap.org/soap/envelope/\" " +
                "s:encodingStyle=\"http://schemas.xmlsoap.org/soap/encoding/\"><s:Body>" +
                "<u:GetEQResponse xmlns:u=\"urn:schemas-upnp-org:service:RenderingControl:1\"><CurrentValue>0" +
                "</CurrentValue></u:GetEQResponse></s:Body></s:Envelope>";
        MockHelper.mockCommandBuilder(response);
        SonosDevice sonosDevice = new SonosDevice("127.0.0.1");
        assertEquals(false, sonosDevice.isDialogModeActivated());
    }

    @Test
    public void getActivatedDialogMode() throws Exception {
        String response = "<s:Envelope xmlns:s=\"http://schemas.xmlsoap.org/soap/envelope/\" " +
                "s:encodingStyle=\"http://schemas.xmlsoap.org/soap/encoding/\"><s:Body>" +
                "<u:GetEQResponse xmlns:u=\"urn:schemas-upnp-org:service:RenderingControl:1\"><CurrentValue>1" +
                "</CurrentValue></u:GetEQResponse></s:Body></s:Envelope>";
        MockHelper.mockCommandBuilder(response);
        SonosDevice sonosDevice = new SonosDevice("127.0.0.1");
        assertEquals(true, sonosDevice.isDialogModeActivated());
    }


    @Test
    public void checkCommandBuilderUsage() throws Exception {
        CommandBuilder commandBuilderMock = MockHelper.mockCommandBuilder("");

        SonosDevice sonosDevice = new SonosDevice("127.0.0.1");

        sonosDevice.play();
        sonosDevice.playUri("http://test.com", "");
        sonosDevice.pause();
        sonosDevice.stop();
        sonosDevice.seek("01:01:01");
        sonosDevice.next();
        sonosDevice.previous();
        sonosDevice.setPlayMode(PlayMode.NORMAL);
        sonosDevice.clearQueue();
        sonosDevice.join("MyPrettyUID<3");
        sonosDevice.unjoin();
        sonosDevice.setVolume(100);
        sonosDevice.setMute(true);
        sonosDevice.setLoudness(true);
        sonosDevice.setTreble(8);
        sonosDevice.setNightMode(true);
        sonosDevice.setZoneName("test");
        sonosDevice.setLedState(true);
        sonosDevice.setLedState(false);
        sonosDevice.setDialogMode(true);

        Mockito.verify(commandBuilderMock, Mockito.times(21)).executeOn("127.0.0.1");
    }

    @Test
    public void toStringTest() {
        SonosDevice sonosDevice = new SonosDevice("127.0.0.1");
        assertEquals("SonosDevice{ip='127.0.0.1'}", sonosDevice.toString());
    }
}
