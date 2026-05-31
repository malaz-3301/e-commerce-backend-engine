package com.saea.bookecommerce.repository;

import com.saea.bookecommerce.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book, Long> {
}
