package org.example.shelfie.review;

import org.example.shelfie.auth.AppUser;
import org.example.shelfie.auth.CurrentUserService;
import org.example.shelfie.book.Book;
import org.example.shelfie.book.BookService;
import org.example.shelfie.common.ConflictException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final CurrentUserService currentUserService;
    private final BookService bookService;

    public ReviewService(ReviewRepository reviewRepository, CurrentUserService currentUserService, BookService bookService) {
        this.reviewRepository = reviewRepository;
        this.currentUserService = currentUserService;
        this.bookService = bookService;
    }

    @Transactional(readOnly = true)
    public List<Integer> findMyRatings() {
        AppUser user = currentUserService.getCurrentUser();

        return reviewRepository.findByAuthor(user).stream()
                .map(Review::getRating)
                .toList();
    }

    @Transactional
    public ReviewDto createReview(Long bookId, CreateReviewRequest request) {
        AppUser user = currentUserService.getCurrentUser();
        Book book = bookService.getEntity(bookId);

        if (reviewRepository.existsByBookAndAuthor(book, user)) {
            throw new ConflictException("You have already reviewed '" + book.getTitle() + "'");
        }
        Review saved = reviewRepository.save(new Review(book, user, request.rating(), request.text()));

        return ReviewDto.from(saved);
    }
}
