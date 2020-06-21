package com.emilancius.top5.itunes;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

public class ITunesSearchResponseSpec {

    @Test
    public void noEntriesInITunesSearchResponse_returnsTrue() {
        List<ITunesEntry> entries = Collections.singletonList(new ITunesArtist(0L, "", ""));
        Assertions.assertFalse(new ITunesSearchResponse(entries).isEmpty());
    }

    @Test
    public void ITunesSearchResponseContainsEntries_returnsFalse() {
        Assertions.assertTrue(new ITunesSearchResponse(Collections.emptyList()).isEmpty());
    }
}
