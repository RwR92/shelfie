package org.example.shelfie.shelf;

import org.example.shelfie.book.Book;

import java.time.LocalDateTime;

public record ShelfEntryDto(Long id, Long bookId, String title, String author, LocalDateTime addedAt, LocalDateTime finishedAt) {

    static ShelfEntryDto from(ShelfEntry entry) {
        Book book = entry.getBook();
        return new ShelfEntryDto(entry.getId(),
                book.getId(),
                book.getTitle(),
                book.getAuthor(),
                entry.getAddedAt(),
                entry.getFinishedAt());
    }
}
