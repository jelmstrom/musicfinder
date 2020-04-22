package se.jelmstrom.musicfinder.artist.client.artwork;

import org.junit.jupiter.api.Test;
import se.jelmstrom.musicfinder.artist.dto.Album;

import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.*;

class CoverArtClientTest {


    private CoverArtClient coverArtClient = new CoverArtClient();

    @Test
    public void decorateWithArtworkAsync(){
        Album album = new Album();
        album.setId("d6b52521-0dfa-390f-970f-790174c22752");
        CompletableFuture<Album> future = coverArtClient.decorateWithArtwork(album);
        future.join();
        assertEquals(3, album.getImages().size());
    }

}