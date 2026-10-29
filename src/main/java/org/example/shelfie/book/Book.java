package org.example.shelfie.book;

import jakarta.persistence.*;
import org.example.shelfie.review.Review;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String title;
    @Column(nullable = false)
    private String author;
    private Integer publishYear;
    @OneToMany(mappedBy = "book")
    private List<Review> reviews = new ArrayList<>();

    protected Book() {
    }

    public Book(String title, String author, Integer publishYear) {
        this.title = title;
        this.author = author;
        this.publishYear = publishYear;
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public Integer getPublishYear() {
        return publishYear;
    }

    public List<Review> getReviews() {
        return reviews;
    }
}
