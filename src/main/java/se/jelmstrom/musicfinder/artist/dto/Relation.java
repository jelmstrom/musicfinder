package se.jelmstrom.musicfinder.artist.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Relation {

    private RelationUrl url;
    private String type;
    @JsonProperty("type-id")
    private String typeId;

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


    public String getTypeId() {
        return typeId;
    }

    public void setTypeId(String typeId) {
        this.typeId = typeId;
    }
}
