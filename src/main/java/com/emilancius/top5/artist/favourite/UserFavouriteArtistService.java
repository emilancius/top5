package com.emilancius.top5.artist.favourite;

import com.emilancius.top5.itunes.ITunesArtist;
import com.emilancius.top5.itunes.ITunesArtistSearchService;
import com.emilancius.top5.prerequisites.Prerequisites;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserFavouriteArtistService {

    private final UserFavouriteArtistRepository userFavouriteArtistRepository;
    private final ITunesArtistSearchService artistSearchService;

    public UserFavouriteArtistService(
            final UserFavouriteArtistRepository userFavouriteArtistRepository,
            final ITunesArtistSearchService artistSearchService
    ) {
        this.userFavouriteArtistRepository = userFavouriteArtistRepository;
        this.artistSearchService = artistSearchService;
    }

    public Optional<Long> fetchUserFavouriteArtistId(final Long userId) {
        Prerequisites.existingArgument(userId, "Argument \"userId\" cannot be null");
        return userFavouriteArtistRepository
                .findByUserId(userId)
                .map(UserFavouriteArtistEntity::getArtistId);
    }

    public void setUserFavouriteArtist(final Long userId, final Long artistId) {
        Prerequisites.existingArgument(userId, "Argument \"userId\" cannot be null");
        Prerequisites.existingArgument(artistId, "Argument \"artistId\" cannot be null");
        Optional<UserFavouriteArtistEntity> existingFavouriteArtist = userFavouriteArtistRepository.findByUserId(userId);

        if (existingFavouriteArtist.isPresent() && existingFavouriteArtist.get().getArtistId().equals(artistId)) {
            return; // User's favourite artist does not change - ignore
        }

        // BEFORE setting user's favourite artist, check if it exists in iTunes services in order to avoid
        // any issues, regarding artist's top albums synchronisation.
        checkArtistExistsByArtistId(artistId);

        UserFavouriteArtistEntity userFavouriteArtist = existingFavouriteArtist
                .orElseGet(() -> {
                    UserFavouriteArtistEntity other = new UserFavouriteArtistEntity();
                    other.setUserId(userId);
                    return other;
                });
        userFavouriteArtist.setArtistId(artistId);
        userFavouriteArtistRepository.save(userFavouriteArtist);
    }

    private void checkArtistExistsByArtistId(final Long artistId) {
        Optional<ITunesArtist> artist = artistSearchService.searchForArtistById(artistId);

        if (artist.isEmpty()) {
            throw new IllegalArgumentException("Artist (artistId=" + artistId + ") does not exist in iTunes store");
        }
    }
}
