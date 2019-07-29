package com.vmichalak.sonoscontroller.model;

import com.vmichalak.sonoscontroller.ParserHelper;

public class Favorite {

	private final String title;
    private final String albumArtURI;
    private final String description;
    private final String protocolinfo;
    
    public Favorite(String title, String albumArtURI, String description, String protoclinfo) {
        this.title = title;
        this.albumArtURI = albumArtURI;
        this.description=description;
        this.protocolinfo=protoclinfo;
        
    }
    
    public static Favorite parse(String metadata) {
        return new Favorite(
                ParserHelper.findOne("<dc:title>(.*?)</dc:title>", metadata),
                ParserHelper.findOne("<upnp:albumArtURI>(.*)</upnp:albumArtURI>", metadata),
                ParserHelper.findOne("<r:description>(.*)</r:description>", metadata),
                ParserHelper.findOne("<res protocolInfo=.*>(.*)</res>" , metadata)
                
        );
    }
    
    public String getTitle() {
        return title;
    }

    public String getAlbumArtURI() {
        return albumArtURI;
    }

    @Override
    public String toString() {
        return "Favorite{" +
                "title='" + title + '\'' + System.lineSeparator() +
                ", albumArtURI='" + albumArtURI + '\'' + System.lineSeparator() +
                ", description='" + description + '\'' + System.lineSeparator() +
                ", protocolinfo='" + protocolinfo + '\'' + System.lineSeparator() +
                '}';
    }
    
    
    
	public String getProtocolinfo() {
		return protocolinfo;
	}

	public String getDescription() {
		return description;
	}

    public String toDIDL() {
        return "<DIDL-Lite xmlns:dc=\"http://purl.org/dc/elements/1.1/\" xmlns:upnp=\"urn:schemas-upnp-org:metadata-1-0/upnp/\" xmlns:r=\"urn:schemas-rinconnetworks-com:metadata-1-0/\" xmlns=\"urn:schemas-upnp-org:metadata-1-0/DIDL-Lite/\">" +
                    "<item>" +
                        "<dc:title>" + title + "</dc:title>" +
                        "<r:description>" + description + "</r:description>" +
                        "<upnp:albumArtURI>" + albumArtURI + "</upnp:albumArtURI>" +
                    "</item>" +
                "</DIDL-Lite>";
    }

}
