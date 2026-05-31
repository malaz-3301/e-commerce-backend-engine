package com.saea.bookecommerce.service;

import com.saea.bookecommerce.dto.ReviewRequest;
import com.saea.bookecommerce.exception.ResourceNotFoundException;
import com.saea.bookecommerce.model.Book;
import com.saea.bookecommerce.model.Review;
import com.saea.bookecommerce.model.User;
import com.saea.bookecommerce.repository.ReviewRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final UserService userService;
    private final BookService bookService;

    public ReviewService(ReviewRepository reviewRepository, UserService userService, BookService bookService) {
        this.reviewRepository = reviewRepository;
        this.userService = userService;
        this.bookService = bookService;
    }

    public List<Review> findAll() {
        return reviewRepository.findAll();
    }

    public Review findById(Long id) {
        return reviewRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Review not found"));
    }

    public Review create(ReviewRequest request) {
        Review review = new Review();
        applyRequest(review, request);
        return reviewRepository.save(review);
    }

    public Review update(Long id, ReviewRequest request) {
        Review review = findById(id);
        applyRequest(review, request);
        return reviewRepository.save(review);
    }

    public void delete(Long id) {
        Review review = findById(id);
        reviewRepository.delete(review);
    }

    private void applyRequest(Review review, ReviewRequest request) {
        User user = userService.findById(request.getUserId());
        Book book = bookService.findById(request.getBookId());
        review.setUser(user);
        review.setBook(book);
        review.setRating(request.getRating());
        review.setComment(request.getComment());
    }
}
