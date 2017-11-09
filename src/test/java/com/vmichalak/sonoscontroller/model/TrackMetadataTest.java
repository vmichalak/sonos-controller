package com.vmichalak.sonoscontroller.model;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TrackMetadataTest {
    @Test
    public void parse() {
        String metadataString =
                "<DIDL-Lite xmlns:dc=\"http://purl.org/dc/elements/1.1/\" xmlns:upnp=\"urn:schemas-upnp-org:metadata-1-0/upnp/\" xmlns:r=\"urn:schemas-rinconnetworks-com:metadata-1-0/\" xmlns=\"urn:schemas-upnp-org:metadata-1-0/DIDL-Lite/\">" +
                    "<item id=\"-1\" parentID=\"-1\">" +
                        "<res protocolInfo=\"http-get:*:audio/x-spotify:*\" duration=\"0:03:23\">x-sonos-spotify:spotify%3atrack%3a2rOi4xsX0M04hiYnswIX9E?sid=9&amp;flags=0&amp;sn=3</res>" +
                        "<upnp:albumArtURI>https://i.scdn.co/image/581f4402e14ac0f839f7b50dff4fdd0bbc02bee5</upnp:albumArtURI>" +
                        "<upnp:class>object.item.audioItem.musicTrack</upnp:class>" +
                        "<dc:title>Drunk In The Morning</dc:title>" +
                        "<dc:creator>Lukas Graham</dc:creator>" +
                        "<r:albumArtist>Lukas Graham</r:albumArtist>" +
                        "<upnp:album>Lukas Graham (Blue Album)</upnp:album>" +
                        "<r:tiid>ed24b408301ad5d0e5271e1581023364</r:tiid>" +
                    "</item>" +
                "</DIDL-Lite>";

        TrackMetadata parsedMetadata = TrackMetadata.parse(metadataString);
        assertEquals("Drunk In The Morning", parsedMetadata.getTitle());
        assertEquals("Lukas Graham", parsedMetadata.getCreator());
        assertEquals("Lukas Graham", parsedMetadata.getAlbumArtist());
        assertEquals("Lukas Graham (Blue Album)", parsedMetadata.getAlbum());
        assertEquals("https://i.scdn.co/image/581f4402e14ac0f839f7b50dff4fdd0bbc02bee5", parsedMetadata.getAlbumArtURI());
        assertEquals("TrackMetadata{title='Drunk In The Morning', creator='Lukas Graham', albumArtist='Lukas Graham', album='Lukas Graham (Blue Album)', albumArtURI='https://i.scdn.co/image/581f4402e14ac0f839f7b50dff4fdd0bbc02bee5'}", parsedMetadata.toString());
        assertEquals("<DIDL-Lite xmlns:dc=\"http://purl.org/dc/elements/1.1/\" xmlns:upnp=\"urn:schemas-upnp-org:metadata-1-0/upnp/\" xmlns:r=\"urn:schemas-rinconnetworks-com:metadata-1-0/\" xmlns=\"urn:schemas-upnp-org:metadata-1-0/DIDL-Lite/\"><item><dc:title>Drunk In The Morning</dc:title><dc:creator>Lukas Graham</dc:creator><dc:albumArtist>Lukas Graham</dc:albumArtist><upnp:album>Lukas Graham (Blue Album)</upnp:album><upnp:albumArtURI>https://i.scdn.co/image/581f4402e14ac0f839f7b50dff4fdd0bbc02bee5</upnp:albumArtURI></item></DIDL-Lite>", parsedMetadata.toDIDL());
    }
}
