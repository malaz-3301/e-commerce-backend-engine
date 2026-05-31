package com.saea.bookecommerce.service;

import com.saea.bookecommerce.dto.OrderItemRequest;
import com.saea.bookecommerce.dto.OrderRequest;
import com.saea.bookecommerce.exception.ResourceNotFoundException;
import com.saea.bookecommerce.model.Book;
import com.saea.bookecommerce.model.Order;
import com.saea.bookecommerce.model.OrderStatus;
import com.saea.bookecommerce.model.User;
import com.saea.bookecommerce.repository.OrderRepository;
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
class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private UserService userService;

    @Mock
    private BookService bookService;

    @InjectMocks
    private OrderService orderService;

    private User user;
    private Book book;
    private Order order;
    private OrderRequest request;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setName("Malaz Ahmad");

        book = new Book();
        book.setId(1L);
        book.setTitle("Clean Code");
        book.setPrice(new BigDecimal("20.00"));
        book.setStockQuantity(10);

        order = new Order();
        order.setId(1L);
        order.setUser(user);
        order.setStatus(OrderStatus.PENDING);
        order.setTotalAmount(BigDecimal.ZERO);

        OrderItemRequest itemRequest = new OrderItemRequest();
        itemRequest.setBookId(1L);
        itemRequest.setQuantity(2);

        request = new OrderRequest();
        request.setUserId(1L);
        request.setItems(List.of(itemRequest));
    }

    @Test
    void findAllReturnsOrders() {
        when(orderRepository.findAll()).thenReturn(List.of(order));

        List<Order> orders = orderService.findAll();

        assertThat(orders).hasSize(1);
        assertThat(orders.getFirst().getUser()).isEqualTo(user);
    }

    @Test
    void findByIdReturnsOrderWhenExists() {
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));

        Order result = orderService.findById(1L);

        assertThat(result.getId()).isEqualTo(1L);
    }

    @Test
    void findByIdThrowsWhenOrderDoesNotExist() {
        when(orderRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> orderService.findById(99L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Order not found");
    }

    @Test
    void createOrderCalculatesTotalAndReducesStock() {
        when(userService.findById(1L)).thenReturn(user);
        when(bookService.findById(1L)).thenReturn(book);
        when(orderRepository.save(org.mockito.ArgumentMatchers.any(Order.class))).thenAnswer(invocation -> {
            Order saved = invocation.getArgument(0);
            saved.setId(1L);
            return saved;
        });

        Order created = orderService.create(request);

        assertThat(created.getId()).isEqualTo(1L);
        assertThat(created.getItems()).hasSize(1);
        assertThat(created.getTotalAmount()).isEqualByComparingTo("40.00");
        assertThat(book.getStockQuantity()).isEqualTo(8);
        verify(orderRepository).save(org.mockito.ArgumentMatchers.any(Order.class));
    }

    @Test
    void createOrderThrowsWhenStockIsNotEnough() {
        book.setStockQuantity(1);
        when(userService.findById(1L)).thenReturn(user);
        when(bookService.findById(1L)).thenReturn(book);

        assertThatThrownBy(() -> orderService.create(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Not enough stock for book: Clean Code");
    }

    @Test
    void updateStatus() {
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        when(orderRepository.save(order)).thenReturn(order);

        Order updated = orderService.updateStatus(1L, OrderStatus.CONFIRMED);

        assertThat(updated.getStatus()).isEqualTo(OrderStatus.CONFIRMED);
        verify(orderRepository).save(order);
    }

    @Test
    void deleteOrder() {
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));

        orderService.delete(1L);

        verify(orderRepository).delete(order);
    }
}
