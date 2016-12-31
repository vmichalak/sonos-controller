package com.vmichalak.sonoscontroller;

import com.vmichalak.protocol.ssdp.Device;

import java.io.IOException;
import java.util.List;

public class Main {
    public static void main(String[] args) throws IOException {
        List<SonosDevice> devices = SonosDiscovery.discover();
        System.out.println(devices);
        SonosDevice sonosDevice = devices.get(0);

        /*Device device = new Device("192.168.1.17",
                "http://192.168.1.17:1400/xml/device_description.xml",
                "Linux UPnP/1.0 Sonos/34.7-35162c (ZP90)",
                "urn:schemas-upnp-org:device:ZonePlayer:1",
                "uuid:RINCON_000E58A83A0A01400::urn:schemas-upnp-org:device:ZonePlayer:1");*/

        //SonosDevice sonosDevice = SonosDevice.instantiateFromSSDPDevice(device);
        //sonosDevice.setLedState(true);
    }
}
