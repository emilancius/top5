package com.emilancius.top5.itunes;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "wrapperType")
@JsonSubTypes({
        @JsonSubTypes.Type(name = "artist", value = ITunesArtist.class),
        @JsonSubTypes.Type(name = "collection", value = ITunesAlbum.class)
})
public abstract class ITunesEntry {

}
