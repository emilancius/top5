package com.emilancius.top5.itunes;

import com.emilancius.top5.album.AlbumEntity;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.Instant;

public class ITunesAlbumSpec {

    @Test
    public void createsAlbumEntityInstance() {
        Instant instant = Instant.now();
        Assertions.assertEquals(
                new AlbumEntity(0L, 8L, "", "", instant),
                new ITunesAlbum(0L, 8L, "", "", instant).toAlbumEntity()
        );
    }
}
