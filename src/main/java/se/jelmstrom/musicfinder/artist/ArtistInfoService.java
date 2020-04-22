package se.jelmstrom.musicfinder.artist;

import se.jelmstrom.musicfinder.artist.client.artwork.CoverArtClient;
import se.jelmstrom.musicfinder.artist.client.discogs.DiscogsClient;
import se.jelmstrom.musicfinder.artist.client.musicBrainz.MusicBrainzClient;
import se.jelmstrom.musicfinder.artist.dto.Album;
import se.jelmstrom.musicfinder.artist.dto.Artist;
import se.jelmstrom.musicfinder.artist.dto.ArtistList;

import java.util.List;

public class ArtistInfoService {
    private final MusicBrainzClient client;
    private final DiscogsClient discogsClient;
    private final CoverArtClient coverArtClient;

    public ArtistInfoService(MusicBrainzClient client, DiscogsClient discogsClient, CoverArtClient coverArtClient) {
        this.client = client;
        this.discogsClient = discogsClient;
        this.coverArtClient = coverArtClient;
    }


    public Artist getArtistInfo(String mbId){
        Artist artist = Cache.get(mbId);
        if (artist == null) {
            artist = client.getArtist(mbId);
            List<Album> albumsWithImages = coverArtClient.getAlbumsWithImages(artist.getAlbums());
            artist.setAlbums(albumsWithImages);
            if (artist.getBio() == null && artist.getDiscogsId() != null) {
                artist.setBio(discogsClient.artistFacts(artist.getDiscogsId()));
            }
            Cache.put(artist);
        }
        return artist;
    }

    public ArtistList findArtist(String query){
        ArtistList artist = client.searchArtist(query);
        return artist;
    }

}
