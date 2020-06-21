package com.emilancius.top5.artist.favourite;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@EqualsAndHashCode
@ToString
public final class UserFavouriteArtistRequest {

    private final Long userId;
    private final Long artistId;

    @JsonCreator
    public UserFavouriteArtistRequest(
            @JsonProperty("userId") final Long userId,
            @JsonProperty("artistId") final Long artistId
    ) {
        this.userId = userId;
        this.artistId = artistId;
    }
}
