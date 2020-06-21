package com.emilancius.top5.itunes;

import com.emilancius.top5.itunes.exception.ITunesResponseException;
import com.emilancius.top5.prerequisites.Prerequisites;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ITunesService implements ITunesArtistSearchService, ITunesAlbumSearchService {

    private final String itunesTarget;

    private final RestTemplate template;

    public ITunesService(
            @Value("${service.settings.itunes.target}") final String itunesTarget,
            final RestTemplate template
    ) {
        this.itunesTarget = itunesTarget;
        this.template = template;
    }

    @Override
    public List<ITunesAlbum> searchForAlbumsByArtistId(final Long artistId, final Integer limit) {
        Prerequisites.existingArgument(artistId, "Argument \"artistId\" cannot be null");
        String target = itunesTarget
                + "/lookup?entity=album&id="
                + artistId
                + (limit == null ? "" : ("&limit=" + limit));
        return executeHttpGetSearchRequest(target)
                .resultsAsStream()
                .filter(it -> it instanceof ITunesAlbum)
                .map(ITunesAlbum.class::cast)
                .collect(Collectors.toList());
    }

    @Override
    public List<ITunesArtist> searchForArtistsByName(final String name, final Integer limit) {
        Prerequisites.stringContainsText(name, "Argument \"name\" cannot be null or empty");
        String target = itunesTarget
                + "/search?entity=musicArtist&term="
                + name
                + (limit == null ? "" : ("&limit=" + limit));
        return executeHttpGetSearchRequest(target)
                .resultsAsStream()
                .map(ITunesArtist.class::cast)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<ITunesArtist> searchForArtistById(final Long id) {
        Prerequisites.existingArgument(id, "Argument \"id\" cannot be null");
        String target = itunesTarget
                + "/lookup?entity=musicArtist&limit=1&id="
                + id;
        ITunesSearchResponse response = executeHttpGetSearchRequest(target);
        return response.isEmpty() ? Optional.empty() : Optional.of((ITunesArtist) response.getResults().get(0));
    }

    private ITunesSearchResponse executeHttpGetSearchRequest(final String target) {
        try {
            return template.getForEntity(target, ITunesSearchResponse.class).getBody();
        } catch (HttpStatusCodeException exception) {
            String message = "Exception occurred on HTTP GET "
                    + target
                    + " invocation on "
                    + Instant.now().toString()
                    + ". Request resulted in response code: "
                    + exception.getRawStatusCode()
                    + ", and message: "
                    + exception.getMessage();
            throw new ITunesResponseException(message);
        }
    }
}
