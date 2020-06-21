package com.emilancius.top5.itunes;

import java.util.List;
import java.util.Optional;

public interface ITunesArtistSearchService {

    List<ITunesArtist> searchForArtistsByName(final String name, final Integer limit);

    Optional<ITunesArtist> searchForArtistById(final Long id);
}
