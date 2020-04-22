package se.jelmstrom.musicfinder.artist.dto;

import java.util.ArrayList;
import java.util.List;

public class ArtistList {
    private final List<Artist> artists;
    private int count;

    public ArtistList() {
        artists = new ArrayList<>();
    }

    public List<Artist> getArtists() {
        return artists;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

}
