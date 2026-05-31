package com.saea.bookecommerce.service;

import com.saea.bookecommerce.dto.BookRequest;
import com.saea.bookecommerce.exception.ResourceNotFoundException;
import com.saea.bookecommerce.model.Book;
import com.saea.bookecommerce.model.Category;
import com.saea.bookecommerce.repository.BookRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BookServiceTest {

    @Mock
    private BookRepository bookRepository;

    @Mock
    private CategoryService categoryService;

    @InjectMocks
    private BookService bookService;

    private Category category;
    private Book book;
    private BookRequest request;

    @BeforeEach
    void setUp() {
        category = new Category();
        category.setId(1L);
        category.setName("Programming");

        book = new Book();
        book.setId(1L);
        book.setTitle("Clean Code");
        book.setAuthor("Robert Martin");
        book.setDescription("Software craftsmanship book");
        book.setPrice(new BigDecimal("20.00"));
        book.setStockQuantity(10);
        book.setCategory(category);

        request = new BookRequest();
        request.setTitle("Clean Code");
        request.setAuthor("Robert Martin");
        request.setDescription("Software craftsmanship book");
        request.setPrice(new BigDecimal("20.00"));
        request.setStockQuantity(10);
        request.setCategoryId(1L);
    }

    @Test
    void findAllReturnsBooks() {
        when(bookRepository.findAll()).thenReturn(List.of(book));

        List<Book> books = bookService.findAll();

        assertThat(books).hasSize(1);
        assertThat(books.getFirst().getTitle()).isEqualTo("Clean Code");
    }

    @Test
    void findByIdReturnsBookWhenExists() {
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));

        Book result = bookService.findById(1L);

        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getCategory().getName()).isEqualTo("Programming");
    }

    @Test
    void findByIdThrowsWhenBookDoesNotExist() {
        when(bookRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> bookService.findById(99L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Book not found");
    }

    @Test
    void createBook() {
        when(categoryService.findById(1L)).thenReturn(category);
        when(bookRepository.save(org.mockito.ArgumentMatchers.any(Book.class))).thenAnswer(invocation -> {
            Book saved = invocation.getArgument(0);
            saved.setId(1L);
            return saved;
        });

        Book created = bookService.create(request);

        assertThat(created.getId()).isEqualTo(1L);
        assertThat(created.getTitle()).isEqualTo("Clean Code");
        assertThat(created.getCategory()).isEqualTo(category);
        verify(bookRepository).save(org.mockito.ArgumentMatchers.any(Book.class));
    }

    @Test
    void updateBook() {
        request.setTitle("Clean Code Updated");
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        when(categoryService.findById(1L)).thenReturn(category);
        when(bookRepository.save(book)).thenReturn(book);

        Book updated = bookService.update(1L, request);

        assertThat(updated.getTitle()).isEqualTo("Clean Code Updated");
        verify(bookRepository).save(book);
    }

    @Test
    void deleteBook() {
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));

        bookService.delete(1L);

        verify(bookRepository).delete(book);
    }
}
