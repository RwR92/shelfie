package org.example.shelfie.book;

import org.example.shelfie.review.Review;

import java.time.LocalDateTime;
import java.util.List;
import java.util.OptionalDouble;

public record BookDetailDto(Long id, String title, String author, Integer publishYear, Double averageRating,
                            List<ReviewSummary> reviews) {
    public record ReviewSummary(Long id, int rating, String text, String username, LocalDateTime createdAt) {
    }

    static BookDetailDto from(Book book) {
        List<ReviewSummary> reviews = book.getReviews().stream()
                .map(review -> new ReviewSummary(
                        review.getId(),
                        review.getRating(),
                        review.getText(),
                        review.getAuthor().getUsername(),
                        review.getCreatedAt()
                ))
                .toList();

        OptionalDouble average = book.getReviews().stream()
                .mapToInt(Review::getRating)
                .average();

        return new BookDetailDto(
                book.getId(),
                book.getTitle(),
                book.getAuthor(),
                book.getPublishYear(),
                average.isPresent() ? average.getAsDouble() : null,
                reviews
        );
    }
}
