package org.example.shelfie.stats;

import org.example.shelfie.shelf.ShelfType;

import java.util.List;
import java.util.Map;

public record StatsDto(Map<ShelfType, Integer> booksPerShelf, Double averageRatingGiven, List<String> topAuthors) {
}
