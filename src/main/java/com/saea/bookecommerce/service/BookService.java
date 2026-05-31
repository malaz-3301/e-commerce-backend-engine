package com.saea.bookecommerce.service;

import com.saea.bookecommerce.dto.BookRequest;
import com.saea.bookecommerce.exception.ResourceNotFoundException;
import com.saea.bookecommerce.model.Book;
import com.saea.bookecommerce.model.Category;
import com.saea.bookecommerce.repository.BookRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookService {

    private final BookRepository bookRepository;
    private final CategoryService categoryService;

    public BookService(BookRepository bookRepository, CategoryService categoryService) {
        this.bookRepository = bookRepository;
        this.categoryService = categoryService;
    }

    public List<Book> findAll() {
        return bookRepository.findAll();
    }

    public Book findById(Long id) {
        return bookRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Book not found"));
    }

    public Book create(BookRequest request) {
        Book book = new Book();
        applyRequest(book, request);
        return bookRepository.save(book);
    }

    public Book update(Long id, BookRequest request) {
        Book book = findById(id);
        applyRequest(book, request);
        return bookRepository.save(book);
    }

    public void delete(Long id) {
        Book book = findById(id);
        bookRepository.delete(book);
    }

    private void applyRequest(Book book, BookRequest request) {
        Category category = categoryService.findById(request.getCategoryId());
        book.setTitle(request.getTitle());
        book.setAuthor(request.getAuthor());
        book.setDescription(request.getDescription());
        book.setPrice(request.getPrice());
        book.setStockQuantity(request.getStockQuantity());
        book.setCategory(category);
    }
}
