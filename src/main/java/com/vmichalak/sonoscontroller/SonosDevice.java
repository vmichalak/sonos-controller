package com.vmichalak.sonoscontroller;

import com.vmichalak.sonoscontroller.exception.SonosControllerException;
import com.vmichalak.sonoscontroller.exception.UPnPSonosControllerException;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

public class SonosDevice {
    private final static int    SOAP_PORT                   = 1400;
    private final static String TRANSPORT_ENDPOINT          = "/MediaRenderer/AVTransport/Control";
    private final static String TRANSPORT_SERVICE           = "urn:schemas-upnp-org:service:AVTransport:1";
    private final static String RENDERING_ENDPOINT          = "/MediaRenderer/RenderingControl/Control";
    private final static String RENDERING_SERVICE           = "urn:schemas-upnp-org:service:RenderingControl:1";
    private final static String DEVICE_ENDPOINT             = "/DeviceProperties/Control";
    private final static String DEVICE_SERVICE              = "urn:schemas-upnp-org:service:DeviceProperties:1";
    private final static String CONTENT_DIRECTORY_ENDPOINT  = "/MediaServer/ContentDirectory/Control";
    private final static String CONTENT_DIRECTORY_SERVICE   = "urn:schemas-upnp-org:service:ContentDirectory:1";

    private final String ip;

    private HttpClient httpClient;

    public SonosDevice(String ip) {
        this.ip = ip;
    }

    //<editor-fold desc="AV TRANSPORT">

    /**
     * Play the currently selected track.
     * @throws IOException
     * @throws SonosControllerException
     */
    public void play() throws IOException, SonosControllerException {
        this.sendCommand(TRANSPORT_ENDPOINT, TRANSPORT_SERVICE, "Play",
                "<InstanceID>0</InstanceID><Speed>1</Speed>");
    }

    /**
     * Play a given stream. Pauses the queue.
     * @param uri URI of a stream to be played.
     * @param meta The track metadata to show in the player (DIDL format).
     * @throws IOException
     * @throws SonosControllerException
     */
    public void playUri(String uri, String meta) throws IOException, SonosControllerException {
        this.sendCommand(TRANSPORT_ENDPOINT, TRANSPORT_SERVICE, "SetAVTransportURI",
                "<InstanceID>0</InstanceID><CurrentURI>" + uri
                + "</CurrentURI><CurrentURIMetaData>" + meta + "</CurrentURIMetaData>");

        this.play();
    }

    /**
     * Pause the currently playing track.
     * @throws IOException
     * @throws SonosControllerException
     */
    public void pause() throws IOException, SonosControllerException {
        this.sendCommand(TRANSPORT_ENDPOINT, TRANSPORT_SERVICE, "Pause",
                "<InstanceID>0</InstanceID><Speed>1</Speed>");
    }

    /**
     * Stop the currently playing track.
     * @throws IOException
     * @throws SonosControllerException
     */
    public void stop() throws IOException, SonosControllerException {
        this.sendCommand(TRANSPORT_ENDPOINT, TRANSPORT_SERVICE, "Stop",
                "<InstanceID>0</InstanceID><Speed>1</Speed>");
    }

    /**
     * Go to the next track on the queue.
     * @throws IOException
     * @throws SonosControllerException
     */
    public void next() throws IOException, SonosControllerException {
        this.sendCommand(TRANSPORT_ENDPOINT, TRANSPORT_SERVICE, "Next",
                "<InstanceID>0</InstanceID><Speed>1</Speed>");
    }

    /**
     * Go back to the previously played track.
     * @throws IOException
     * @throws SonosControllerException
     */
    public void previous() throws IOException, SonosControllerException {
        this.sendCommand(TRANSPORT_ENDPOINT, TRANSPORT_SERVICE, "Previous",
                "<InstanceID>0</InstanceID><Speed>1</Speed>");
    }

