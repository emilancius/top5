package com.emilancius.top5.album;

import com.emilancius.top5.artist.ArtistEntity;
import com.emilancius.top5.artist.ArtistService;
import com.emilancius.top5.itunes.ITunesAlbum;
import com.emilancius.top5.itunes.ITunesAlbumSearchService;
import com.emilancius.top5.prerequisites.Prerequisites;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AlbumService {

    private final Integer synchronisationEntriesCount;
    private final Duration synchronisationFrequency;

    private final AlbumRepository albumRepository;
    private final ArtistService artistService;
    private final ITunesAlbumSearchService albumSearchService;

    public AlbumService(
            @Value("${service.settings.sync.count:5}") final Integer synchronisationEntriesCount,
            @Value("${service.settings.sync.frequency:1d}") final Duration synchronisationFrequency,
            final AlbumRepository albumRepository,
            final ArtistService artistService,
            final ITunesAlbumSearchService albumSearchService
    ) {
        this.synchronisationEntriesCount = synchronisationEntriesCount;
        this.synchronisationFrequency = synchronisationFrequency;
        this.albumRepository = albumRepository;
        this.artistService = artistService;
        this.albumSearchService = albumSearchService;
    }

    public List<AlbumEntity> fetchAlbumsByArtistId(final Long artistId) {
        Prerequisites.existingArgument(artistId, "Argument \"artistId\" cannot be null");
        return artistService
                .fetchArtistById(artistId)
                .map(this::fetchLatestAlbumsByArtist)
                .orElse(Collections.emptyList());
    }

    private List<AlbumEntity> fetchLatestAlbumsByArtist(final ArtistEntity artist) {
        Long artistId = artist.getId();
        List<AlbumEntity> albums;

        if (checkShouldUpdateAlbums(artist)) {
            albums = updateArtistTopAlbums(artistId);
            artist.setAlbumsLastUpdated(Instant.now());
            artistService.save(artist);
        } else {
            albums = albumRepository.findByArtistId(artistId);
        }

        return albums;
    }

    private List<AlbumEntity> updateArtistTopAlbums(final Long artistId) {
        List<AlbumEntity> albums = albumSearchService
                .searchForAlbumsByArtistId(artistId, synchronisationEntriesCount)
                .stream()
                .map(ITunesAlbum::toAlbumEntity)
                .collect(Collectors.toList());
        albumRepository.deleteByArtistId(artistId);
        albumRepository.saveAll(albums);
        return albums;
    }

    private boolean checkShouldUpdateAlbums(final ArtistEntity artist) {
        Instant albumsLastUpdated = artist.getAlbumsLastUpdated();
        return albumsLastUpdated == null || albumsLastUpdated.isBefore(Instant.now().minusSeconds(synchronisationFrequency.toSeconds()));
    }
}
