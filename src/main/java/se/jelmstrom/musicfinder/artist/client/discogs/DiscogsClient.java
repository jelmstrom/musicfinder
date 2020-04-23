package se.jelmstrom.musicfinder.artist.client.discogs;

import se.jelmstrom.musicfinder.artist.dto.ArtistBiography;
import se.jelmstrom.musicfinder.http.ClientWrapper;

import java.util.Optional;

public class DiscogsClient {
    private final ClientWrapper client = new ClientWrapper();
    private static final String URL_PATTERN = "https://api.discogs.com/artists/%s";


    public ArtistBiography artistFacts(String id) {
        Optional<ArtistBiography> discogsData = client.makeRequest(String.format(URL_PATTERN, id), ArtistBiography.class);
        return discogsData.get();
    }
}
