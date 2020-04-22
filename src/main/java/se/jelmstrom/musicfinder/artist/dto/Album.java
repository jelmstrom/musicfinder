package se.jelmstrom.musicfinder.artist.dto;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;

import java.util.ArrayList;
import java.util.List;
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Album {
    private String id;
    private String title;
    private String released;
    @JsonProperty("images")
    private List<String> images = new ArrayList<>();


    public Album() {
    }

    public Album(String id, String title, String released) {
        this.id = id;
        this.title = title;
        this.released = released;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @JsonGetter("released")
    public String getReleased() {
        return released;
    }

    @JsonSetter("first-release-date")
    public void setReleased(String released) {
        this.released = released;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }
}
