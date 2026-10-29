package org.example.shelfie.shelf;

import org.example.shelfie.auth.AppUser;
import org.example.shelfie.auth.CurrentUserService;
import org.example.shelfie.book.BookService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Order(3)
public class ShelfSeeder implements CommandLineRunner {

    private final ShelfRepository shelfRepository;
    private final CurrentUserService currentUserService;
    private final BookService bookService;

    ShelfSeeder(ShelfRepository shelfRepository, CurrentUserService currentUserService, BookService bookService) {
        this.shelfRepository = shelfRepository;
        this.currentUserService = currentUserService;
        this.bookService = bookService;
    }

    @Override
    public void run(String... args) {
        AppUser user = currentUserService.getCurrentUser();

        if (!shelfRepository.findByOwner(user).isEmpty()) {
            return;
        }

        Shelf read = new Shelf("Read", ShelfType.READ, user);
        bookService.findAllEntities().stream()
                .limit(2)
                .forEach(book -> read.addEntry(new ShelfEntry(read, book)));

        shelfRepository.saveAll(List.of(
                new Shelf("Want to read", ShelfType.WANT_TO_READ, user),
                new Shelf("Reading", ShelfType.READING, user),
                read
        ));
    }
}
