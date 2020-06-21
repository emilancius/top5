package com.emilancius.top5.artist;

import com.emilancius.top5.album.Album;
import com.emilancius.top5.album.AlbumEntity;
import com.emilancius.top5.album.AlbumService;
import com.emilancius.top5.artist.favourite.UserFavouriteArtistService;
import com.emilancius.top5.itunes.ITunesArtist;
import com.emilancius.top5.itunes.ITunesArtistSearchService;
import com.emilancius.top5.itunes.exception.ITunesResponseException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ArtistController.class)
public class ArtistControllerSpec {

    private static final String URI_SEARCH_ARTISTS = "/artists";
    private static final String URI_FETCH_FAVOURITE_ARTIST_ALBUMS = "/artists/favourite/albums";
    private static final String URI_SET_USER_FAVOURITE_ARTIST = "/artists/favourite";

    @Autowired
    private MockMvc mvc;

    @MockBean
    private ITunesArtistSearchService artistSearchService;

    @MockBean
    private UserFavouriteArtistService userFavouriteArtistService;

    @MockBean
    private AlbumService albumService;

    private ObjectMapper mapper;

    @BeforeEach
    public void setup() {
        mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);

    }

    @Test
    public void nameQueryParameterIsAbsent_returnsStatus400() throws Exception {
        mvc.perform(get(URI_SEARCH_ARTISTS)).andExpect(status().is(400));
    }

    @Test
    public void inputIsOK_returnsStatusOK_returnsArtistListAsJson() throws Exception {
        List<ITunesArtist> artists = new ArrayList<>();
        artists.add(new ITunesArtist(3L, "", ""));
        artists.add(new ITunesArtist(339L, "", ""));
        Mockito
                .when(artistSearchService.searchForArtistsByName(ArgumentMatchers.anyString(), ArgumentMatchers.anyInt()))
                .thenReturn(artists);

        String responseBody = mvc
                .perform(get(URI_SEARCH_ARTISTS).param("name", "Jean").param("limit", "5"))
                .andExpect(status().is(200))
                .andReturn()
                .getResponse()
                .getContentAsString();

        Artist[] responseContent = mapper.readValue(responseBody, Artist[].class);

        Assertions.assertIterableEquals(
                artists.stream().map(ITunesArtist::toArtist).collect(Collectors.toList()),
                Arrays.asList(responseContent)
        );
    }

    @Test
    public void inputIsOK_ITunesResponseExceptionOccurs_returnsStatus500() throws Exception {
        Mockito
                .when(artistSearchService.searchForArtistsByName(ArgumentMatchers.anyString(), ArgumentMatchers.eq(null)))
                .thenThrow(ITunesResponseException.class);
        mvc.perform(get(URI_SEARCH_ARTISTS).param("name", "Jean")).andExpect(status().isInternalServerError());
    }

    @Test
    public void requestHeaderUserIdIsAbsent_returnsStatus400() throws Exception {
        mvc.perform(get(URI_FETCH_FAVOURITE_ARTIST_ALBUMS)).andExpect(status().is(400));
    }

    @Test
    public void inputIsOK_noUsersFavouriteArtistSet_returnsStatus404() throws Exception {
        Mockito
                .when(userFavouriteArtistService.fetchUserFavouriteArtistId(ArgumentMatchers.anyLong()))
                .thenReturn(Optional.empty());

        mvc.perform(get(URI_FETCH_FAVOURITE_ARTIST_ALBUMS).header("userId", 356)).andExpect(status().is(404));
    }

    @Test
    public void inputIsOK_returnsStatus200_returnsUserFavouriteArtistAlbumsAsJson() throws Exception {
        List<AlbumEntity> albums = new ArrayList<>();
        albums.add(new AlbumEntity(3L, 345L, "", "", Instant.now()));
        albums.add(new AlbumEntity(7L, 345L, "", "", Instant.now()));
        Mockito
                .when(userFavouriteArtistService.fetchUserFavouriteArtistId(ArgumentMatchers.anyLong()))
                .thenReturn(Optional.of(345L));
        Mockito
                .when(albumService.fetchAlbumsByArtistId(ArgumentMatchers.anyLong()))
                .thenReturn(albums);

        String responseBody = mvc
                .perform(get(URI_FETCH_FAVOURITE_ARTIST_ALBUMS).header("userId", 356))
                .andExpect(status().is(200))
                .andReturn()
                .getResponse()
                .getContentAsString();

        Album[] responseContent = mapper.readValue(responseBody, Album[].class);

        Assertions.assertIterableEquals(
                albums.stream().map(AlbumEntity::toAlbum).collect(Collectors.toList()),
                Arrays.asList(responseContent)
        );
    }

    @Test
    public void noRequestBodyProvided_returnsStatus400() throws Exception {
        mvc.perform(post(URI_SET_USER_FAVOURITE_ARTIST)).andExpect(status().is(400));
    }

    @Test
    public void inputIsOK_returnsStatus201() throws Exception {
        Mockito
                .doNothing()
                .when(userFavouriteArtistService)
                .setUserFavouriteArtist(ArgumentMatchers.anyLong(), ArgumentMatchers.anyLong());

        String requestBody = "{\"userId\":356,\"artistId\":3}";
        mvc
                .perform(post(URI_SET_USER_FAVOURITE_ARTIST).content(requestBody).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
    }
}
