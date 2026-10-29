package org.example.shelfie.shelf;

import org.example.shelfie.auth.AppUser;
import org.example.shelfie.book.Book;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;
import org.springframework.dao.DataIntegrityViolationException;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@DataJpaTest
public class ShelfRepositoryTest {

    @Autowired
    private ShelfRepository shelfRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    void savingAShelfAlsoSavesItsEntries() {
        AppUser user = entityManager.persist(new AppUser("alice"));
        Book book1 = entityManager.persist(new Book("Dune", "Frank Herbert", 1965));
        Book book2 = entityManager.persist(new Book("1984", "George Orwell", 1949));

        Shelf shelf = new Shelf("Read", ShelfType.READ, user);
        shelf.addEntry(new ShelfEntry(shelf, book1));
        shelf.addEntry(new ShelfEntry(shelf, book2));
        shelfRepository.save(shelf);

        entityManager.flush();
        entityManager.clear();

        Shelf loaded = shelfRepository.findById(shelf.getId()).orElseThrow();
        assertThat(loaded.getEntries()).hasSize(2);
    }

    @Test
    void theSameBookCannotBeOnTheSameShelfTwice() {
        AppUser user = entityManager.persist(new AppUser("bob"));
        Book book = entityManager.persist(new Book("Dune", "Frank Herbert", 1965));

        Shelf shelf = new Shelf("Read", ShelfType.READ, user);
        shelf.addEntry(new ShelfEntry(shelf, book));
        shelf.addEntry(new ShelfEntry(shelf, book));

        assertThatThrownBy(() -> shelfRepository.saveAndFlush(shelf))
                .isInstanceOf(DataIntegrityViolationException.class);
    }
}
