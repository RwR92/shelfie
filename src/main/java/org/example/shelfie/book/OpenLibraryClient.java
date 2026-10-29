package org.example.shelfie.book;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.time.Duration;
import java.util.List;

@Component
public class OpenLibraryClient {

    private static final String FIELDS = "key,title,author_name,first_publish_year,cover_i";

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

    List<OpenLibraryDoc> search(String query) {
        OpenLibrarySearchResponse response = restClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/search.json")
                        .queryParam("q", query)
                        .queryParam("fields", FIELDS)
                        .queryParam("limit", 10)
                        .build())
                .retrieve()
                .body(OpenLibrarySearchResponse.class);

        if (response == null || response.docs() == null) {
            return List.of();
        }
        return response.docs();
    }
}
