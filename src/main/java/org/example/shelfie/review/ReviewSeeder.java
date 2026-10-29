package org.example.shelfie.review;

import org.example.shelfie.auth.AppUser;
import org.example.shelfie.auth.CurrentUserService;
import org.example.shelfie.book.Book;
import org.example.shelfie.book.BookService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Order(4)
class ReviewSeeder implements CommandLineRunner {

    private final ReviewRepository reviewRepository;
    private final CurrentUserService currentUserService;
    private final BookService bookService;

    ReviewSeeder(ReviewRepository reviewRepository, CurrentUserService currentUserService, BookService bookService) {
        this.reviewRepository = reviewRepository;
        this.currentUserService = currentUserService;
        this.bookService = bookService;
    }

    @Override
    public void run(String... args) throws Exception {

        if (reviewRepository.count() > 0) {
            return;
        }

        AppUser user = currentUserService.getCurrentUser();
        List<Book> books = bookService.findAllEntities();

        reviewRepository.saveAll(List.of(
                new Review(books.get(0), user, 5, "A classic adventure."),
                new Review(books.get(1), user, 4, null)
        ));
    }
}
