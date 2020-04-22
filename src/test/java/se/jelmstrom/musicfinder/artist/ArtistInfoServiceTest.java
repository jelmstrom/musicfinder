package se.jelmstrom.musicfinder.artist;

import org.junit.jupiter.api.Test;
import se.jelmstrom.musicfinder.artist.client.artwork.CoverArtClient;
import se.jelmstrom.musicfinder.artist.client.discogs.DiscogsClient;
import se.jelmstrom.musicfinder.artist.client.musicBrainz.MusicBrainzClient;
import se.jelmstrom.musicfinder.artist.dto.Artist;

import static org.junit.jupiter.api.Assertions.*;

class ArtistInfoServiceTest {
    private ArtistInfoService service = new ArtistInfoService(
            new MusicBrainzClient(),
            new DiscogsClient(),
            new CoverArtClient());


    @Test
    public void getArtistWithDetails(){

        Artist artist = service.getArtistInfo("f27ec8db-af05-4f36-916e-3d57f91ecf5e");
        assertEquals("Michael Jackson", artist.getName());
        assertEquals("f27ec8db-af05-4f36-916e-3d57f91ecf5e", artist.getId());
        assertTrue(artist.getAlbums().stream().anyMatch(group -> group.getImages().size() > 0));
        assertEquals("15885", artist.getDiscogsId());
        assertEquals("https://www.discogs.com/artist/15885-Michael-Jackson", artist.getBio().getUri());
    }

    @Test
    public void cacheRetursSameEntityWhrenRequestedTwice(){
        Artist artist = service.getArtistInfo("f27ec8db-af05-4f36-916e-3d57f91ecf5e");
        Artist artist2 = service.getArtistInfo("f27ec8db-af05-4f36-916e-3d57f91ecf5e");
        assertTrue(artist == artist2);
    }

}