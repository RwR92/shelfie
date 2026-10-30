package org.example.shelfie.shelf;

import jakarta.validation.Valid;
import org.example.shelfie.book.BookService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/entries")
public class EntryController {

    private final ShelfService shelfService;

    public EntryController(ShelfService shelfService, BookService bookService) {
        this.shelfService = shelfService;
    }


    @PatchMapping("/{id}")
    public ShelfEntryDto moveEntry(@PathVariable Long id, @Valid @RequestBody MoveEntryRequest request) {
        return shelfService.moveEntry(id, request);
    }


    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteEntry(@PathVariable Long id) {
        shelfService.deleteEntry(id);
    }
}
