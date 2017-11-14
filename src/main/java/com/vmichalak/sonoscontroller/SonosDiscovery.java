package com.vmichalak.sonoscontroller;

import com.vmichalak.protocol.ssdp.Device;
import com.vmichalak.protocol.ssdp.SSDPClient;
import com.vmichalak.sonoscontroller.exception.SonosControllerException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SonosDiscovery {

    // Hide the implicit public constructor.
    private SonosDiscovery() { }

    /**
     * Discover all SONOS speakers on network using SSDP (Simple Service Discovery Protocol).
     * @return List of SONOS speakers
     * @throws IOException
     */
    public static List<SonosDevice> discover() throws IOException {
        List<Device> source = SSDPClient.discover(1000, "urn:schemas-upnp-org:device:ZonePlayer:1");
        ArrayList<SonosDevice> output = new ArrayList<SonosDevice>();
        for (Device device : source) { output.add(new SonosDevice(device.getIPAddress())); }
        return Collections.unmodifiableList(output);
    }

    /**
     * Discover one SONOS speakers on network using SSDP (Simple Service Discovery Protocol).
     * @return SONOS speaker
     * @throws IOException
     */
    public static SonosDevice discoverOne() throws IOException {
        Device source = SSDPClient.discoverOne(1000, "urn:schemas-upnp-org:device:ZonePlayer:1");
        if(source == null) { return null; }
        return new SonosDevice(source.getIPAddress());
    }

    /**
     * Discover one SONOS speakers on network using SSDP (Simple Service Discovery Protocol) by UID.
     * @param uid Sonos Speaker UID
     * @return SONOS speaker
     * @throws IOException
     */
    public static SonosDevice discoverByUID(String uid) throws IOException {
        Device source = SSDPClient.discoverOne(1000, "uuid:" + uid);
        if(source == null) { return null; }
        return new SonosDevice(source.getIPAddress());
    }

    /**
     * Discover one SONOS speakers on network using SSDP (Simple Service Discovery Protocol) by name.
     * @param name Sonos Speaker name.
     * @return Sonos speaker (or null if no speaker was found)
     * @throws IOException
     */
    public static SonosDevice discoverByName(String name) throws IOException {
        List<SonosDevice> sonosDevices = SonosDiscovery.discover();
        for(SonosDevice sonosDevice : sonosDevices) {
            try {
                if(sonosDevice.getZoneName().equalsIgnoreCase(name)) {
                    return sonosDevice;
                }
            } catch (SonosControllerException e) { /* ignored */ }
        }
        return null;
    }
}