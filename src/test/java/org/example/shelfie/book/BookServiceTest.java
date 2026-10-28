package org.example.shelfie.book;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;


@SpringBootTest
public class BookServiceTest {

    @Autowired
    private BookService bookService;

    @Test
    void findAllReturnsSeededBooksAsDtos(){
        List<BookDto> books =  bookService.findAll();

        assertThat(books).hasSize(5);
        assertThat(books)
                .extracting(BookDto::title)
                .contains("The Hobbit", "Dune");
    }
}
