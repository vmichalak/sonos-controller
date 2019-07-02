package com.vmichalak.sonoscontroller.model;

import com.vmichalak.sonoscontroller.ParserHelper;

public class TrackMetadata {
    private final String title;
    private final String creator;
    private final String albumArtist;
    private final String album;
    private final String albumArtURI;

    public TrackMetadata(String title, String creator, String albumArtist, String album, String albumArtURI) {
        this.title = title;
        this.creator = creator;
        this.albumArtist = albumArtist;
        this.album = album;
        this.albumArtURI = albumArtURI;
    }
    
    public static TrackMetadata parse(String metadata) {
      
	    String T = ParserHelper.findOne("<dc:title>(.*)</dc:title>", metadata);
	    if(T.equals("stream"))
	    {
	    	T = ParserHelper.findOne("<r:streamContent>(.*)</r:streamContent>", metadata);
	    }
	    String C = ParserHelper.findOne("<dc:creator>(.*)</dc:creator>", metadata);
	    String AA =ParserHelper.findOne("<r:albumArtist>(.*)</r:albumArtist>", metadata);
	    String A = ParserHelper.findOne("<upnp:album>(.*)</upnp:album>", metadata);
	    String AU= ParserHelper.findOne("<upnp:albumArtURI>(.*)</upnp:albumArtURI>", metadata);
	    return new TrackMetadata(T, C, AA, A, AU);   
    }

    public String getTitle() {
        return title;
    }

    public String getCreator() {
        return creator;
    }

    public String getAlbumArtist() {
        return albumArtist;
    }

    public String getAlbum() {
        return album;
    }

    public String getAlbumArtURI() {
        return albumArtURI;
    }

    @Override
    public String toString() {
        return "TrackMetadata{" +
                "title='" + title + '\'' +
                ", creator='" + creator + '\'' +
                ", albumArtist='" + albumArtist + '\'' +
                ", album='" + album + '\'' +
                ", albumArtURI='" + albumArtURI + '\'' +
                '}';
    }
}
