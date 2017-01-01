package com.vmichalak.sonoscontroller;

import com.vmichalak.protocol.ssdp.Device;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
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

    public void play() throws IOException {
        this.sendCommand(TRANSPORT_ENDPOINT, TRANSPORT_SERVICE, "Play",
                "<InstanceID>0</InstanceID><Speed>1</Speed>");
    }

    public void playUri(String uri, String meta) throws IOException {
        this.sendCommand(TRANSPORT_ENDPOINT, TRANSPORT_SERVICE, "SetAVTransportURI",
                "<InstanceID>0</InstanceID><CurrentURI>" + uri
                + "</CurrentURI><CurrentURIMetaData>" + meta + "</CurrentURIMetaData>");
        this.play();
    }

    public void pause() throws IOException {
        this.sendCommand(TRANSPORT_ENDPOINT, TRANSPORT_SERVICE, "Pause",
                "<InstanceID>0</InstanceID><Speed>1</Speed>");
    }

    public void stop() throws IOException {
        this.sendCommand(TRANSPORT_ENDPOINT, TRANSPORT_SERVICE, "Stop",
                "<InstanceID>0</InstanceID><Speed>1</Speed>");
    }

    public void next() throws IOException {
        this.sendCommand(TRANSPORT_ENDPOINT, TRANSPORT_SERVICE, "Next",
                "<InstanceID>0</InstanceID><Speed>1</Speed>");
    }

    public void previous() throws IOException {
        this.sendCommand(TRANSPORT_ENDPOINT, TRANSPORT_SERVICE, "Previous",
                "<InstanceID>0</InstanceID><Speed>1</Speed>");
    }

    public void clearQueue() throws IOException {
        this.sendCommand(TRANSPORT_ENDPOINT, TRANSPORT_SERVICE, "RemoveAllTracksFromQueue",
                "<InstanceID>0</InstanceID>");
    }

    //</editor-fold>

    //<editor-fold desc="RENDERING">

    public int getVolume() throws IOException {
        String r = this.sendCommand(RENDERING_ENDPOINT, RENDERING_SERVICE, "GetVolume",
                "<InstanceID>0</InstanceID><Channel>Master</Channel>");

        Pattern pattern = Pattern.compile("<CurrentVolume>([0-9]*)</CurrentVolume>");
        Matcher matcher = pattern.matcher(r);
        matcher.find();
        //TODO: Check errors

        return Integer.parseInt(matcher.group(1));
    }

    public void setVolume(int volume) throws IOException {
        this.sendCommand(RENDERING_ENDPOINT, RENDERING_SERVICE, "SetVolume",
                "<InstanceID>0</InstanceID><Channel>Master</Channel><DesiredVolume>" + volume + "</DesiredVolume>");
    }

    //</editor-fold>

    //<editor-fold desc="DEVICE">

    public void setPlayerName(String playerName) throws IOException {
        this.sendCommand(DEVICE_ENDPOINT, DEVICE_SERVICE, "SetZoneAttributes",
                "<DesiredZoneName>" + playerName + "</DesiredZoneName><DesiredIcon /><DesiredConfiguration />");
    }

    public boolean getLedState() throws IOException {
        String r = this.sendCommand(DEVICE_ENDPOINT, DEVICE_SERVICE, "GetLEDState", "");
        Pattern pattern = Pattern.compile("<CurrentLEDState>(.*)</CurrentLEDState>");
        Matcher matcher = pattern.matcher(r);
        matcher.find();
        return matcher.group(1).contains("On") ? true : false;
    }

    public void setLedState(boolean state) throws IOException {
        String s = state ? "On" : "Off";
        this.sendCommand(DEVICE_ENDPOINT, DEVICE_SERVICE, "SetLEDState",
                "<DesiredLEDState>" + s + "</DesiredLEDState>");
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
    private String sendCommand(String endpoint, String service, String action, String body) throws IOException {
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
        HttpEntity entity = null;
        entity = new ByteArrayEntity(content.getBytes("UTF-8"));
        request.setEntity(entity);
        HttpResponse response = httpClient.execute(request);
        return EntityUtils.toString(response.getEntity());
    }

    @Override
    public String toString() {
        return "SonosDevice{" +
                "ip='" + ip + '\'' +
                '}';
    }
}
