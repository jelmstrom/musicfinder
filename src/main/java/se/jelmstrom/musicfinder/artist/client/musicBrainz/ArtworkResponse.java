package se.jelmstrom.musicfinder.artist.client.musicBrainz;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class ArtworkResponse {

    private String release;
    @JsonProperty("images")
    private List<Artwork> images;

    public ArtworkResponse() {
    }

    public String getRelease() {
        return release;
    }

    public void setRelease(String release) {
        this.release = release;
    }

    public List<Artwork> getImages() {
        return images;
    }

    public void setImages(List<Artwork> images) {
        this.images = images;
    }
}
