package com.emilancius.top5.artist;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@EqualsAndHashCode
@ToString
public final class Artist {

    private final Long id;
    private final String name;
    private final String genre;

    @JsonCreator
    public Artist(
            @JsonProperty("id") final Long id,
            @JsonProperty("name") final String name,
            @JsonProperty("genre") final String genre
    ) {
        this.id = id;
        this.name = name;
        this.genre = genre;
    }
}
