package org.example.shelfie.book;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

interface BookRepository extends JpaRepository<Book, Long> {

    Optional<Book> findByOpenLibraryKey(String openLibraryKey);
}
