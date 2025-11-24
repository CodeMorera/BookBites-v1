package com.morera_christian.BookBites.post;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {

    // All post for a given book
    List<Post> findByBookId(Long bookId);
}
