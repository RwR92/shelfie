package org.example.shelfie.review;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ReviewController.class)
class ReviewControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ReviewService reviewService;

    @Test
    void validReviewReturns201() throws Exception {
        when(reviewService.createReview(eq(3L), any(CreateReviewRequest.class)))
                .thenReturn(new ReviewDto(10L, 3L, 4, "Good read", "testuser", LocalDateTime.now()));

        mockMvc.perform(post("/api/books/3/reviews")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ \"rating\": 4, \"text\": \"Good read\" }"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(10))
                .andExpect(jsonPath("$.rating").value(4))
                .andExpect(jsonPath("$.username").value("testuser"));
    }

    @Test
    void ratingAboveFiveIsRejectedBeforeTheServiceIsCalled() throws Exception {
        mockMvc.perform(post("/api/books/3/reviews")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ \"rating\": 7 }"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors.rating").exists());

        verify(reviewService, never()).createReview(any(), any());
    }

    @Test
    void missingRatingIsRejected() throws Exception {
        mockMvc.perform(post("/api/books/3/reviews")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ }"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors.rating").exists());

        verify(reviewService, never()).createReview(any(), any());
    }
}