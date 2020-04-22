package se.jelmstrom.musicfinder.artist.client.artwork;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import se.jelmstrom.musicfinder.artist.client.musicBrainz.Artwork;
import se.jelmstrom.musicfinder.artist.client.musicBrainz.ArtworkResponse;
import se.jelmstrom.musicfinder.artist.dto.Album;
import se.jelmstrom.musicfinder.http.ClientWrapper;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import static java.util.stream.Collectors.toList;

public class CoverArtClient {
    private static final ClientWrapper client = new ClientWrapper();
    private final static Log log = LogFactory.getLog(CoverArtClient.class);
    private final String coverArtPattern = "http://coverartarchive.org/release-group/%s";

    /**
     * Adds artwork to albums.
     *
     * @return
     * @param albums
     */
    public List<Album> getAlbumsWithImages(List<Album> albums) {
        List<CompletableFuture<Album>> futures = albums
                .parallelStream()
                .map(this::decorateWithArtwork)
                .collect(toList());

        // use AllOf to get event when all are completes
        List<Album> albumListWithArtwork = CompletableFuture
                .allOf(futures.toArray(new CompletableFuture[futures.size()])) // but why?
                .thenApply(future -> {
                    return futures.stream()
                            .map(CompletableFuture::join)
                            .collect(toList());
                }).join();
        return albumListWithArtwork;
    }

    public CompletableFuture<Album> decorateWithArtwork(Album album) {
        return CompletableFuture.supplyAsync(() -> {
            Optional<ArtworkResponse> images = client.makeRequest(String.format(coverArtPattern, album.getId()), ArtworkResponse.class);
            images.ifPresent(artworkResponse -> album.setImages(artworkResponse.getImages().stream().map(Artwork::getImage).collect(toList())));
            return album;
        }).exceptionally( err -> {
            log.warn(String.format("Failed to get artwork for %s", album.getId()));
            return album;
        });
    }
}
