package org.example.shelfie.shelf;

import org.example.shelfie.auth.AppUser;
import org.example.shelfie.auth.CurrentUserService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class ShelfService {

    private final ShelfRepository shelfRepository;
    private final CurrentUserService currentUserService;

    public ShelfService(ShelfRepository shelfRepository, CurrentUserService currentUserService) {
        this.shelfRepository = shelfRepository;
        this.currentUserService = currentUserService;
    }

    @Transactional(readOnly = true)
    public List<ShelfDto> findMyShelves() {
        AppUser user = currentUserService.getCurrentUser();

        return shelfRepository.findByOwner(user).stream()
                .map(ShelfDto::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<ShelfEntryDto> findEntries(Long shelfId){
        AppUser user = currentUserService.getCurrentUser();

        Shelf shelf = shelfRepository.findById(shelfId)
                .filter(s -> s.getOwner().getId().equals(user.getId()))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Shelf not found"));

        return shelf.getEntries().stream()
                .map(ShelfEntryDto::from)
                .toList();
    }
}
