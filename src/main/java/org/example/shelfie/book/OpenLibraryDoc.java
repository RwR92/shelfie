package org.example.shelfie.book;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record OpenLibraryDoc(String key, String title, @JsonProperty("author_name") List<String> authorNames,
                             @JsonProperty("first_publish_year") Integer firstPublishYear,
                             @JsonProperty("cover_i") Integer coverId) {
}
