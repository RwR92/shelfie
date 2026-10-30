package org.example.shelfie.shelf;

import jakarta.persistence.*;
import org.example.shelfie.book.Book;

import java.time.LocalDateTime;

@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"shelf_id", "book_id"}))
public class ShelfEntry {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDateTime addedAt;

    private LocalDateTime finishedAt;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "shelf_id")
    private Shelf shelf;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "book_id")
    private Book book;

    protected  ShelfEntry() {}
    public ShelfEntry(Shelf shelf, Book book) {
        this.shelf = shelf;
        this.book = book;
        this.addedAt = LocalDateTime.now();
    }

    public void moveTo(Shelf newShelf) {
        this.shelf = newShelf;
        this.finishedAt = newShelf.getType() == ShelfType.READ ? LocalDateTime.now() : null;
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getAddedAt() {
        return addedAt;
    }

    public LocalDateTime getFinishedAt() {
        return finishedAt;
    }

    public Shelf getShelf() {
        return shelf;
    }

    public Book getBook() {
        return book;
    }
}
