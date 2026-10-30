package org.example.shelfie.book;

import org.example.shelfie.common.NotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class BookService {
    private final BookRepository bookRepository;
    private final OpenLibraryClient openLibraryClient;

    BookService(BookRepository bookRepository, OpenLibraryClient openLibraryClient) {
        this.bookRepository = bookRepository;
        this.openLibraryClient = openLibraryClient;
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
    public BookDetailDto findById(Long id) {
        return bookRepository.findById(id)
                .map(BookDetailDto::from)
                .orElseThrow(() -> new NotFoundException("Book " + id + " not found"));
    }

    public List<OpenLibraryBookDto> search(String query) {
        return openLibraryClient.search(query).stream()
                .map(OpenLibraryBookDto::from)
                .toList();
    }

    public ImportResult importBook(String olKey) {
        Optional<Book> existing = bookRepository.findByOpenLibraryKey(olKey);
        if (existing.isPresent()) {
            return new ImportResult(BookDto.from(existing.get()), false);
        }

        OpenLibraryDoc doc = openLibraryClient.findByKey(olKey)
                .orElseThrow(() -> new NotFoundException("No book with key " + olKey + " on Open Library"));

        OpenLibraryBookDto data = OpenLibraryBookDto.from(doc);
        Book saved = bookRepository.save(new Book(
                data.title(),
                data.author(),
                data.publishYear(),
                data.olKey(),
                data.coverUrl()
        ));

        return new ImportResult(BookDto.from(saved), true);
    }

    @Transactional(readOnly = true)
    public Book getEntity(Long id){
        return bookRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Book " + id + " not found"));
    }
}
