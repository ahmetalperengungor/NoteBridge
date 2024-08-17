package di5.data.model;

import di5.data.enums.MediaType;

public class Image extends BaseEntity {
    private String id;
    private String url;
    private String owner;
    private MediaType mediaType;

    public Image() {
        id = null;
        url = null;
        mediaType = null;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public MediaType getMediaType() {
        return mediaType;
    }

    public void setMediaType(MediaType mediaType) {
        this.mediaType = mediaType;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getOwner() {
        return owner;
    }
}