    /**
     * Get the play mode for the queue.
     * @return current PlayMode of the queue
     * @throws IOException
     * @throws SonosControllerException
     */
    public PlayMode getPlayMode() throws IOException, SonosControllerException {
        String r = this.sendCommand(TRANSPORT_ENDPOINT, TRANSPORT_SERVICE, "GetTransportSettings",
                "<InstanceID>0</InstanceID>");
        return PlayMode.valueOf(ParserHelper.findOne("<PlayMode>(.*)</PlayMode>", r));
    }

    /**
     * Sets the play mode for the queue.
     * @param playMode
     * @throws IOException
     * @throws SonosControllerException
     */
    public void setPlayMode(PlayMode playMode) throws IOException, SonosControllerException {
        this.sendCommand(TRANSPORT_ENDPOINT, TRANSPORT_SERVICE, "SetPlayMode",
                "<InstanceID>0</InstanceID><NewPlayMode>" + playMode + "</NewPlayMode>");
    }

    /**
     * Remove all tracks from the queue.
     * @throws IOException
     * @throws SonosControllerException
     */
    public void clearQueue() throws IOException, SonosControllerException {
        this.sendCommand(TRANSPORT_ENDPOINT, TRANSPORT_SERVICE, "RemoveAllTracksFromQueue",
                "<InstanceID>0</InstanceID>");
    }

    //</editor-fold>

    //<editor-fold desc="RENDERING">

    /**
     * Get the Sonos speaker volume.
     * @return A volume value between 0 and 100
     * @throws IOException
     * @throws SonosControllerException
     */
    public int getVolume() throws IOException, SonosControllerException {
        String r = this.sendCommand(RENDERING_ENDPOINT, RENDERING_SERVICE, "GetVolume",
                "<InstanceID>0</InstanceID><Channel>Master</Channel>");
        return Integer.parseInt(ParserHelper.findOne("<CurrentVolume>([0-9]*)</CurrentVolume>", r));
    }

    /**
     * Set the Sonos speaker volume.
     * @param volume A volume value between 0 and 100
     * @throws IOException
     * @throws SonosControllerException
     */
    public void setVolume(int volume) throws IOException, SonosControllerException {
        this.sendCommand(RENDERING_ENDPOINT, RENDERING_SERVICE, "SetVolume",
                "<InstanceID>0</InstanceID><Channel>Master</Channel><DesiredVolume>" + volume + "</DesiredVolume>");
    }

    /**
     * Return the mute state of the Sonos speaker.
     * @return True if is muted, false if isn't
     * @throws IOException
     * @throws SonosControllerException
     */
    public boolean getMute() throws IOException, SonosControllerException {
        String r = this.sendCommand(RENDERING_ENDPOINT, RENDERING_SERVICE, "GetMute",
                "<InstanceID>0</InstanceID><Channel>Master</Channel>");
        return ParserHelper.findOne("<CurrentMute>([01])</CurrentMute>", r).equals("1") ? true : false;
    }

    /**
     * Mute or unmute the Sonos speaker.
     * @param state True to mute, False to unmute
     * @throws IOException
     * @throws SonosControllerException
     */
    public void setMute(boolean state) throws IOException, SonosControllerException {
        String s = state ? "1" : "0";
        this.sendCommand(RENDERING_ENDPOINT, RENDERING_SERVICE, "SetMute",
                "<InstanceID>0</InstanceID><Channel>Master</Channel><DesiredMute>" + s + "</DesiredMute>");
    }

    /**
     * Mute or unmute the speaker.
     * @throws IOException
     * @throws SonosControllerException
     */
    public void switchMute() throws IOException, SonosControllerException {
        setMute(!getMute());
    }

    //</editor-fold>

    //<editor-fold desc="DEVICE">

    public String getZoneName() throws IOException, SonosControllerException {
        return this.getSpeakerInfo().getZoneName();
    }

    public void setZoneName(String zoneName) throws IOException, SonosControllerException {
        this.sendCommand(DEVICE_ENDPOINT, DEVICE_SERVICE, "SetZoneAttributes",
                "<DesiredZoneName>" + zoneName + "</DesiredZoneName><DesiredIcon /><DesiredConfiguration />");
    }

