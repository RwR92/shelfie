package org.example.shelfie.book;

public record BookDto(Long id, String title, String author, Integer publishYear, String coverUrl) {
    static BookDto from(Book book) {
        return new BookDto(book.getId(), book.getTitle(), book.getAuthor(), book.getPublishYear(), book.getCoverUrl());
    }
}
