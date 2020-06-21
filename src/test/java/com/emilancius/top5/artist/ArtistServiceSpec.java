package com.emilancius.top5.artist;

import com.emilancius.top5.itunes.ITunesArtist;
import com.emilancius.top5.itunes.ITunesArtistSearchService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;

import java.util.Optional;

public class ArtistServiceSpec {

    private ArtistRepository repository;
    private ITunesArtistSearchService searchService;
    private ArtistService artistService;

    @BeforeEach
    public void setup() {
        repository = Mockito.mock(ArtistRepository.class);
        searchService = Mockito.mock(ITunesArtistSearchService.class);
        artistService = new ArtistService(repository, searchService);
    }

    @Test
    public void noArtistId_producesIllegalArgumentException() {
        Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> artistService.fetchArtistById(null)
        );
    }

    @Test
    public void artistExistsByArtistId_returnsArtist() {
        Optional<ArtistEntity> artist = Optional.of(new ArtistEntity(0L, "", ""));
        Mockito.when(repository.findById(ArgumentMatchers.anyLong())).thenReturn(artist);
        Assertions.assertEquals(artist, artistService.fetchArtistById(0L));
    }

    @Test
    public void noArtistExistsInRepository_fetchesArtistFromITunes_returnsAndSavesArtist() {
        Mockito
                .when(repository.findById(ArgumentMatchers.anyLong()))
                .thenReturn(Optional.empty());
        Mockito
                .when(searchService.searchForArtistById(ArgumentMatchers.anyLong()))
                .thenReturn(Optional.of(new ITunesArtist(0L, "", "")));
        Mockito
                .when(repository.save(ArgumentMatchers.any(ArtistEntity.class)))
                .thenReturn(new ArtistEntity(0L, "", ""));

        Optional<ArtistEntity> artist = artistService.fetchArtistById(0L);

        Assertions.assertEquals(Optional.of(new ArtistEntity(0L, "", "")), artist);
    }

    @Test
    public void noArtistExistsInRepository_cannotFindArtistInITunes_returnsEmptyOptional() {
        Mockito
                .when(repository.findById(ArgumentMatchers.anyLong()))
                .thenReturn(Optional.empty());
        Mockito
                .when(searchService.searchForArtistById(ArgumentMatchers.anyLong()))
                .thenReturn(Optional.empty());

        Optional<ArtistEntity> artist = artistService.fetchArtistById(0L);

        Assertions.assertEquals(Optional.empty(), artist);
    }
}