    public boolean getLedState() throws IOException, SonosControllerException {
        String r = this.sendCommand(DEVICE_ENDPOINT, DEVICE_SERVICE, "GetLEDState", "");
        return ParserHelper.findOne("<CurrentLEDState>(.*)</CurrentLEDState>", r).equals("On") ? true : false;
    }

    public void setLedState(boolean state) throws IOException, SonosControllerException {
        String s = state ? "On" : "Off";
        this.sendCommand(DEVICE_ENDPOINT, DEVICE_SERVICE, "SetLEDState",
                "<DesiredLEDState>" + s + "</DesiredLEDState>");
    }

    public void switchLedState() throws IOException, SonosControllerException {
        setLedState(!getLedState());
    }

    //</editor-fold>

    //<editor-fold desc="CONTENT DIRECTORY">

    //</editor-fold>

    /**
     * Get information about the Sonos speaker.
     * @return Information about the Sonos speaker, such as the UID, MAC Address, and Zone Name.
     * @throws IOException
     * @throws SonosControllerException
     */
    public SonosSpeakerInfo getSpeakerInfo() throws IOException, SonosControllerException {
        if(this.httpClient == null) { this.httpClient = HttpClientBuilder.create().build(); }
        String uri = "http://" + this.ip + ":" + SOAP_PORT + "/status/zp";
        HttpGet request = new HttpGet(uri);
        HttpResponse response = httpClient.execute(request);
        String responseString = EntityUtils.toString(response.getEntity());
        handleError(responseString);

        String zoneName = ParserHelper.findOne("<ZoneName>(.*)</ZoneName>", responseString);
        String zoneIcon = ParserHelper.findOne("<ZoneIcon>(.*)</ZoneIcon>", responseString);
        String configuration = ParserHelper.findOne("<Configuration>(.*)</Configuration>", responseString);
        String localUID = ParserHelper.findOne("<LocalUID>(.*)</LocalUID>", responseString);
        String serialNumber = ParserHelper.findOne("<SerialNumber>(.*)</SerialNumber>", responseString);
        String softwareVersion = ParserHelper.findOne("<SoftwareVersion>(.*)</SoftwareVersion>", responseString);
        String softwareDate = ParserHelper.findOne("<SoftwareDate>(.*)</SoftwareDate>", responseString);
        String softwareScm = ParserHelper.findOne("<SoftwareScm>(.*)</SoftwareScm>", responseString);
        String minCompatibleVersion = ParserHelper.findOne("<MinCompatibleVersion>(.*)</MinCompatibleVersion>", responseString);
        String legacyCompatibleVersion = ParserHelper.findOne("<LegacyCompatibleVersion>(.*)</LegacyCompatibleVersion>", responseString);
        String hardwareVersion = ParserHelper.findOne("<HardwareVersion>(.*)</HardwareVersion>", responseString);
        String dspVersion = ParserHelper.findOne("<DspVersion>(.*)</DspVersion>", responseString);
        String hwFlags = ParserHelper.findOne("<HwFlags>(.*)</HwFlags>", responseString);
        String hwFeatures = ParserHelper.findOne("<HwFeatures>(.*)</HwFeatures>", responseString);
        String variant = ParserHelper.findOne("<Variant>(.*)</Variant>", responseString);
        String generalFlags = ParserHelper.findOne("<GeneralFlags>(.*)</GeneralFlags>", responseString);
        String ipAddress = ParserHelper.findOne("<IPAddress>(.*)</IPAddress>", responseString);
        String macAddress = ParserHelper.findOne("<MACAddress>(.*)</MACAddress>", responseString);
        String copyright = ParserHelper.findOne("<Copyright>(.*)</Copyright>", responseString);
        String extraInfo = ParserHelper.findOne("<ExtraInfo>(.*)</ExtraInfo>", responseString);
        String htAudioInCode = ParserHelper.findOne("<HTAudioInCode>(.*)</HTAudioInCode>", responseString);
        String idxTrk = ParserHelper.findOne("<IdxTrk>(.*)</IdxTrk>", responseString);
        String mdp2Ver = ParserHelper.findOne("<MDP2Ver>(.*)</MDP2Ver>", responseString);
        String mdp3Ver = ParserHelper.findOne("<MDP3Ver>(.*)</MDP3Ver>", responseString);
        String relBuild = ParserHelper.findOne("<RelBuild>(.*)</RelBuild>", responseString);
        String whitelistBuild = ParserHelper.findOne("<WhitelistBuild>(.*)</WhitelistBuild>", responseString);
        String prodUnit = ParserHelper.findOne("<ProdUnit>(.*)</ProdUnit>", responseString);
        String fuseCfg = ParserHelper.findOne("<FuseCfg>(.*)</FuseCfg>", responseString);
        String revokeFuse = ParserHelper.findOne("<RevokeFuse>(.*)</RevokeFuse>", responseString);
        String authFlags = ParserHelper.findOne("<AuthFlags>(.*)</AuthFlags>", responseString);
        String swFeatures = ParserHelper.findOne("<SwFeatures>(.*)</SwFeatures>", responseString);
        String regState = ParserHelper.findOne("<RegState>(.*)</RegState>", responseString);
        String customerID = ParserHelper.findOne("<CustomerID>(.*)</CustomerID>", responseString);

        return new SonosSpeakerInfo(zoneName, zoneIcon, configuration, localUID, serialNumber, softwareVersion,
                softwareDate, softwareScm, minCompatibleVersion, legacyCompatibleVersion, hardwareVersion, dspVersion,
                hwFlags, hwFeatures, variant, generalFlags, ipAddress, macAddress, copyright, extraInfo, htAudioInCode,
                idxTrk, mdp2Ver, mdp3Ver, relBuild, whitelistBuild, prodUnit, fuseCfg, revokeFuse, authFlags,
                swFeatures, regState, customerID);
    }

