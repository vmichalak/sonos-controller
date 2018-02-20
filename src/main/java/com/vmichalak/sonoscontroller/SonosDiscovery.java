package com.vmichalak.sonoscontroller;

import com.vmichalak.protocol.ssdp.Device;
import com.vmichalak.protocol.ssdp.SSDPClient;
import com.vmichalak.sonoscontroller.exception.SonosControllerException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SonosDiscovery {

    private static final int DEFAULT_SCAN_DURATION = 1000;

    // Hide the implicit public constructor.
    private SonosDiscovery() { }

    /**
     * Discover all SONOS speakers on network using SSDP (Simple Service Discovery Protocol).
     * @return List of SONOS speakers
     * @throws IOException
     */
    public static List<SonosDevice> discover() throws IOException {
        return discover(DEFAULT_SCAN_DURATION);
    }

    /**
     * Discover all SONOS speakers on network using SSDP (Simple Service Discovery Protocol).
     * @param scanDuration The number of milliseconds to wait while scanning for devices.
     * @return List of SONOS speakers
     * @throws IOException
     */
    public static List<SonosDevice> discover(int scanDuration) throws IOException {
        List<Device> source = SSDPClient.discover(scanDuration, "urn:schemas-upnp-org:device:ZonePlayer:1");
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
        return discoverOne(DEFAULT_SCAN_DURATION);
    }

    /**
     * Discover one SONOS speakers on network using SSDP (Simple Service Discovery Protocol).
     * @param scanDuration The number of milliseconds to wait while scanning for devices.
     * @return SONOS speaker
     * @throws IOException
     */
    public static SonosDevice discoverOne(int scanDuration) throws IOException {
        Device source = SSDPClient.discoverOne(scanDuration, "urn:schemas-upnp-org:device:ZonePlayer:1");
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
        return discoverByUID(uid, DEFAULT_SCAN_DURATION);
    }

    /**
     * Discover one SONOS speakers on network using SSDP (Simple Service Discovery Protocol) by UID.
     * @param uid Sonos Speaker UID
     * @param scanDuration The number of milliseconds to wait while scanning for devices.
     * @return SONOS speaker
     * @throws IOException
     */
    public static SonosDevice discoverByUID(String uid, int scanDuration) throws IOException {
        Device source = SSDPClient.discoverOne(scanDuration, "uuid:" + uid);
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
        return discoverByName(name, DEFAULT_SCAN_DURATION);
    }

    /**
     * Discover one SONOS speakers on network using SSDP (Simple Service Discovery Protocol) by name.
     * @param name Sonos Speaker name.
     * @param scanDuration The number of milliseconds to wait while scanning for devices.
     * @return Sonos speaker (or null if no speaker was found)
     * @throws IOException
     */
    public static SonosDevice discoverByName(String name, int scanDuration) throws IOException {
        List<SonosDevice> sonosDevices = SonosDiscovery.discover(scanDuration);
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