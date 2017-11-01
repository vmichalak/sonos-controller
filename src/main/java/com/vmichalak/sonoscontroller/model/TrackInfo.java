package com.vmichalak.sonoscontroller.model;

public class TrackInfo {
    private final int playlistPosition;
    private final String duration;
    private final String position;
    private final String uri;

    public TrackInfo(int playlistPosition, String duration, String position, String uri) {
        this.playlistPosition = playlistPosition;
        this.duration = duration;
        this.position = position;
        this.uri = uri;
    }

    public int getPlaylistPosition() {
        return playlistPosition;
    }

    public String getDuration() {
        return duration;
    }

    public String getPosition() {
        return position;
    }

    public String getUri() {
        return uri;
    }

    @Override
    public String toString() {
        return "TrackInfo{" +
                "playlistPosition=" + playlistPosition +
                ", duration='" + duration + '\'' +
                ", position='" + position + '\'' +
                ", uri='" + uri + '\'' +
                '}';
    }
}
