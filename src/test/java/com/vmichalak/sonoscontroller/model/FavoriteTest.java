package com.vmichalak.sonoscontroller.model;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class FavoriteTest {
    @Test
    public void parse() {
    	
    	String favoriteString =
    			  "<DIDL-Lite xmlns:dc=\"http://purl.org/dc/elements/1.1/\" xmlns:upnp=\"urn:schemas-upnp-org:metadata-1-0/upnp/\" xmlns:r=\"urn:schemas-rinconnetworks-com:metadata-1-0/\" xmlns=\"urn:schemas-upnp-org:metadata-1-0/DIDL-Lite/\">"
	    			+ "<item id=\"FV:2/0\" parentID=\"FV:2\" restricted=\"false\">"
		    			+ "<dc:title>Radio FM1</dc:title>"
		    			+ "<upnp:class>object.itemobject.item.sonos-favorite</upnp:class>"
		    			+ "<r:ordinal>2</r:ordinal>"
		    			+ "<res protocolInfo=\"x-sonosapi-stream:*:*:*\">x-sonosapi-stream:s25077?sid=254&flags=8224&sn=0</res>"
		    			+ "<upnp:albumArtURI>http://cdn-radiotime-logos.tunein.com/s25077q.png</upnp:albumArtURI>"
		    			+ "<r:type>instantPlay</r:type>"
		    			+ "<r:description>TuneIn Sender</r:description>"
		    			+ "<r:resMD>"
			    			+ "<DIDL-Lite xmlns:dc=\"http://purl.org/dc/elements/1.1/\" xmlns:upnp=\"urn:schemas-upnp-org:metadata-1-0/upnp/\" xmlns:r=\"urn:schemas-rinconnetworks-com:metadata-1-0/\" xmlns=\"urn:schemas-upnp-org:metadata-1-0/DIDL-Lite/\">"
				    			+ "<item id=\"F10092020s25077\" parentID=\"F00020000search%3astation:fm1\" restricted=\"true\">"
					    			+ "<dc:title>Radio FM1</dc:title>"
					    			+ "<upnp:class>object.item.audioItem.audioBroadcast</upnp:class>"
					    			+ "<desc id=\"cdudn\" nameSpace=\"urn:schemas-rinconnetworks-com:metadata-1-0/\">SA_RINCON65031_</desc>"
				    			+ "</item>"
			    			+ "</DIDL-Lite>"
		    			+ "</r:resMD>"
	    			+ "</item>"
    			+ "</DIDL-Lite>";

        Favorite parsedFavorite = Favorite.parse(favoriteString);
        assertEquals("Radio FM1", parsedFavorite.getTitle());
        assertEquals("TuneIn Sender", parsedFavorite.getDescription());
        assertEquals("x-sonosapi-stream:s25077?sid=254&flags=8224&sn=0", parsedFavorite.getProtocolinfo());
        assertEquals("http://cdn-radiotime-logos.tunein.com/s25077q.png", parsedFavorite.getAlbumArtURI());
        assertEquals("Favorite{title='Radio FM1'\r\n" + 
        		", albumArtURI='http://cdn-radiotime-logos.tunein.com/s25077q.png'\r\n" + 
        		", description='TuneIn Sender'\r\n" + 
        		", protocolinfo='x-sonosapi-stream:s25077?sid=254&flags=8224&sn=0'\r\n" + 
        		"}", parsedFavorite.toString());
        assertEquals("<DIDL-Lite xmlns:dc=\"http://purl.org/dc/elements/1.1/\" xmlns:upnp=\"urn:schemas-upnp-org:metadata-1-0/upnp/\" xmlns:r=\"urn:schemas-rinconnetworks-com:metadata-1-0/\" xmlns=\"urn:schemas-upnp-org:metadata-1-0/DIDL-Lite/\"><item><dc:title>Radio FM1</dc:title><r:description>TuneIn Sender</r:description><upnp:albumArtURI>http://cdn-radiotime-logos.tunein.com/s25077q.png</upnp:albumArtURI></item></DIDL-Lite>", parsedFavorite.toDIDL());
    }
}