package com.emilancius.top5.itunes;

import com.emilancius.top5.artist.Artist;
import com.emilancius.top5.artist.ArtistEntity;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@EqualsAndHashCode(callSuper = false)
@ToString
public final class ITunesArtist extends ITunesEntry {

    private final Long id;
    private final String name;
    private final String genre;

    @JsonCreator
    public ITunesArtist(
            @JsonProperty("artistId") final Long id,
            @JsonProperty("artistName") final String name,
            @JsonProperty("primaryGenreName") final String genre
    ) {
        this.id = id;
        this.name = name;
        this.genre = genre;
    }

    public Artist toArtist() {
        return new Artist(id, name, genre);
    }

    public ArtistEntity toArtistEntity() {
        return new ArtistEntity(id, name, genre);
    }
}
