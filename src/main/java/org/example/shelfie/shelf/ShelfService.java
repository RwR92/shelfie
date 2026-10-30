package org.example.shelfie.shelf;

import org.example.shelfie.auth.AppUser;
import org.example.shelfie.auth.CurrentUserService;
import org.example.shelfie.book.Book;
import org.example.shelfie.book.BookService;
import org.example.shelfie.common.ConflictException;
import org.example.shelfie.common.NotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ShelfService {

    private final ShelfRepository shelfRepository;
    private final CurrentUserService currentUserService;
    private final ShelfEntryRepository shelfEntryRepository;
    private final BookService bookService;

    ShelfService(ShelfRepository shelfRepository, CurrentUserService currentUserService, ShelfEntryRepository shelfEntryRepository, BookService bookService) {
        this.shelfRepository = shelfRepository;
        this.currentUserService = currentUserService;
        this.shelfEntryRepository = shelfEntryRepository;
        this.bookService = bookService;
    }

    @Transactional(readOnly = true)
    public List<ShelfDto> findMyShelves() {
        AppUser user = currentUserService.getCurrentUser();

        return shelfRepository.findByOwner(user).stream()
                .map(ShelfDto::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<ShelfEntryDto> findEntries(Long shelfId) {
        return getOwnShelf(shelfId).getEntries().stream()
                .map(ShelfEntryDto::from)
                .toList();
    }

    @Transactional
    public ShelfEntryDto addEntry(Long shelfId, AddEntryRequest request) {
        Shelf shelf = getOwnShelf(shelfId);
        Book book = bookService.getEntity(request.bookId());

        if (shelfEntryRepository.existsByShelfAndBook(shelf, book)) {
            throw new ConflictException("'" + book.getTitle() + "' is already on this shelf");
        }

        ShelfEntry saved = shelfEntryRepository.save(new ShelfEntry(shelf, book));
        return ShelfEntryDto.from(saved);
    }

    @Transactional
    public ShelfEntryDto moveEntry(Long entryId, MoveEntryRequest request) {
        ShelfEntry entry = getOwnEntry(entryId);
        Shelf target = getOwnShelf(request.shelfId());

        if (entry.getShelf().getId().equals(target.getId())) {
            return ShelfEntryDto.from(entry);
        }
        if (shelfEntryRepository.existsByShelfAndBook(target, entry.getBook())) {
            throw new ConflictException("'" + entry.getBook().getTitle() + "' is already on that shelf");
        }

        entry.moveTo(target);
        return ShelfEntryDto.from(entry);
    }

    @Transactional
    public void deleteEntry(Long entryId) {
        ShelfEntry entry = getOwnEntry(entryId);
        entry.getShelf().removeEntry(entry);
    }

    private Shelf getOwnShelf(Long shelfId) {
        AppUser user = currentUserService.getCurrentUser();
        return shelfRepository.findById(shelfId)
                .filter(s -> s.getOwner().getId().equals(user.getId()))
                .orElseThrow(() -> new NotFoundException("Shelf " + shelfId + " not found"));
    }

    private ShelfEntry getOwnEntry(Long entryId) {
        AppUser user = currentUserService.getCurrentUser();
        return shelfEntryRepository.findById(entryId)
                .filter(e -> e.getShelf().getOwner().getId().equals(user.getId()))
                .orElseThrow(() -> new NotFoundException("Entry " + entryId + " not found"));
    }
}
