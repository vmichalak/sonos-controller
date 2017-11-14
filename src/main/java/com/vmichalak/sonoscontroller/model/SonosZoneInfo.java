package com.vmichalak.sonoscontroller.model;

import com.vmichalak.sonoscontroller.SonosDevice;
import com.vmichalak.sonoscontroller.SonosDiscovery;

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

    public List<SonosDevice> getSonosDevicesInGroup() {
        ArrayList<SonosDevice> devices = new ArrayList<SonosDevice>();
        for (String uid : zonePlayerUIDInGroup) {
            try {
                SonosDevice device = SonosDiscovery.discoverByUID(uid);
                if(device != null) {
                    devices.add(device);
                }
            }
            catch (IOException e) { /* ignored */ }
        }
        return devices;
    }
}
