package com.emilancius.top5.album;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.time.Instant;

@Getter
@EqualsAndHashCode
@ToString
public final class Album {

    private final Long id;
    private final Long artistId;
    private final String name;
    private final String genre;
    private final Instant released;

    @JsonCreator
    public Album(
            @JsonProperty("id") final Long id,
            @JsonProperty("artistId") final Long artistId,
            @JsonProperty("name") final String name,
            @JsonProperty("genre") final String genre,
            @JsonProperty("released") final Instant released
    ) {
        this.id = id;
        this.artistId = artistId;
        this.name = name;
        this.genre = genre;
        this.released = released;
    }
}
