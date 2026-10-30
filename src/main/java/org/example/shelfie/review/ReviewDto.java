package org.example.shelfie.review;

import java.time.LocalDateTime;

public record ReviewDto(Long id, Long bookId, int rating, String text, String username, LocalDateTime createdAt) {

    static ReviewDto from(Review review) {
        return new ReviewDto(
                review.getId(),
                review.getBook().getId(),
                review.getRating(),
                review.getText(),
                review.getAuthor().getUsername(),
                review.getCreatedAt());
    }
}
