package se.jelmstrom.musicfinder.artist.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ArtistBiography {
    private String profile;
    private String releases;
    private String uri;
    private String realName;


    public ArtistBiography() {
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public String getReleases() {
        return releases;
    }

    public void setReleases(String releases) {
        this.releases = releases;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }
}
