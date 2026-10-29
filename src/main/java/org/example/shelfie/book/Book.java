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
    @Column(unique = true)
    private String openLibraryKey;

    private String coverUrl;

    protected Book() {
    }

    public Book(String title, String author, Integer publishYear) {
        this(title, author, publishYear, null, null);
    }

    public Book(String title, String author, Integer publishYear, String openLibraryKey, String coverUrl) {
        this.title = title;
        this.author = author;
        this.publishYear = publishYear;
        this.openLibraryKey = openLibraryKey;
        this.coverUrl = coverUrl;
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

    public String getOpenLibraryKey() {
        return openLibraryKey;
    }

    public String getCoverUrl() {
        return coverUrl;
    }
}
