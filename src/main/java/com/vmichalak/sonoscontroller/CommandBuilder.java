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
import java.util.HashMap;
import java.util.Map;

class CommandBuilder {
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

    private final static HashMap<Integer, String> ERROR_DESCRIPTION_MAP = new HashMap<Integer, String>();

    static {
        ERROR_DESCRIPTION_MAP.put(400, "Bad Request");
        ERROR_DESCRIPTION_MAP.put(401, "Invalid Action");
        ERROR_DESCRIPTION_MAP.put(402, "Invalid Args");
        ERROR_DESCRIPTION_MAP.put(404, "Invalid Var");
        ERROR_DESCRIPTION_MAP.put(412, "Precondition Failed");
        ERROR_DESCRIPTION_MAP.put(501, "Action Failed");
        ERROR_DESCRIPTION_MAP.put(600, "Argument Value Invalid");
        ERROR_DESCRIPTION_MAP.put(601, "Argument Value Out of Range");
        ERROR_DESCRIPTION_MAP.put(602, "Option Action Not Implemented");
        ERROR_DESCRIPTION_MAP.put(603, "Out Of Memory");
        ERROR_DESCRIPTION_MAP.put(604, "Human Intervention Required");
        ERROR_DESCRIPTION_MAP.put(605, "String Argument Too Long");
        ERROR_DESCRIPTION_MAP.put(606, "Action Not Authorized");
        ERROR_DESCRIPTION_MAP.put(607, "Signature Failure");
        ERROR_DESCRIPTION_MAP.put(608, "Signature Missing");
        ERROR_DESCRIPTION_MAP.put(609, "Not Encrypted");
        ERROR_DESCRIPTION_MAP.put(610, "Invalid Sequence");
        ERROR_DESCRIPTION_MAP.put(611, "Invalid Control Url");
        ERROR_DESCRIPTION_MAP.put(612, "No Such Session");
    }

    private static HttpClient httpClient;

    private final String endpoint;
    private final String service;
    private final String action;
    private final HashMap<String, String> bodyEntries = new HashMap<String, String>();

    public CommandBuilder(String endpoint, String service, String action) {
        this.endpoint = endpoint;
        this.service = service;
        this.action = action;
    }

    public static CommandBuilder transport(String action) {
        return new CommandBuilder(TRANSPORT_ENDPOINT, TRANSPORT_SERVICE, action);
    }

    public static CommandBuilder rendering(String action) {
        return new CommandBuilder(RENDERING_ENDPOINT, RENDERING_SERVICE, action);
    }

    public static CommandBuilder device(String action) {
        return new CommandBuilder(DEVICE_ENDPOINT, DEVICE_SERVICE, action);
    }

    public static CommandBuilder contentDirectory(String action) {
        return new CommandBuilder(CONTENT_DIRECTORY_ENDPOINT, CONTENT_DIRECTORY_SERVICE, action);
    }

    public static CommandBuilder zoneGroupTopology(String action) {
        return new CommandBuilder(ZONE_GROUP_TOPOLOGY_ENDPOINT, ZONE_GROUP_TOPOLOGY_SERVICE, action);
    }

    public static String downloadSpeakerInfo(String ip) throws IOException, SonosControllerException {
        String uri = "http://" + ip + ":" + SOAP_PORT + "/status/zp";
        HttpGet request = new HttpGet(uri);
        HttpResponse response = getHttpClient().execute(request);
        String responseString = EntityUtils.toString(response.getEntity());
        handleError(ip, responseString);
        return responseString;
    }

    public CommandBuilder put(String key, String value) {
        this.bodyEntries.put(key, value);
        return this;
    }

    public String executeOn(String ip) throws IOException, SonosControllerException {
        String uri = "http://" + ip + ":" + SOAP_PORT + this.endpoint;
        HttpPost request = new HttpPost(uri);
        request.setHeader("Content-Type", "text/xml");
        request.setHeader("SOAPACTION", this.service + "#" + this.action);
        String content = "<s:Envelope xmlns:s=\"http://schemas.xmlsoap.org/soap/envelope/\""
                + " s:encodingStyle=\"http://schemas.xmlsoap.org/soap/encoding/\"><s:Body>"
                + "<u:" + this.action + " xmlns:u=\"" + this.service + "\">"
                + this.getBody()
                + "</u:" + this.action + ">"
                + "</s:Body></s:Envelope>";
        HttpEntity entity = new ByteArrayEntity(content.getBytes("UTF-8"));
        request.setEntity(entity);
        HttpResponse response = getHttpClient().execute(request);
        String responseString = EntityUtils.toString(response.getEntity());
        this.handleError(ip, responseString);
        return responseString;
    }

    protected static void handleError(String ip, String response) throws SonosControllerException {
        if(!response.contains("errorCode")) { return; }
        int errorCode = Integer.parseInt(ParserHelper.findOne("<errorCode>([0-9]*)</errorCode>", response));
        String desc = ERROR_DESCRIPTION_MAP.get(errorCode);
        throw new UPnPSonosControllerException(
                "UPnP Error " + errorCode +" (" + desc + ") received from " + ip,
                errorCode, desc, response);
    }

    protected String getBody() {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry entry : bodyEntries.entrySet()) {
            sb.append("<").append(entry.getKey()).append(">").append(entry.getValue()).append("</").append(entry.getKey()).append(">");
        }
        return sb.toString();
    }

    private static HttpClient getHttpClient() {
        if(httpClient == null) { httpClient = HttpClientBuilder.create().build(); }
        return httpClient;
    }
}
