package com.emilancius.top5.artist;

import com.emilancius.top5.itunes.ITunesArtist;
import com.emilancius.top5.itunes.ITunesArtistSearchService;
import com.emilancius.top5.prerequisites.Prerequisites;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ArtistService {

    private final ArtistRepository artistRepository;
    private final ITunesArtistSearchService artistSearchService;

    public ArtistService(
            final ArtistRepository artistRepository,
            final ITunesArtistSearchService artistSearchService
    ) {
        this.artistRepository = artistRepository;
        this.artistSearchService = artistSearchService;
    }

    public Optional<ArtistEntity> fetchArtistById(final Long artistId) {
        Prerequisites.existingArgument(artistId, "Argument \"artistId\" cannot be null");
        Optional<ArtistEntity> artist = artistRepository.findById(artistId);

        if (artist.isPresent()) {
            return artist;
        }

        return artistSearchService
                .searchForArtistById(artistId)
                .map(ITunesArtist::toArtistEntity)
                .map(this::save);
    }

    public ArtistEntity save(final ArtistEntity artist) {
        return artistRepository.save(artist);
    }
}
