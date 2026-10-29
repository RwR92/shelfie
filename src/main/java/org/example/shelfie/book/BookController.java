package org.example.shelfie.book;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/books")
public class BookController {

    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping
    public List<BookDto> getAll() {
        return bookService.findAll();
    }

    @GetMapping("/{id}")
    public BookDetailDto getBookById(@PathVariable Long id) {
        return bookService.findById(id);
    }

    @GetMapping("/search")
    public List<OpenLibraryBookDto> search(@RequestParam String q) {
        return bookService.search(q);
    }

    @PostMapping("/import/{olKey:OL\\d+W}")
    public ResponseEntity<BookDto> importBook(@PathVariable String olKey) {
        ImportResult result = bookService.importBook(olKey);
        HttpStatus status = result.created() ? HttpStatus.CREATED : HttpStatus.OK;
        return ResponseEntity.status(status).body(result.book());
    }
}
