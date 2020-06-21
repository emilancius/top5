package com.emilancius.top5.itunes;

import java.util.List;

public interface ITunesAlbumSearchService {

    List<ITunesAlbum> searchForAlbumsByArtistId(final Long artistId, final Integer limit);
}
