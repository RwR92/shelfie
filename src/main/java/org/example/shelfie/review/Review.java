package org.example.shelfie.review;

import jakarta.persistence.*;
import org.example.shelfie.auth.AppUser;
import org.example.shelfie.book.Book;

import java.time.LocalDateTime;

@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"book_id", "author_id"}))
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private int rating;
    @Column(length = 2000)
    private String text;
    @Column(nullable = false)
    private LocalDateTime createdAt;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "book_id")
    private Book book;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "author_id")
    private AppUser author;

    protected Review() {
    }

    public Review(Book book, AppUser author, int rating, String text) {
        this.book = book;
        this.author = author;
        this.rating = rating;
        this.text = text;
        this.createdAt = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public int getRating() {
        return rating;
    }

    public String getText() {
        return text;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public Book getBook() {
        return book;
    }

    public AppUser getAuthor() {
        return author;
    }
}
