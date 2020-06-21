package com.emilancius.top5.itunes;

import com.emilancius.top5.itunes.exception.ITunesResponseException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class ITunesServiceSpec {

    private RestTemplate template;
    private ITunesService iTunesService;

    @BeforeEach
    public void setup() {
        template = Mockito.mock(RestTemplate.class);
        iTunesService = new ITunesService("", template);
    }

    @Test
    public void noArtistId_producesIllegalArgumentException() {
        Assertions.assertAll(
                () -> Assertions.assertThrows(
                        IllegalArgumentException.class,
                        () -> iTunesService.searchForAlbumsByArtistId(null, null)
                ),
                () -> Assertions.assertThrows(
                        IllegalArgumentException.class,
                        () -> iTunesService.searchForArtistById(null)
                )
        );
    }

    @Test
    public void nameIsAbsent_producesIllegalArgumentException() {
        Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> iTunesService.searchForArtistsByName("", null)
        );
    }

    @Test
    public void executesSearchRequest_artistAlbumsFound_returnsAlbumsList() {
        List<ITunesEntry> results = new ArrayList<>();
        results.add(new ITunesArtist(3L, "", ""));
        results.add(new ITunesAlbum(100L, 3L, "", "", Instant.now()));
        ITunesSearchResponse response = new ITunesSearchResponse(results);
        Mockito
                .when(template.getForEntity(ArgumentMatchers.anyString(), ArgumentMatchers.eq(ITunesSearchResponse.class)))
                .thenReturn(new ResponseEntity<>(response, HttpStatus.OK));

        Assertions.assertEquals(1, iTunesService.searchForAlbumsByArtistId(3L, 5).size());
    }

    @Test
    public void executesSearchRequest_artistsFound_returnsArtistsList() {
        ITunesSearchResponse response = new ITunesSearchResponse(Collections.singletonList(new ITunesArtist(3L, "", "")));
        Mockito
                .when(template.getForEntity(ArgumentMatchers.anyString(), ArgumentMatchers.eq(ITunesSearchResponse.class)))
                .thenReturn(new ResponseEntity<>(response, HttpStatus.OK));

        Assertions.assertEquals(1, iTunesService.searchForArtistsByName("Jean", 5).size());
    }

    @Test
    public void executesSearchRequest_artistFound_returnsArtist() {
        ITunesArtist artist = new ITunesArtist(3L, "", "");
        ITunesSearchResponse response = new ITunesSearchResponse(Collections.singletonList(artist));
        Mockito
                .when(template.getForEntity(ArgumentMatchers.anyString(), ArgumentMatchers.eq(ITunesSearchResponse.class)))
                .thenReturn(new ResponseEntity<>(response, HttpStatus.OK));

        Assertions.assertEquals(Optional.of(artist), iTunesService.searchForArtistById(3L));
    }

    @Test
    public void executesSearchRequest_noArtistFound_returnsEmptyOptional() {
        ITunesSearchResponse response = new ITunesSearchResponse(Collections.emptyList());
        Mockito
                .when(template.getForEntity(ArgumentMatchers.anyString(), ArgumentMatchers.eq(ITunesSearchResponse.class)))
                .thenReturn(new ResponseEntity<>(response, HttpStatus.OK));

        Assertions.assertEquals(Optional.empty(), iTunesService.searchForArtistById(3L));
    }

    @Test
    public void executesSearchRequest_httpErrorOccurs_producesITunesResponseException() {
        Mockito
                .when(template.getForEntity(ArgumentMatchers.anyString(), ArgumentMatchers.eq(ITunesSearchResponse.class)))
                .thenThrow(HttpServerErrorException.class);

        Assertions.assertAll(
                () -> Assertions.assertThrows(
                        ITunesResponseException.class,
                        () -> iTunesService.searchForAlbumsByArtistId(3L, 5)
                ),
                () -> Assertions.assertThrows(
                        ITunesResponseException.class,
                        () -> iTunesService.searchForArtistsByName("Jean", 5)
                ),
                () -> Assertions.assertThrows(
                        ITunesResponseException.class,
                        () -> iTunesService.searchForArtistById(3L)
                )
        );
    }
}
