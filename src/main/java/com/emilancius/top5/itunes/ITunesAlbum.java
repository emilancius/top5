package com.emilancius.top5.itunes;

import com.emilancius.top5.album.AlbumEntity;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.time.Instant;

@Getter
@EqualsAndHashCode(callSuper = false)
@ToString
public final class ITunesAlbum extends ITunesEntry {

    private final Long id;
    private final Long artistId;
    private final String name;
    private final String genre;
    private final Instant released;

    @JsonCreator
    public ITunesAlbum(
            @JsonProperty("collectionId") final Long id,
            @JsonProperty("artistId") final Long artistId,
            @JsonProperty("collectionName") final String name,
            @JsonProperty("primaryGenreName") final String genre,
            @JsonProperty("releaseDate") final Instant released
    ) {
        this.id = id;
        this.artistId = artistId;
        this.name = name;
        this.genre = genre;
        this.released = released;
    }

    public AlbumEntity toAlbumEntity() {
        return new AlbumEntity(id, artistId, name, genre, released);
    }
}
