package org.example.shelfie.stats;

import org.example.shelfie.review.ReviewService;
import org.example.shelfie.shelf.ShelfDto;
import org.example.shelfie.shelf.ShelfEntryDto;
import org.example.shelfie.shelf.ShelfService;
import org.example.shelfie.shelf.ShelfType;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.OptionalDouble;
import java.util.stream.Collectors;

@Service
public class StatsService {

    private final ShelfService shelfService;
    private final ReviewService reviewService;

    public StatsService(ShelfService shelfService, ReviewService reviewService) {
        this.shelfService = shelfService;
        this.reviewService = reviewService;
    }

    private Map<ShelfType, Integer> booksPerShelf(List<ShelfDto> shelves){
        return shelves.stream()
                .collect(Collectors.groupingBy(
                        ShelfDto::type,
                        Collectors.summingInt(ShelfDto::entryCount)
                ));
    }
    private Double averageRatingGiven(List<Integer> ratings) {
        OptionalDouble average = ratings.stream()
                .mapToInt(Integer::intValue)
                .average();

        return average.isPresent() ? average.getAsDouble() : null;
    }

    private List<String> topAuthors(List<ShelfDto> shelves) {
        Map<String, Long> booksPerAuthor = shelves.stream()
                .filter(shelf -> shelf.type() == ShelfType.READ)
                .flatMap(shelf -> shelfService.findEntries(shelf.id()).stream())
                .collect(Collectors.groupingBy(ShelfEntryDto::author, Collectors.counting()));

        return booksPerAuthor.entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                .limit(3)
                .map(Map.Entry::getKey)
                .toList();
    }

    public StatsDto getMyStats() {
        List<ShelfDto> shelves = shelfService.findMyShelves();
        List<Integer> ratings = reviewService.findMyRatings();

        return new StatsDto(
                booksPerShelf(shelves),
                averageRatingGiven(ratings),
                topAuthors(shelves)
        );
    }
}
