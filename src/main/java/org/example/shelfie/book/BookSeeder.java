package org.example.shelfie.book;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class BookSeeder implements CommandLineRunner {

    private final BookRepository bookRepository;

    public BookSeeder(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @Override
    public void run(String... args) throws Exception {

        if (bookRepository.count() > 0) {
            return;
        }
        bookRepository.saveAll(List.of(
                new Book("The Hobbit", "J.R.R. Tolkien", 1937),
                new Book("Dune", "Frank Herbert", 1965),
                new Book("Clean Code", "Robert C. Martin", 2008),
                new Book("The Pragmatic Programmer", "Andrew Hunt", 1999),
                new Book("Harry Potter and the Philosopher's Stone", "J.K. Rowling", null)));
    }
}
