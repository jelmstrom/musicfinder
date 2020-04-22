package se.jelmstrom.musicfinder.artist.client.musicBrainz;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import se.jelmstrom.musicfinder.artist.client.artwork.CoverArtClient;
import se.jelmstrom.musicfinder.artist.dto.Artist;
import se.jelmstrom.musicfinder.artist.dto.ArtistList;
import se.jelmstrom.musicfinder.artist.dto.Album;
import se.jelmstrom.musicfinder.artist.dto.Relation;
import se.jelmstrom.musicfinder.http.ClientWrapper;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Optional;


public class MusicBrainzClient {
    private final ClientWrapper client = new ClientWrapper();
    private final static Log log = LogFactory.getLog(MusicBrainzClient.class);

    private final String searchArtistPattern = "https://musicbrainz.org/ws/2/artist?query=%s&fmt=json";
    private final String getArtistPattern = "https://musicbrainz.org/ws/2/artist/%s?inc=url-rels+release-groups&fmt=json";
    private final String coverArtPattern = "http://coverartarchive.org/release-group/%s";


    public ArtistList searchArtist(String query) {

        try {
            String url = String.format(searchArtistPattern, URLEncoder.encode(query, StandardCharsets.UTF_8.toString()));
            Optional<ArtistList> searchResult = client.makeRequest(url, ArtistList.class);
            if (searchResult.isPresent()) {
                return searchResult.get();
            }
        } catch (IOException e) {
            log.warn(e.getMessage());
        }
        return new ArtistList();
    }

    public Artist getArtist(String mbId) {
        Optional<Artist> result = client.makeRequest(String.format(getArtistPattern, mbId), Artist.class);
        if(result.isPresent()){
            Artist artist = result.get();
            artist.setDiscogsId(getDiscogsId(artist));
            return artist;
        }  else {
            return new Artist();
        }
    }

    private String getDiscogsId(Artist artist) {
        String discogsId = null;

        Relation discogsResource = artist.getRelations()
                .stream()
                .filter(rel -> "discogs".equalsIgnoreCase(rel.getType()))
                .findFirst()
                .orElse(new Relation());
        if(discogsResource != null && discogsResource.getUrl() != null) {
            String url = discogsResource.getUrl().getResource();
            discogsId = url.substring(url.lastIndexOf('/')+1);
        }
        return discogsId;
    }
}
