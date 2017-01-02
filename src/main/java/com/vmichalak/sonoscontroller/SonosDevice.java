package com.vmichalak.sonoscontroller;

import com.vmichalak.sonoscontroller.exception.SonosControllerException;
import com.vmichalak.sonoscontroller.exception.UPnPSonosControllerException;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
        Pattern pattern = Pattern.compile("<PlayMode>(.*)</PlayMode>");
        Matcher matcher = pattern.matcher(r);
        matcher.find();
        return PlayMode.valueOf(matcher.group(1));
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

        Pattern pattern = Pattern.compile("<CurrentVolume>([0-9]*)</CurrentVolume>");
        Matcher matcher = pattern.matcher(r);
        matcher.find();

        return Integer.parseInt(matcher.group(1));
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
        Pattern pattern = Pattern.compile("<CurrentMute>([01])</CurrentMute>");
        Matcher matcher = pattern.matcher(r);
        matcher.find();
        return matcher.group(1).equals("1") ? true : false;
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

    public void setPlayerName(String playerName) throws IOException, SonosControllerException {
        this.sendCommand(DEVICE_ENDPOINT, DEVICE_SERVICE, "SetZoneAttributes",
                "<DesiredZoneName>" + playerName + "</DesiredZoneName><DesiredIcon /><DesiredConfiguration />");
    }

    public boolean getLedState() throws IOException, SonosControllerException {
        String r = this.sendCommand(DEVICE_ENDPOINT, DEVICE_SERVICE, "GetLEDState", "");
        Pattern pattern = Pattern.compile("<CurrentLEDState>(.*)</CurrentLEDState>");
        Matcher matcher = pattern.matcher(r);
        matcher.find();
        return matcher.group(1).equals("On") ? true : false;
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
        Pattern errorCodePattern = Pattern.compile("<errorCode>([0-9]*)</errorCode>");
        Matcher errorCodeMatcher = errorCodePattern.matcher(response);
        errorCodeMatcher.find();
        Pattern descriptionPattern = Pattern.compile("<errorDescription>(.*)</errorDescription>");
        Matcher descriptionMatcher = descriptionPattern.matcher(response);
        descriptionMatcher.find();
        int errorCode = Integer.parseInt(errorCodeMatcher.group(1));
        String errorDescription = "";
        if(descriptionMatcher.groupCount() < 1) {
            errorDescription = descriptionMatcher.group(1);
        }
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
