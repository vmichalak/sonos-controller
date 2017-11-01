package com.vmichalak.sonoscontroller.model;

public class TrackInfo {
    private final int queueIndex;
    private final String duration;
    private final String position;
    private final String uri;
    private final TrackMetadata metadata;

    public TrackInfo(int queueIndex, String duration, String position, String uri, TrackMetadata metadata) {
        this.queueIndex = queueIndex;
        this.duration = duration;
        this.position = position;
        this.uri = uri;
        this.metadata = metadata;
    }

    public int getQueueIndex() {
        return queueIndex;
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

    public TrackMetadata getMetadata() {
        return metadata;
    }

    @Override
    public String toString() {
        return "TrackInfo{" +
                "queueIndex=" + queueIndex +
                ", duration='" + duration + '\'' +
                ", position='" + position + '\'' +
                ", uri='" + uri + '\'' +
                ", metadata=" + metadata +
                '}';
    }
}
