package se.jelmstrom.musicfinder.artist.musicbrainz;

import org.junit.jupiter.api.Test;
import se.jelmstrom.musicfinder.artist.client.artwork.CoverArtClient;
import se.jelmstrom.musicfinder.artist.dto.Artist;
import se.jelmstrom.musicfinder.artist.dto.ArtistList;
import se.jelmstrom.musicfinder.artist.dto.Album;
import se.jelmstrom.musicfinder.artist.client.musicBrainz.MusicBrainzClient;


import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.*;

class MusicBrainzClientTest {
    private final MusicBrainzClient musicBrainzClient = new MusicBrainzClient();
    private final CoverArtClient coverArtClient = new CoverArtClient();
    @Test
    public void searchArtistReturnsList(){
        ArtistList artistList = musicBrainzClient.searchArtist("Michael Jackson");

        Artist artist = artistList.getArtists().stream().findFirst().get();
        assertEquals("Michael Jackson", artist.getName());
        assertEquals("f27ec8db-af05-4f36-916e-3d57f91ecf5e", artist.getId());
        assertEquals(100, artist.getScore());
    }

    @Test
    public void unknownArtistReturnsEmptyList(){
        ArtistList artists = musicBrainzClient.searchArtist("lkjhfoiuhfsdnf");
        assertEquals(0, artists.getArtists().size());
    }

    @Test
    public void getArtistReturnsArtistDetailsWithLinks(){
        Artist artist = musicBrainzClient.getArtist("f27ec8db-af05-4f36-916e-3d57f91ecf5e");
        assertEquals("Michael Jackson", artist.getName());
        assertEquals("f27ec8db-af05-4f36-916e-3d57f91ecf5e", artist.getId());

        assertEquals("15885", artist.getDiscogsId());
    }
}
