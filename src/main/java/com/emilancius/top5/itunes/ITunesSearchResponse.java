package com.emilancius.top5.itunes;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.List;
import java.util.stream.Stream;

@Getter
@EqualsAndHashCode
@ToString
public final class ITunesSearchResponse {

    private final List<ITunesEntry> results;

    @JsonCreator
    public ITunesSearchResponse(@JsonProperty("results") final List<ITunesEntry> results) {
        this.results = results;
    }

    public boolean isEmpty() {
        return results.isEmpty();
    }

    public Stream<ITunesEntry> resultsAsStream() {
        return results.stream();
    }
}
