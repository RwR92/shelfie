package org.example.shelfie.review;

import org.example.shelfie.auth.AppUser;
import org.example.shelfie.book.Book;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

interface ReviewRepository extends JpaRepository<Review, Long> {

    List<Review> findByAuthor(AppUser author);

    boolean existsByBookAndAuthor(Book book, AppUser author);
}
