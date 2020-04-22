package se.jelmstrom.musicfinder.artist.dto;

public class Relation {

    private RelationUrl url;
    private String type;

    public Relation() {
    }

    public RelationUrl getUrl() {
        return url;
    }

    public void setUrl(RelationUrl url) {
        this.url = url;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
