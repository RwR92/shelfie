package org.example.shelfie.book;

import java.util.List;

// The outer object of Open Library's /search.json response
public record OpenLibrarySearchResponse(List<OpenLibraryDoc> docs) {
}
