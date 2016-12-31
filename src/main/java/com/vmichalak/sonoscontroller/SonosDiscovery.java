package com.vmichalak.sonoscontroller;

import com.vmichalak.protocol.ssdp.Device;
import com.vmichalak.protocol.ssdp.SSDPClient;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SonosDiscovery {
    /**
     * Discover all SONOS speakers on network using SSDP (Simple Service Discovery Protocol);
     * @return List of SONOS speakers
     * @throws IOException
     */
    public static List<SonosDevice> discover() throws IOException {
        List<Device> source = SSDPClient.discover(1000, "urn:schemas-upnp-org:device:ZonePlayer:1");
        ArrayList<SonosDevice> output = new ArrayList<SonosDevice>();
        for (Device device : source) { output.add(new SonosDevice(device.getIPAddress())); }
        return output;
    }
}