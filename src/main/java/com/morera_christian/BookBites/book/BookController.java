package com.morera_christian.BookBites.book;

import org.springframework.data.convert.ReadingConverter;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/books")
public class BookController {

    private final BookRepository repo;

    public BookController(BookRepository repo){
        this.repo = repo;
    }

    //Get /api/books -> list all books
    @GetMapping
    public List<Book> getAllBooks(){
        return repo.findAll();
    }

    // GET /api/books/{id} -> get a single book
    @GetMapping("/{id}")
    public Optional<Book> getBookById(@PathVariable Long id){
        return repo.findById(id);
    }
}
