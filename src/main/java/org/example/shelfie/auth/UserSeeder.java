package org.example.shelfie.auth;

import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(1)
public class UserSeeder implements CommandLineRunner {

    private final UserRepository userRepository;

    public UserSeeder(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        if(userRepository.findByUsername(CurrentUserService.TEST_USERNAME).isEmpty()) {
            userRepository.save(new AppUser(CurrentUserService.TEST_USERNAME));
        }
    }
}
