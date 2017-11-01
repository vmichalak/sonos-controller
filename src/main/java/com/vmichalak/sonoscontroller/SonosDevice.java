package com.vmichalak.sonoscontroller;

import com.vmichalak.sonoscontroller.exception.SonosControllerException;
import com.vmichalak.sonoscontroller.model.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SonosDevice {

    private final String ip;

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
        CommandBuilder.transport("Play").put("InstanceID", "0").put("Speed", "1").executeOn(this.ip);
    }

    /**
     * Play a given stream. Pauses the queue.
     * @param uri URI of a stream to be played.
     * @param meta The track metadata to show in the player (DIDL format).
     * @throws IOException
     * @throws SonosControllerException
     */
    public void playUri(String uri, String metadata) throws IOException, SonosControllerException {
        CommandBuilder.transport("SetAVTransportURI").put("InstanceID", "0").put("CurrentURI", uri)
                .put("CurrentURIMetaData", metadata).executeOn(this.ip);
        this.play();
    }

    /**
     * Play an item from the queue.
     * @param queueIndex >= 0
     */
    public void playFromQueue(int queueIndex) throws IOException, SonosControllerException {
        if(queueIndex < 0) { throw new IllegalArgumentException("Queue index cannot be < 0."); }
        this.playUri("x-rincon-queue:" + this.getSpeakerInfo().getLocalUID() + "#0", "");
        CommandBuilder.transport("Seek").put("InstanceID", "0").put("Unit", "TRACK_NR")
                .put("Target", String.valueOf(queueIndex)).executeOn(this.ip);
        this.play();
    }

    /**
     * Pause current music, Play URI and resume (very useful for announcement).
     * clip is a blocking method. Take care !
     * @param uri URI of a stream to be played.
     * @param metadata The track metadata to show in the player (DIDL format).
     * @throws IOException
     * @throws SonosControllerException
     * @throws InterruptedException
     */
    public void clip(String uri, String metadata) throws IOException, SonosControllerException, InterruptedException {
        TrackInfo previous = this.getCurrentTrackInfo();
        this.playUri(uri, metadata);
        while (this.getPlayState() != PlayState.STOPPED) {
            Thread.sleep(500);
        }
        this.playUri("x-rincon-queue:" + this.getSpeakerInfo().getLocalUID() + "#0", "");
        this.playFromQueue(previous.getQueueIndex());
        this.seek(previous.getPosition());
        this.play();
    }

    /**
     * Pause the currently playing track.
     * @throws IOException
     * @throws SonosControllerException
     */
    public void pause() throws IOException, SonosControllerException {
        CommandBuilder.transport("Pause").put("InstanceID", "0").put("Speed", "1").executeOn(this.ip);
    }

    /**
     * Get the play state of the device.
     * @return current PlayState of the device
     * @throws IOException
     * @throws SonosControllerException
     */
    public PlayState getPlayState() throws IOException, SonosControllerException {
        String r = CommandBuilder.transport("GetTransportInfo").put("InstanceID", "0").executeOn(this.ip);
        return PlayState.valueOf(ParserHelper.findOne("<CurrentTransportState>(.*)</CurrentTransportState>", r));
    }

    /**
     * Stop the currently playing track.
     * @throws IOException
     * @throws SonosControllerException
     */
    public void stop() throws IOException, SonosControllerException {
        CommandBuilder.transport("Stop").put("InstanceID", "0").put("Speed", "1").executeOn(this.ip);
    }

    /**
     * Seeks to a given timestamp in the current track, specified in the format HH:MM:SS.
     * @param time specified in the format HH:MM:SS.
     * @throws IOException
     * @throws SonosControllerException
     */
    public void seek(String time) throws IOException, SonosControllerException {
        CommandBuilder.transport("Seek").put("InstanceID", "0").put("Unit", "REL_TIME").put("Target", time)
                .executeOn(this.ip);
    }

    /**
     * Go to the next track on the queue.
     * @throws IOException
     * @throws SonosControllerException
     */
    public void next() throws IOException, SonosControllerException {
        CommandBuilder.transport("Next").put("InstanceID", "0").put("Speed", "1").executeOn(this.ip);
    }

    /**
     * Go back to the previously played track.
     * @throws IOException
     * @throws SonosControllerException
     */
    public void previous() throws IOException, SonosControllerException {
        CommandBuilder.transport("Previous").put("InstanceID", "0").put("Speed", "1").executeOn(this.ip);
    }

    /**
     * Adds a given track to the queue.
     * @param uri URI of a stream to be played.
     * @param metadata The track metadata to show in the player (DIDL format).
     * @throws IOException
     * @throws SonosControllerException
     */
    public void addToQueue(String uri, String metadata) throws IOException, SonosControllerException {
        CommandBuilder.transport("AddURIToQueue").put("InstanceID", "0").put("EnqueuedURI", uri)
                .put("EnqueuedURIMetaData", metadata).put("DesiredFirstTrackNumberEnqueued", "0")
                .put("EnqueueAsNext", "1").executeOn(this.ip);
    }

    /**
     * Remove a track from the queue.
     * @param queueIndex >= 0
     * @throws IOException
     * @throws SonosControllerException
     */
    public void removeFromQueue(int queueIndex) throws IOException, SonosControllerException {
        if(queueIndex < 0) { throw new IllegalArgumentException("Queue index cannot be < 0."); }
        CommandBuilder.transport("RemoveTrackFromQueue").put("InstanceID", "0").put("ObjectID", "Q:0/" + queueIndex)
                .put("UpdateID", "0").executeOn(this.ip);
    }

    /**
     * Get Current Track Info (position in the queue, duration, position, ...).
     * @return TrackInfo object.
     * @throws IOException
     * @throws SonosControllerException
     */
    public TrackInfo getCurrentTrackInfo() throws IOException, SonosControllerException {
        String r = CommandBuilder.transport("GetPositionInfo").put("InstanceID", "0").put("Channel", "Master")
                .executeOn(this.ip);
        String track = ParserHelper.findOne("<Track>([0-9]*)</Track>", r);
        int trackNumber = -1;
        if(!track.equals("NOT_IMPLEMENTED") && !track.equals("")) { trackNumber = Integer.valueOf(track); }
        return new TrackInfo(
                trackNumber,
                ParserHelper.findOne("<TrackDuration>([0-9]*:[0-9]*:[0-9]*)</TrackDuration>", r),
                ParserHelper.findOne("<RelTime>([0-9]*:[0-9]*:[0-9]*)</RelTime>", r),
                ParserHelper.findOne("<TrackURI>(.*)</TrackURI>", r),
                TrackMetadata.parse(ParserHelper.findOne("<TrackMetaData>(.*)</TrackMetaData>", r))
        );
    }

    /**
     * Get the play mode for the queue.
     * @return current PlayMode of the queue
     * @throws IOException
     * @throws SonosControllerException
     */
    public PlayMode getPlayMode() throws IOException, SonosControllerException {
        String r = CommandBuilder.transport("GetTransportSettings").put("InstanceID", "0").executeOn(this.ip);
        return PlayMode.valueOf(ParserHelper.findOne("<PlayMode>(.*)</PlayMode>", r));
    }

    /**
     * Sets the play mode for the queue.
     * @param playMode
     * @throws IOException
     * @throws SonosControllerException
     */
    public void setPlayMode(PlayMode playMode) throws IOException, SonosControllerException {
        CommandBuilder.transport("SetPlayMode").put("InstanceID", "0").put("NewPlayMode", playMode.toString())
                .executeOn(this.ip);
    }

    /**
     * Remove all tracks from the queue.
     * @throws IOException
     * @throws SonosControllerException
     */
    public void clearQueue() throws IOException, SonosControllerException {
        CommandBuilder.transport("RemoveAllTracksFromQueue").put("InstanceID", "0").executeOn(this.ip);
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
        CommandBuilder.transport("SetAVTransportURI")
                .put("InstanceID", "0").put("CurrentURI", "x-rincon:" + masterUID).put("CurrentURIMetaData", "")
                .executeOn(this.ip);
    }

    /**
     * Remove this speaker from a group.
     * @throws IOException
     * @throws SonosControllerException
     */
    public void unjoin() throws IOException, SonosControllerException {
        CommandBuilder.transport("BecomeCoordinatorOfStandaloneGroup")
                .put("InstanceID", "0").put("Speed", "1").executeOn(this.ip);
    }

    /**
     * Switch the speaker's input to line-in.
     * @throws IOException
     * @throws SonosControllerException
     */
    public void switchToLineIn() throws IOException, SonosControllerException {
        String uid = this.getSpeakerInfo().getLocalUID();
        CommandBuilder.transport("SetAVTransportURI").put("InstanceID", "0")
                .put("CurrentURI", "x-rincon-stream:" + uid).put("CurrentURIMetaData", "").executeOn(this.ip);
    }

    /**
     * Switch the speaker's input to TV input.
     * /!\ WARNING: WORKS ONLY WITH PLAYBAR / PLAYBASE /!\
     * @throws IOException
     * @throws SonosControllerException
     */
    public void switchToTV() throws IOException, SonosControllerException {
        String uid = this.getSpeakerInfo().getLocalUID();
        CommandBuilder.transport("SetAVTransportURI").put("InstanceID", "0")
                .put("CurrentURI", "x-sonos-htastream:" + uid + ":spdif").put("CurrentURIMetaData", "")
                .executeOn(this.ip);
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
        String r = CommandBuilder.rendering("GetVolume").put("InstanceID", "0").put("Channel", "Master")
                .executeOn(this.ip);
        return Integer.parseInt(ParserHelper.findOne("<CurrentVolume>([0-9]*)</CurrentVolume>", r));
    }

    /**
     * Set the Sonos speaker volume.
     * @param volume A volume value between 0 and 100
     * @throws IOException
     * @throws SonosControllerException
     */
    public void setVolume(int volume) throws IOException, SonosControllerException {
        CommandBuilder.rendering("SetVolume").put("InstanceID", "0").put("Channel", "Master")
                .put("DesiredVolume", String.valueOf(volume)).executeOn(this.ip);
    }

    /**
     * Return the mute state of the Sonos speaker.
     * @return True if is muted, false if isn't
     * @throws IOException
     * @throws SonosControllerException
     */
    public boolean isMuted() throws IOException, SonosControllerException {
        String r = CommandBuilder.rendering("GetMute").put("InstanceID", "0").put("Channel", "Master")
                .executeOn(this.ip);
        return ParserHelper.findOne("<CurrentMute>([01])</CurrentMute>", r).equals("1") ? true : false;
    }

    /**
     * Mute or unmute the Sonos speaker.
     * @param state True to mute, False to unmute
     * @throws IOException
     * @throws SonosControllerException
     */
    public void setMute(boolean state) throws IOException, SonosControllerException {
        CommandBuilder.rendering("SetMute").put("InstanceID", "0").put("Channel", "Master")
                .put("DesiredMute", state ? "1" : "0").executeOn(this.ip);
    }

    /**
     * Mute or unmute the speaker.
     * @throws IOException
     * @throws SonosControllerException
     */
    public void switchMute() throws IOException, SonosControllerException {
        setMute(!isMuted());
    }

    /**
     * Get the Sonos speaker bass EQ.
     * @return value between 10 and -10
     * @throws IOException
     * @throws SonosControllerException
     */
    public int getBass() throws IOException, SonosControllerException {
        String r = CommandBuilder.rendering("GetBass").put("InstanceID", "0").put("Channel", "Master")
                .executeOn(this.ip);
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
        CommandBuilder.rendering("SetBass").put("InstanceID", "0").put("DesiredBass", String.valueOf(bass))
                .executeOn(this.ip);
    }
    
    /**
     * Get the Sonos speaker's loudness compensation.
     * @return True if is On, False if isn't
     * @throws IOException
     * @throws SonosControllerException
     */
    public boolean isLoudnessActivated() throws IOException, SonosControllerException {
        String r = CommandBuilder.rendering("GetLoudness").put("InstanceID", "0").put("Channel", "Master")
                .executeOn(this.ip);
        return ParserHelper.findOne("<CurrentLoudness>(.*)</CurrentLoudness>", r).equals("1") ? true : false;
    }

    /**
     * Set the Sonos speaker's loudness compensation.
     * @param loudness True for set On, False for set Off
     * @throws IOException
     * @throws SonosControllerException
     */
    public void setLoudness(boolean loudness) throws IOException, SonosControllerException {
        CommandBuilder.rendering("SetLoudness").put("InstanceID", "0").put("Channel", "Master")
                .put("DesiredLoudness", loudness ? "1" : "0").executeOn(this.ip);
    }

    /**
     * Get the Sonos speaker's treble EQ.
     * @return value between -10 and 10
     * @throws IOException
     * @throws SonosControllerException
     */
    public int getTreble() throws IOException, SonosControllerException {
        String r = CommandBuilder.rendering("GetTreble").put("InstanceID", "0").put("Channel", "Master")
                .executeOn(this.ip);
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
        CommandBuilder.rendering("SetTreble").put("InstanceID", "0").put("DesiredTreble", String.valueOf(treble))
                .executeOn(this.ip);
    }

    /**
     * Check if the Night Mode is activated or not.
     * /!\ WARNING: WORKS ONLY WITH PLAYBAR / PLAYBASE /!\
     * @return True if activated, False if isn't.
     * @throws IOException
     * @throws SonosControllerException
     */
    public boolean isNightModeActivated() throws IOException, SonosControllerException {
        String s = CommandBuilder.rendering("GetEQ").put("InstanceID", "0").put("EQType", "NightMode")
                .executeOn(this.ip);
        return ParserHelper.findOne("<CurrentValue>(.*)</CurrentValue>", s).equals("1") ? true : false;
    }

    /**
     * Set the Night Mode.
     * /!\ WARNING: WORKS ONLY WITH PLAYBAR / PLAYBASE /!\
     * @param state
     * @throws IOException
     * @throws SonosControllerException
     */
    public void setNightMode(boolean state) throws IOException, SonosControllerException {
        CommandBuilder.rendering("SetEQ").put("InstanceID", "0").put("EQType", "NightMode")
                .put("DesiredValue", state ? "1" : "0").executeOn(this.ip);
    }

    /**
     * Turn On / Off the Night Mode.
     * @throws IOException
     * @throws SonosControllerException
     */
    public void switchNightMode() throws IOException, SonosControllerException {
        this.setNightMode(!this.isNightModeActivated());
    }

    /**
     * Check if the Dialog Mode is activated or not.
     * /!\ WARNING: WORKS ONLY WITH PLAYBAR / PLAYBASE /!\
     * @return True if activated, False if isn't.
     * @throws IOException
     * @throws SonosControllerException
     */
    public boolean isDialogModeActivated() throws IOException, SonosControllerException {
        String s = CommandBuilder.rendering("GetEQ").put("InstanceID", "0").put("EQType", "DialogLevel")
                .executeOn(this.ip);
        return ParserHelper.findOne("<CurrentValue>(.*)</CurrentValue>", s).equals("1") ? true : false;
    }

    /**
     * Set the Dialog Mode.
     * /!\ WARNING: WORKS ONLY WITH PLAYBAR / PLAYBASE /!\
     * @param state
     * @throws IOException
     * @throws SonosControllerException
     */
    public void setDialogMode(boolean state) throws IOException, SonosControllerException {
        CommandBuilder.rendering("SetEQ").put("InstanceID", "0").put("EQType", "DialogLevel")
                .put("DesiredValue", state ? "1" : "0").executeOn(this.ip);
    }

    /**
     * Turn On / Off the Night Mode.
     * /!\ WARNING: WORKS ONLY WITH PLAYBAR / PLAYBASE /!\
     * @throws IOException
     * @throws SonosControllerException
     */
    public void switchDialogMode() throws IOException, SonosControllerException {
        this.setDialogMode(!this.isDialogModeActivated());
    }

    //</editor-fold>

    //<editor-fold desc="DEVICE">

    public String getZoneName() throws IOException, SonosControllerException {
        return this.getSpeakerInfo().getZoneName();
    }

    public void setZoneName(String zoneName) throws IOException, SonosControllerException {
        CommandBuilder.device("SetZoneAttributes").put("DesiredZoneName", zoneName).put("DesiredIcon", "")
                .put("DesiredConfiguration", "").executeOn(this.ip);
    }

    public boolean getLedState() throws IOException, SonosControllerException {
        String r = CommandBuilder.device("GetLEDState").executeOn(this.ip);
        return ParserHelper.findOne("<CurrentLEDState>(.*)</CurrentLEDState>", r).equals("On") ? true : false;
    }

    public void setLedState(boolean state) throws IOException, SonosControllerException {
        CommandBuilder.device("SetLEDState").put("DesiredLEDState", state ? "On" : "Off").executeOn(this.ip);
    }

    public void switchLedState() throws IOException, SonosControllerException {
        setLedState(!getLedState());
    }

    //</editor-fold>

    //<editor-fold desc="CONTENT DIRECTORY">

    public List<TrackMetadata> getQueue(int startingIndex, int requestedCount) throws IOException, SonosControllerException {
        String r = CommandBuilder.contentDirectory("Browse")
                .put("ObjectID", "Q:0")
                .put("BrowseFlag", "BrowseDirectChildren")
                .put("Filter", "dc:title,res,dc:creator,upnp:artist,upnp:album,upnp:albumArtURI")
                .put("StartingIndex", String.valueOf(startingIndex))
                .put("RequestedCount", String.valueOf(requestedCount))
                .put("SortCriteria", "")
                .executeOn(this.ip);
        List<String> itemsNonParsed = ParserHelper.findAll("<item .+?(?=>)>(.+?(?=</item>))", r);
        List<TrackMetadata> itemsParsed = new ArrayList<TrackMetadata>();
        for (String s : itemsNonParsed) {
            itemsParsed.add(TrackMetadata.parse(s));
        }
        return itemsParsed;
    }

    //</editor-fold>

    //<editor-fold desc="ZONE GROUP TOPOLOGY">

    public SonosZoneInfo getZoneGroupState() throws IOException, SonosControllerException {
        String r = CommandBuilder.zoneGroupTopology("GetZoneGroupAttributes").executeOn(this.ip);
        String name = ParserHelper.findOne("<CurrentZoneGroupName>(.*)</CurrentZoneGroupName>", r);
        String id = ParserHelper.findOne("<CurrentZoneGroupID>(.*)</CurrentZoneGroupID>", r);
        String devices = ParserHelper.findOne("<CurrentZonePlayerUUIDsInGroup>(.*)</CurrentZonePlayerUUIDsInGroup>", r);
        List<String> deviceList = Arrays.asList(devices.split(","));
        return new SonosZoneInfo(name, id, deviceList);
    }

    /**
     * Check if the speaker is a group coordinator or not.
     * @return True if the speaker is a group coordinator, otherwise False.
     * @throws IOException
     * @throws SonosControllerException
     */
    public boolean isCoordinator() throws IOException, SonosControllerException {
        // If zone have the same UID as the speaker -> speaker is the coordinator of the zone.
        String uid = this.getSpeakerInfo().getLocalUID();
        return getZoneGroupState().getId().startsWith(uid);
    }

    //</editor-fold>

    /**
     * Get information about the Sonos speaker.
     * @return Information about the Sonos speaker, such as the UID, MAC Address, and Zone Name.
     * @throws IOException
     * @throws SonosControllerException
     */
    public SonosSpeakerInfo getSpeakerInfo() throws IOException, SonosControllerException {
        String responseString = CommandBuilder.downloadSpeakerInfo(this.ip);

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

    @Override
    public String toString() {
        return "SonosDevice{" +
                "ip='" + ip + '\'' +
                '}';
    }
}
