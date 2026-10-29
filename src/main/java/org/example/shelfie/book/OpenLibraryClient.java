package org.example.shelfie.book;

import org.example.shelfie.common.ExternalServiceException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import java.time.Duration;
import java.util.List;
import java.util.Optional;

@Component
class OpenLibraryClient {

    private static final String FIELDS = "key,title,author_name,first_publish_year,cover_i";
    private static final int SEARCH_LIMIT = 10;

    private final RestClient restClient;

    OpenLibraryClient(@Value("${openlibrary.base-url}") String baseUrl) {
        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setConnectTimeout(Duration.ofSeconds(3));
        requestFactory.setReadTimeout(Duration.ofSeconds(5));

        this.restClient = RestClient.builder()
                .baseUrl(baseUrl)
                .requestFactory(requestFactory)
                .build();
    }

    // Free-text search, e.g. "tolkien"
    List<OpenLibraryDoc> search(String query) {
        return fetch(query, SEARCH_LIMIT);
    }

    // Exactly one book by its Open Library key, e.g. "OL27482W"
    Optional<OpenLibraryDoc> findByKey(String olKey) {
        return fetch("key:/works/" + olKey, 1).stream().findFirst();
    }

    private List<OpenLibraryDoc> fetch(String query, int limit) {
        try {
            OpenLibrarySearchResponse response = restClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/search.json")
                            .queryParam("q", query)
                            .queryParam("fields", FIELDS)
                            .queryParam("limit", limit)
                            .build())
                    .retrieve()
                    .body(OpenLibrarySearchResponse.class);

            if (response == null || response.docs() == null) {
                return List.of();
            }
            return response.docs();

        } catch (RestClientException ex) {
            throw new ExternalServiceException("Open Library is not available right now", ex);
        }
    }
}