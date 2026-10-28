package org.example.shelfie.book;

import jakarta.persistence.*;

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

    protected Book(){}
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
}
