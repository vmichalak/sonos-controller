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
import java.util.Arrays;
import java.util.List;

public class SonosDevice {
    private final static int    SOAP_PORT                    = 1400;
    private final static String TRANSPORT_ENDPOINT           = "/MediaRenderer/AVTransport/Control";
    private final static String TRANSPORT_SERVICE            = "urn:schemas-upnp-org:service:AVTransport:1";
    private final static String RENDERING_ENDPOINT           = "/MediaRenderer/RenderingControl/Control";
    private final static String RENDERING_SERVICE            = "urn:schemas-upnp-org:service:RenderingControl:1";
    private final static String DEVICE_ENDPOINT              = "/DeviceProperties/Control";
    private final static String DEVICE_SERVICE               = "urn:schemas-upnp-org:service:DeviceProperties:1";
    private final static String CONTENT_DIRECTORY_ENDPOINT   = "/MediaServer/ContentDirectory/Control";
    private final static String CONTENT_DIRECTORY_SERVICE    = "urn:schemas-upnp-org:service:ContentDirectory:1";
    private final static String ZONE_GROUP_TOPOLOGY_ENDPOINT = "/ZoneGroupTopology/Control";
    private final static String ZONE_GROUP_TOPOLOGY_SERVICE  = "urn:upnp-org:serviceId:ZoneGroupTopology";

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
     * Seeks to a given timestamp in the current track, specified in the format HH:MM:SS.
     * @param time specified in the format HH:MM:SS.
     * @throws IOException
     * @throws SonosControllerException
     */
    public void seek(String time) throws IOException, SonosControllerException {
        this.sendCommand(TRANSPORT_ENDPOINT, TRANSPORT_SERVICE, "Seek",
                "<InstanceID>0</InstanceID><Unit>REL_TIME</Unit><Target>" + time + "</Target>");
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

    /**
     * Return if the Sonos is joined with another one.
     * @return True if is joined, false if is isn't
     * @throws IOException
     * @throws SonosControllerException
     */
    public boolean isJoined() throws IOException, SonosControllerException {
        return this.getZoneGroupState().getZonePlayerUIDInGroup().size() > 1;
    }

    /**
     * Get all Sonos speaker joined with this speaker.
     * @return List of Sonos speaker joined with this speaker.
     * @throws IOException
     * @throws SonosControllerException
     */
    public List<SonosDevice> joinedWith() throws IOException, SonosControllerException {
        return this.getZoneGroupState().getSonosDevicesInGroup();
    }

    /**
     * Join this Sonos speaker to another.
     * @param master master speaker
     * @throws IOException
     * @throws SonosControllerException
     */
    public void join(SonosDevice master) throws IOException, SonosControllerException {
        this.join(master.getSpeakerInfo().getLocalUID());
    }

    /**
     * Join this Sonos speaker to another.
     * @param masterUID master speaker UID
     * @throws IOException
     * @throws SonosControllerException
     */
    public void join(String masterUID) throws IOException, SonosControllerException {
        this.sendCommand(TRANSPORT_ENDPOINT, TRANSPORT_SERVICE, "SetAVTransportURI",
                "<InstanceID>0</InstanceID><CurrentURI>x-rincon:" + masterUID +
                "</CurrentURI><CurrentURIMetaData></CurrentURIMetaData>");
    }

    /**
     * Remove this speaker from a group.
     * @throws IOException
     * @throws SonosControllerException
     */
    public void unjoin() throws IOException, SonosControllerException {
        this.sendCommand(TRANSPORT_ENDPOINT, TRANSPORT_SERVICE, "BecomeCoordinatorOfStandaloneGroup",
                "<InstanceID>0</InstanceID><Speed>1</Speed>");
    }

    /**
     * Switch the speaker's input to line-in.
     * @throws IOException
     * @throws SonosControllerException
     */
    public void switchToLineIn() throws IOException, SonosControllerException {
        //TODO: Test this feature (i only have SONOS Play:1)
        String uid = this.getSpeakerInfo().getLocalUID();
        this.sendCommand(TRANSPORT_ENDPOINT, TRANSPORT_SERVICE, "SetAVTransportURI",
                "<InstanceID>0</InstanceID><CurrentURI>x-rincon-stream:" + uid + "</CurrentURI><CurrentURIMetaData></CurrentURIMetaData>");
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

    /**
     * Get the Sonos speaker bass EQ.
     * @return value between 10 and -10
     * @throws IOException
     * @throws SonosControllerException
     */
    public int getBass() throws IOException, SonosControllerException {
        String r = this.sendCommand(RENDERING_ENDPOINT, RENDERING_SERVICE, "GetBass",
                "<InstanceID>0</InstanceID><Channel>Master</Channel>");
        return Integer.parseInt(ParserHelper.findOne("<CurrentBass>(.*)</CurrentBass>", r));
    }

    /**
     * Set the Sonos speaker bass EQ.
     * @param bass Value between 10 and -10
     * @throws IOException
     * @throws SonosControllerException
     */
    public void setBass(int bass) throws IOException, SonosControllerException {
        if(bass > 10 || bass < -10) { throw new IllegalArgumentException("Bass value need to be between 10 and -10"); }
        this.sendCommand(RENDERING_ENDPOINT, RENDERING_SERVICE, "SetBass",
                "<InstanceID>0</InstanceID><DesiredBass>" + bass + "</DesiredBass>");
    }
    
    /**
     * Get the Sonos speaker's loudness compensation.
     * @return True if is On, False if isn't
     * @throws IOException
     * @throws SonosControllerException
     */
    public boolean getLoudness() throws IOException, SonosControllerException {
        String r = this.sendCommand(RENDERING_ENDPOINT, RENDERING_SERVICE, "GetLoudness",
                "<InstanceID>0</InstanceID><Channel>Master</Channel>");
        return ParserHelper.findOne("<CurrentLoudness>(.*)</CurrentLoudness>", r).equals("1") ? true : false;
    }

    /**
     * Set the Sonos speaker's loudness compensation.
     * @param loudness True for set On, False for set Off
     * @throws IOException
     * @throws SonosControllerException
     */
    public void setLoudness(boolean loudness) throws IOException, SonosControllerException {
        String value = loudness ? "1" : "0";
        this.sendCommand(RENDERING_ENDPOINT, RENDERING_SERVICE, "SetLoudness",
                "<InstanceID>0</InstanceID><Channel>Master</Channel><DesiredLoudness>" + value + "</DesiredLoudness>");
    }

    /**
     * Get the Sonos speaker's treble EQ.
     * @return value between -10 and 10
     * @throws IOException
     * @throws SonosControllerException
     */
    public int getTreble() throws IOException, SonosControllerException {
        String r = this.sendCommand(RENDERING_ENDPOINT, RENDERING_SERVICE, "GetTreble",
                "<InstanceID>0</InstanceID><Channel>Master</Channel>");
        return Integer.parseInt(ParserHelper.findOne("<CurrentTreble>(.*)</CurrentTreble>", r));
    }

    /**
     * Set the Sonos speaker's treble EQ.
     * @param treble value between -10 and 10
     * @throws IOException
     * @throws SonosControllerException
     */
    public void setTreble(int treble) throws IOException, SonosControllerException {
        if(treble > 10 || treble < -10) { throw new IllegalArgumentException("treble value need to be between 10 and -10"); }
        this.sendCommand(RENDERING_ENDPOINT, RENDERING_SERVICE, "SetTreble",
                "<InstanceID>0</InstanceID><DesiredTreble>"+treble+"</DesiredTreble>");
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

    //<editor-fold desc="ZONE GROUP TOPOLOGY">

    public SonosZoneInfo getZoneGroupState() throws IOException, SonosControllerException {
        String r = this.sendCommand(ZONE_GROUP_TOPOLOGY_ENDPOINT, ZONE_GROUP_TOPOLOGY_SERVICE, "GetZoneGroupAttributes",
                "");
        String name = ParserHelper.findOne("<CurrentZoneGroupName>(.*)</CurrentZoneGroupName>", r);
        String id = ParserHelper.findOne("<CurrentZoneGroupID>(.*)</CurrentZoneGroupID>", r);
        String devices = ParserHelper.findOne("<CurrentZonePlayerUUIDsInGroup>(.*)</CurrentZonePlayerUUIDsInGroup>", r);
        List<String> deviceList = Arrays.asList(devices.split(","));
        return new SonosZoneInfo(name, id, deviceList);
    }

    //</editor-fold>

    protected String downloadSpeakerInfo() throws IOException, SonosControllerException {
        if(this.httpClient == null) { this.httpClient = HttpClientBuilder.create().build(); }
        String uri = "http://" + this.ip + ":" + SOAP_PORT + "/status/zp";
        HttpGet request = new HttpGet(uri);
        HttpResponse response = httpClient.execute(request);
        String responseString = EntityUtils.toString(response.getEntity());
        handleError(responseString);
        return responseString;
    }

    /**
     * Get information about the Sonos speaker.
     * @return Information about the Sonos speaker, such as the UID, MAC Address, and Zone Name.
     * @throws IOException
     * @throws SonosControllerException
     */
    public SonosSpeakerInfo getSpeakerInfo() throws IOException, SonosControllerException {
        String responseString = this.downloadSpeakerInfo();

        String zoneName                 = ParserHelper.findOne("<ZoneName>(.*)</ZoneName>", responseString);
        String zoneIcon                 = ParserHelper.findOne("<ZoneIcon>(.*)</ZoneIcon>", responseString);
        String configuration            = ParserHelper.findOne("<Configuration>(.*)</Configuration>", responseString);
        String localUID                 = ParserHelper.findOne("<LocalUID>(.*)</LocalUID>", responseString);
        String serialNumber             = ParserHelper.findOne("<SerialNumber>(.*)</SerialNumber>", responseString);
        String softwareVersion          = ParserHelper.findOne("<SoftwareVersion>(.*)</SoftwareVersion>",
                                        responseString);
        String softwareDate             = ParserHelper.findOne("<SoftwareDate>(.*)</SoftwareDate>", responseString);
        String softwareScm              = ParserHelper.findOne("<SoftwareScm>(.*)</SoftwareScm>", responseString);
        String minCompatibleVersion     = ParserHelper.findOne("<MinCompatibleVersion>(.*)</MinCompatibleVersion>",
                                        responseString);
        String legacyCompatibleVersion  = ParserHelper.findOne("<LegacyCompatibleVersion>(.*)</LegacyCompatibleVersion>"
                                        , responseString);
        String hardwareVersion          = ParserHelper.findOne("<HardwareVersion>(.*)</HardwareVersion>",
                                        responseString);
        String dspVersion               = ParserHelper.findOne("<DspVersion>(.*)</DspVersion>", responseString);
        String hwFlags                  = ParserHelper.findOne("<HwFlags>(.*)</HwFlags>", responseString);
        String hwFeatures               = ParserHelper.findOne("<HwFeatures>(.*)</HwFeatures>", responseString);
        String variant                  = ParserHelper.findOne("<Variant>(.*)</Variant>", responseString);
        String generalFlags             = ParserHelper.findOne("<GeneralFlags>(.*)</GeneralFlags>", responseString);
        String ipAddress                = ParserHelper.findOne("<IPAddress>(.*)</IPAddress>", responseString);
        String macAddress               = ParserHelper.findOne("<MACAddress>(.*)</MACAddress>", responseString);
        String copyright                = ParserHelper.findOne("<Copyright>(.*)</Copyright>", responseString);
        String extraInfo                = ParserHelper.findOne("<ExtraInfo>(.*)</ExtraInfo>", responseString);
        String htAudioInCode            = ParserHelper.findOne("<HTAudioInCode>(.*)</HTAudioInCode>", responseString);
        String idxTrk                   = ParserHelper.findOne("<IdxTrk>(.*)</IdxTrk>", responseString);
        String mdp2Ver                  = ParserHelper.findOne("<MDP2Ver>(.*)</MDP2Ver>", responseString);
        String mdp3Ver                  = ParserHelper.findOne("<MDP3Ver>(.*)</MDP3Ver>", responseString);
        String relBuild                 = ParserHelper.findOne("<RelBuild>(.*)</RelBuild>", responseString);
        String whitelistBuild           = ParserHelper.findOne("<WhitelistBuild>(.*)</WhitelistBuild>", responseString);
        String prodUnit                 = ParserHelper.findOne("<ProdUnit>(.*)</ProdUnit>", responseString);
        String fuseCfg                  = ParserHelper.findOne("<FuseCfg>(.*)</FuseCfg>", responseString);
        String revokeFuse               = ParserHelper.findOne("<RevokeFuse>(.*)</RevokeFuse>", responseString);
        String authFlags                = ParserHelper.findOne("<AuthFlags>(.*)</AuthFlags>", responseString);
        String swFeatures               = ParserHelper.findOne("<SwFeatures>(.*)</SwFeatures>", responseString);
        String regState                 = ParserHelper.findOne("<RegState>(.*)</RegState>", responseString);
        String customerID               = ParserHelper.findOne("<CustomerID>(.*)</CustomerID>", responseString);

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
    protected String sendCommand(String endpoint, String service, String action, String body) throws IOException, SonosControllerException {
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

    protected void handleError(String response) throws SonosControllerException {
        if(!response.contains("errorCode")) { return; }
        int errorCode = Integer.parseInt(ParserHelper.findOne("<errorCode>([0-9]*)</errorCode>", response));
        throw new UPnPSonosControllerException(
                "UPnP Error " + errorCode +" received from " + this.ip,
                errorCode, "", response);
    }

    @Override
    public String toString() {
        return "SonosDevice{" +
                "ip='" + ip + '\'' +
                '}';
    }
}
