package com.emilancius.top5.album;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.Instant;

public class AlbumEntitySpec {

    @Test
    public void createsAlbumInstance() {
        Instant instant = Instant.now();
        Assertions.assertEquals(
                new Album(0L, 8L, "", "", instant),
                new AlbumEntity(0L, 8L, "", "", instant).toAlbum()
        );
    }
}
