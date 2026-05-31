package com.saea.bookecommerce.service;

import com.saea.bookecommerce.dto.ReviewRequest;
import com.saea.bookecommerce.exception.ResourceNotFoundException;
import com.saea.bookecommerce.model.Book;
import com.saea.bookecommerce.model.Review;
import com.saea.bookecommerce.model.User;
import com.saea.bookecommerce.repository.ReviewRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ReviewServiceTest {

    @Mock
    private ReviewRepository reviewRepository;

    @Mock
    private UserService userService;

    @Mock
    private BookService bookService;

    @InjectMocks
    private ReviewService reviewService;

    private User user;
    private Book book;
    private Review review;
    private ReviewRequest request;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setName("Malaz Ahmad");

        book = new Book();
        book.setId(1L);
        book.setTitle("Clean Code");

        review = new Review();
        review.setId(1L);
        review.setUser(user);
        review.setBook(book);
        review.setRating(5);
        review.setComment("Excellent book");

        request = new ReviewRequest();
        request.setUserId(1L);
        request.setBookId(1L);
        request.setRating(5);
        request.setComment("Excellent book");
    }

    @Test
    void findAllReturnsReviews() {
        when(reviewRepository.findAll()).thenReturn(List.of(review));

        List<Review> reviews = reviewService.findAll();

        assertThat(reviews).hasSize(1);
        assertThat(reviews.getFirst().getRating()).isEqualTo(5);
    }

    @Test
    void findByIdReturnsReviewWhenExists() {
        when(reviewRepository.findById(1L)).thenReturn(Optional.of(review));

        Review result = reviewService.findById(1L);

        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getBook()).isEqualTo(book);
    }

    @Test
    void findByIdThrowsWhenReviewDoesNotExist() {
        when(reviewRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> reviewService.findById(99L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Review not found");
    }

    @Test
    void createReview() {
        when(userService.findById(1L)).thenReturn(user);
        when(bookService.findById(1L)).thenReturn(book);
        when(reviewRepository.save(org.mockito.ArgumentMatchers.any(Review.class))).thenAnswer(invocation -> {
            Review saved = invocation.getArgument(0);
            saved.setId(1L);
            return saved;
        });

        Review created = reviewService.create(request);

        assertThat(created.getId()).isEqualTo(1L);
        assertThat(created.getUser()).isEqualTo(user);
        assertThat(created.getBook()).isEqualTo(book);
        assertThat(created.getRating()).isEqualTo(5);
        verify(reviewRepository).save(org.mockito.ArgumentMatchers.any(Review.class));
    }

    @Test
    void updateReview() {
        request.setRating(4);
        request.setComment("Good book");
        when(reviewRepository.findById(1L)).thenReturn(Optional.of(review));
        when(userService.findById(1L)).thenReturn(user);
        when(bookService.findById(1L)).thenReturn(book);
        when(reviewRepository.save(review)).thenReturn(review);

        Review updated = reviewService.update(1L, request);

        assertThat(updated.getRating()).isEqualTo(4);
        assertThat(updated.getComment()).isEqualTo("Good book");
        verify(reviewRepository).save(review);
    }

    @Test
    void deleteReview() {
        when(reviewRepository.findById(1L)).thenReturn(Optional.of(review));

        reviewService.delete(1L);

        verify(reviewRepository).delete(review);
    }
}
