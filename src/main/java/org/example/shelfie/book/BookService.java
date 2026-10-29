package org.example.shelfie.book;

import org.example.shelfie.common.NotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class BookService {
    private final BookRepository bookRepository;

    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @Transactional(readOnly = true)
    public List<BookDto> findAll() {
        return bookRepository.findAll()
                .stream()
                .map(BookDto::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<Book> findAllEntities() {
        return bookRepository.findAll();
    }

    @Transactional(readOnly = true)
    public BookDetailDto findById(long id) {
        return bookRepository.findById(id)
                .map(BookDetailDto::from)
                .orElseThrow(() -> new NotFoundException("Book " + id + " not found"));
    }
}
