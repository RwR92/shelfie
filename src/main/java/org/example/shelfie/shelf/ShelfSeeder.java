package org.example.shelfie.shelf;

import org.example.shelfie.auth.AppUser;
import org.example.shelfie.auth.CurrentUserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Order(2)
public class ShelfSeeder implements CommandLineRunner {

    private final ShelfRepository shelfRepository;
    private final CurrentUserService currentUserService;

    ShelfSeeder(ShelfRepository shelfRepository, CurrentUserService currentUserService) {
        this.shelfRepository = shelfRepository;
        this.currentUserService = currentUserService;
    }

    @Override
    public void run(String... args) throws Exception {
        AppUser user = currentUserService.getCurrentUser();

        if(!shelfRepository.findByOwner(user).isEmpty()){
            return;
        }

        shelfRepository.saveAll(List.of(
                new Shelf("Want to read", ShelfType.WANT_TO_READ, user),
                new Shelf("Reading", ShelfType.READING, user),
                new Shelf("Read", ShelfType.READ, user)
        ));
    }
}
