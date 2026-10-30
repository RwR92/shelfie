package org.example.shelfie.shelf;

import org.example.shelfie.book.Book;
import org.springframework.data.jpa.repository.JpaRepository;

interface ShelfEntryRepository extends JpaRepository<ShelfEntry, Long> {

    boolean existsByShelfAndBook(Shelf shelf, Book book);
}
