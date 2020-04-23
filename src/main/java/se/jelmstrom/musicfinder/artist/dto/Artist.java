package se.jelmstrom.musicfinder.artist.dto;

import com.fasterxml.jackson.annotation.*;

import java.beans.Transient;
import java.util.ArrayList;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class Artist {
    private String name;
    private String id;
    private String gender;
    private Integer score;
    private String url;
    private List<Album> albums;
    private ArtistBiography bio;
    private String discogsId;
    private List<Relation> relations;

    public Artist() {
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @JsonGetter("albums")
    public List<Album> getAlbums() {
        return albums;
    }

    @JsonSetter("release-groups")
    public void setAlbums(List<Album> albums) {
        this.albums = albums;
    }

    public ArtistBiography getBio() {
        return bio;
    }

    public void setBio(ArtistBiography bio) {
        this.bio = bio;
    }
    @JsonIgnore
    public String getDiscogsId() {
        return discogsId;
    }

    public void setDiscogsId(String discogsId) {
        this.discogsId = discogsId;
    }
    @JsonIgnore
    public List<Relation> getRelations() {
        return relations;
    }
    @JsonSetter("relations")
    public void setRelations(List<Relation> relations) {
        this.relations = relations;
    }
}