    /**
     * Send a raw command to the Sonos speaker.
     * @param endpoint
     * @param action
     * @param body
     * @return the raw response body returned by the Sonos speaker.
     * @throws IOException
     */
    private String sendCommand(String endpoint, String service, String action, String body) throws IOException, SonosControllerException {
        if(this.httpClient == null) { this.httpClient = HttpClientBuilder.create().build(); }
        String uri = "http://" + this.ip + ":" + SOAP_PORT + endpoint;
        HttpPost request = new HttpPost(uri);
        request.setHeader("Content-Type", "text/xml");
        request.setHeader("SOAPACTION", service + "#" + action);
        String content = "<s:Envelope xmlns:s=\"http://schemas.xmlsoap.org/soap/envelope/\""
                        + " s:encodingStyle=\"http://schemas.xmlsoap.org/soap/encoding/\"><s:Body>"
                        + "<u:" + action + " xmlns:u=\"" + service + "\">"
                        + body
                        + "</u:" + action + ">"
                        + "</s:Body></s:Envelope>";
        HttpEntity entity = new ByteArrayEntity(content.getBytes("UTF-8"));
        request.setEntity(entity);
        HttpResponse response = httpClient.execute(request);
        String responseString = EntityUtils.toString(response.getEntity());
        handleError(responseString);
        return responseString;
    }

    private void handleError(String response) throws SonosControllerException {
        if(!response.contains("errorCode")) { return; }
        int errorCode = Integer.parseInt(ParserHelper.findOne("<errorCode>([0-9]*)</errorCode>", response));
        String errorDescription = ParserHelper.findOne("<errorDescription>(.*)</errorDescription>", response);
        if(errorDescription == null) { errorDescription = ""; }
        throw new UPnPSonosControllerException(
                "UPnP Error " + errorCode +" received: " + errorDescription + " from " + this.ip,
                errorCode, errorDescription, response);
    }

    @Override
    public String toString() {
        return "SonosDevice{" +
                "ip='" + ip + '\'' +
                '}';
    }
}
