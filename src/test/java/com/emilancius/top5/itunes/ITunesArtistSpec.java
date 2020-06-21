package com.emilancius.top5.itunes;

import com.emilancius.top5.artist.Artist;
import com.emilancius.top5.artist.ArtistEntity;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ITunesArtistSpec {

    @Test
    public void createsArtistInstance() {
        Assertions.assertEquals(
                new Artist(0L, "", ""),
                new ITunesArtist(0L, "", "").toArtist()
        );
    }

    @Test
    public void createdArtistEntityInstance() {
        Assertions.assertEquals(
                new ArtistEntity(0L, "", ""),
                new ITunesArtist(0L, "", "").toArtistEntity()
        );
    }
}
