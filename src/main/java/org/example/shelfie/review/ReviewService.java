package org.example.shelfie.review;

import org.example.shelfie.auth.AppUser;
import org.example.shelfie.auth.CurrentUserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final CurrentUserService currentUserService;

    public ReviewService(ReviewRepository reviewRepository, CurrentUserService currentUserService) {
        this.reviewRepository = reviewRepository;
        this.currentUserService = currentUserService;
    }

    @Transactional(readOnly = true)
    public List<Integer> findMyRatings(){
        AppUser user =  currentUserService.getCurrentUser();

        return reviewRepository.findByAuthor(user).stream()
                .map(Review::getRating)
                .toList();
    }
}
