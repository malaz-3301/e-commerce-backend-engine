package com.saea.bookecommerce.service;

import com.saea.bookecommerce.dto.OrderItemRequest;
import com.saea.bookecommerce.dto.OrderRequest;
import com.saea.bookecommerce.exception.ResourceNotFoundException;
import com.saea.bookecommerce.model.Book;
import com.saea.bookecommerce.model.Order;
import com.saea.bookecommerce.model.OrderItem;
import com.saea.bookecommerce.model.OrderStatus;
import com.saea.bookecommerce.model.User;
import com.saea.bookecommerce.repository.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final UserService userService;
    private final BookService bookService;

    public OrderService(OrderRepository orderRepository, UserService userService, BookService bookService) {
        this.orderRepository = orderRepository;
        this.userService = userService;
        this.bookService = bookService;
    }

    public List<Order> findAll() {
        return orderRepository.findAll();
    }

    public Order findById(Long id) {
        return orderRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Order not found"));
    }

    @Transactional
    public Order create(OrderRequest request) {
        User user = userService.findById(request.getUserId());
        Order order = new Order();
        order.setUser(user);

        BigDecimal total = BigDecimal.ZERO;
        for (OrderItemRequest itemRequest : request.getItems()) {
            Book book = bookService.findById(itemRequest.getBookId());
            if (book.getStockQuantity() < itemRequest.getQuantity()) {
                throw new IllegalArgumentException("Not enough stock for book: " + book.getTitle());
            }

            book.setStockQuantity(book.getStockQuantity() - itemRequest.getQuantity());

            OrderItem item = new OrderItem();
            item.setOrder(order);
            item.setBook(book);
            item.setQuantity(itemRequest.getQuantity());
            item.setPrice(book.getPrice());
            order.getItems().add(item);

            total = total.add(book.getPrice().multiply(BigDecimal.valueOf(itemRequest.getQuantity())));
        }

        order.setTotalAmount(total);
        return orderRepository.save(order);
    }

    public Order updateStatus(Long id, OrderStatus status) {
        Order order = findById(id);
        order.setStatus(status);
        return orderRepository.save(order);
    }

    public void delete(Long id) {
        Order order = findById(id);
        orderRepository.delete(order);
    }
}
