package com.emilancius.top5.album;

import com.emilancius.top5.artist.ArtistEntity;
import com.emilancius.top5.artist.ArtistService;
import com.emilancius.top5.itunes.ITunesAlbumSearchService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class AlbumServiceSpec {

    private AlbumRepository albumRepository;
    private ArtistService artistService;
    private ITunesAlbumSearchService albumSearchService;
    private AlbumService albumService;

    @BeforeEach
    public void setup() {
        albumRepository = Mockito.mock(AlbumRepository.class);
        artistService = Mockito.mock(ArtistService.class);
        albumSearchService = Mockito.mock(ITunesAlbumSearchService.class);
        albumService = new AlbumService(
                5,
                Duration.ofDays(1),
                albumRepository,
                artistService,
                albumSearchService
        );
    }

    @Test
    public void noArtistId_producesIllegalArgumentException() {
        Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> albumService.fetchAlbumsByArtistId(null)
        );
    }

    @Test
    public void noArtistFound_returnsEmptyList() {
        Mockito
                .when(artistService.fetchArtistById(ArgumentMatchers.anyLong()))
                .thenReturn(Optional.empty());

        Assertions.assertIterableEquals(
                Collections.emptyList(),
                albumService.fetchAlbumsByArtistId(3L)
        );
    }

    @Test
    public void artistFound_updatesAlbums_returnsUpdatedArtistAlbums() {
        Instant lastUpdated = LocalDateTime
                .parse(
                        "1999-01-01T00:00:00.000Z",
                        DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
                )
                .atZone(ZoneId.of("UTC"))
                .toInstant();
        Mockito
                .when(artistService.fetchArtistById(ArgumentMatchers.anyLong()))
                .thenReturn(Optional.of(new ArtistEntity(3L, "", "", lastUpdated)));
        Mockito
                .when(albumSearchService.searchForAlbumsByArtistId(3L, 5))
                .thenReturn(Collections.emptyList());

        List<AlbumEntity> albums = albumService.fetchAlbumsByArtistId(3L);

        Mockito.verify(albumRepository, Mockito.times(1)).deleteByArtistId(3L);
        Mockito.verify(albumRepository, Mockito.times(1)).saveAll(ArgumentMatchers.anyIterable());
        Mockito.verify(artistService, Mockito.times(1)).save(ArgumentMatchers.any(ArtistEntity.class));

        Assertions.assertIterableEquals(Collections.emptyList(), albums);

    }

    @Test
    public void artistFound_doesntUpdateAlbums_returnsArtistAlbums() {
        Mockito
                .when(artistService.fetchArtistById(ArgumentMatchers.anyLong()))
                .thenReturn(Optional.of(new ArtistEntity(3L, "", "", Instant.now())));
        Mockito
                .when(albumRepository.findByArtistId(3L))
                .thenReturn(Collections.emptyList());

        List<AlbumEntity> albums = albumService.fetchAlbumsByArtistId(3L);

        Mockito
                .verify(albumSearchService, Mockito.never())
                .searchForAlbumsByArtistId(ArgumentMatchers.anyLong(), ArgumentMatchers.anyInt());

        Assertions.assertIterableEquals(Collections.emptyList(), albums);
    }
}
