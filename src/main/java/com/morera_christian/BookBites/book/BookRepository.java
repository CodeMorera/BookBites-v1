package com.morera_christian.BookBites.book;

import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book, Long> {
    boolean existsByTitleIgnoreCaseAndAuthorIgnoreCase(String title, String author);
}


