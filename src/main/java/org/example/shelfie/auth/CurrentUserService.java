package org.example.shelfie.auth;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CurrentUserService {
     static final String TEST_USERNAME = "testuser";
    private final UserRepository userRepository;

    public CurrentUserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional(readOnly = true)
    public AppUser getCurrentUser() {
        return userRepository.findByUsername(TEST_USERNAME)
                .orElseThrow(() -> new IllegalStateException("Test user '" + TEST_USERNAME + "' not found"));
    }
}
