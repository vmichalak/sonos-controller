package com.vmichalak.sonoscontroller;

import com.vmichalak.protocol.ssdp.Device;
import com.vmichalak.protocol.ssdp.SSDPClient;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SonosZoneInfo {
    private final String name;
    private final String id;
    private final List<String> zonePlayerUIDInGroup;

    public SonosZoneInfo(String name, String id, List<String> zonePlayerUIDInGroup) {
        this.name = name;
        this.id = id;
        this.zonePlayerUIDInGroup = zonePlayerUIDInGroup;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public List<String> getZonePlayerUIDInGroup() {
        return Collections.unmodifiableList(zonePlayerUIDInGroup);
    }

    public List<SonosDevice> getSonosDeviceInGroup() {
        ArrayList<SonosDevice> devices = new ArrayList<SonosDevice>();
        for (String uid : zonePlayerUIDInGroup) {
            try {
                Device device = SSDPClient.discoverOne(1000, "uuid:" + uid);
                devices.add(new SonosDevice(device.getIPAddress()));
            }
            catch (IOException e) { }
        }
        return devices;
    }
}
