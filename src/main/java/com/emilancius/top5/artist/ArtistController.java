package com.emilancius.top5.artist;

import com.emilancius.top5.album.Album;
import com.emilancius.top5.album.AlbumEntity;
import com.emilancius.top5.album.AlbumService;
import com.emilancius.top5.artist.favourite.UserFavouriteArtistRequest;
import com.emilancius.top5.artist.favourite.UserFavouriteArtistService;
import com.emilancius.top5.itunes.ITunesArtist;
import com.emilancius.top5.itunes.ITunesArtistSearchService;
import com.emilancius.top5.itunes.exception.ITunesResponseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping(
        value = "/artists",
        produces = MediaType.APPLICATION_JSON_VALUE
)
public class ArtistController {

    private static final Logger logger = LoggerFactory.getLogger(ArtistController.class);

    private final ITunesArtistSearchService artistSearchService;
    private final UserFavouriteArtistService userFavouriteArtistService;
    private final AlbumService albumService;

    public ArtistController(
            final ITunesArtistSearchService artistSearchService,
            final UserFavouriteArtistService userFavouriteArtistService,
            final AlbumService albumService
    ) {
        this.artistSearchService = artistSearchService;
        this.userFavouriteArtistService = userFavouriteArtistService;
        this.albumService = albumService;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public List<Artist> searchArtistsByName(
            @RequestParam("name") final String name,
            @RequestParam(value = "limit", required = false) final Integer limit
    ) {
        return artistSearchService
                .searchForArtistsByName(name, limit)
                .stream()
                .map(ITunesArtist::toArtist)
                .collect(Collectors.toList());
    }

    @GetMapping("/favourite/albums")
    public ResponseEntity<?> fetchUserFavouriteArtistTopAlbums(@RequestHeader final Long userId) {
        Optional<Long> userFavouriteArtistId = userFavouriteArtistService.fetchUserFavouriteArtistId(userId);

        if (userFavouriteArtistId.isEmpty()) {
            return new ResponseEntity<>("User's \"" + userId + "\" favourite artist is not set", HttpStatus.NOT_FOUND);
        } else {
            List<Album> albums = albumService
                    .fetchAlbumsByArtistId(userFavouriteArtistId.get())
                    .stream()
                    .map(AlbumEntity::toAlbum)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(albums);
        }
    }

    @PostMapping("/favourite")
    @ResponseStatus(HttpStatus.CREATED)
    public void setUserFavouriteArtist(@RequestBody final UserFavouriteArtistRequest request) {
        userFavouriteArtistService.setUserFavouriteArtist(request.getUserId(), request.getArtistId());
    }

    @ExceptionHandler({
            ITunesResponseException.class,
            Exception.class
    })
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public ExceptionResponse onITunesResponseExceptionOrUnexpectedException(final ITunesResponseException exception) {
        return onException(exception);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ExceptionResponse onIllegalArgumentException(final IllegalArgumentException exception) {
        return onException(exception);
    }

    private ExceptionResponse onException(final Exception exception) {
        String message = exception.getMessage();
        logger.error(message, exception);
        return new ExceptionResponse(message);
    }
}
