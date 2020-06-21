package com.emilancius.top5.artist.favourite;

import com.emilancius.top5.itunes.ITunesArtist;
import com.emilancius.top5.itunes.ITunesArtistSearchService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;

import java.util.Optional;

public class UserFavouriteArtistServiceSpec {

    private UserFavouriteArtistRepository userFavouriteArtistRepository;
    private ITunesArtistSearchService artistSearchService;
    private UserFavouriteArtistService userFavouriteArtistService;

    @BeforeEach
    public void setup() {
        userFavouriteArtistRepository = Mockito.mock(UserFavouriteArtistRepository.class);
        artistSearchService = Mockito.mock(ITunesArtistSearchService.class);
        userFavouriteArtistService = new UserFavouriteArtistService(userFavouriteArtistRepository, artistSearchService);
    }

    @Test
    public void noUserId_producesIllegalArgumentException() {
        Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> userFavouriteArtistService.fetchUserFavouriteArtistId(null)
        );
        Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> userFavouriteArtistService.setUserFavouriteArtist(null, 3L)
        );
    }

    @Test
    public void noFavouriteArtistInRepositoryByUserId_returnsEmptyOptional() {
        Mockito
                .when(userFavouriteArtistRepository.findByUserId(ArgumentMatchers.anyLong()))
                .thenReturn(Optional.empty());

        Assertions.assertEquals(Optional.empty(), userFavouriteArtistService.fetchUserFavouriteArtistId(0L));
    }

    @Test
    public void favouriteArtistExistsByUserId_returnsArtistIdOptional() {
        Long artistId = 5L;
        Mockito
                .when(userFavouriteArtistRepository.findByUserId(ArgumentMatchers.anyLong()))
                .thenReturn(Optional.of(new UserFavouriteArtistEntity(0L, 3L, artistId)));

        Assertions.assertEquals(Optional.of(artistId), userFavouriteArtistService.fetchUserFavouriteArtistId(3L));
    }

    @Test
    public void noArtistId_producesIllegalArgumentException() {
        Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> userFavouriteArtistService.setUserFavouriteArtist(0L, null)
        );
    }

    @Test
    public void sameUserArtistIdIsSetAlready_abortsInstantly() {
        Long userId = 3L;
        Long artistId = 5L;
        Mockito
                .when(userFavouriteArtistRepository.findByUserId(ArgumentMatchers.eq(userId)))
                .thenReturn(Optional.of(new UserFavouriteArtistEntity(0L, userId, artistId)));

        userFavouriteArtistService.setUserFavouriteArtist(userId, artistId);

        Mockito.verify(artistSearchService, Mockito.never()).searchForArtistById(artistId);
    }

    @Test
    public void noUserFavouriteArtistIsSet_checksArtistExistsInITunes_noArtistFound_producesIllegalArgumentException() {
        Mockito
                .when(userFavouriteArtistRepository.findByUserId(ArgumentMatchers.anyLong()))
                .thenReturn(Optional.empty());
        Mockito
                .when(artistSearchService.searchForArtistById(ArgumentMatchers.anyLong()))
                .thenReturn(Optional.empty());

        Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> userFavouriteArtistService.setUserFavouriteArtist(3L, 5L)
        );
    }

    @Test
    public void noUserFavouriteArtistIsSet_userFavouriteArtistIsSetAndSaved() {
        Long userId = 3L;
        Long artistId = 5L;

        Mockito
                .when(userFavouriteArtistRepository.findByUserId(ArgumentMatchers.anyLong()))
                .thenReturn(Optional.empty());
        Mockito
                .when(artistSearchService.searchForArtistById(ArgumentMatchers.anyLong()))
                .thenReturn(Optional.of(new ITunesArtist(artistId, "", "")));

        userFavouriteArtistService.setUserFavouriteArtist(userId, artistId);

        Mockito
                .verify(userFavouriteArtistRepository, Mockito.times(1))
                .save(ArgumentMatchers.eq(new UserFavouriteArtistEntity(null, userId, artistId)));
    }
}
