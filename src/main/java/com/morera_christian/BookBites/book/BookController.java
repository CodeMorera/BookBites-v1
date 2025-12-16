package com.morera_christian.BookBites.book;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/books")
public class BookController {

    private final BookRepository bookRepository;

    public BookController(BookRepository bookRepository){
        this.bookRepository = bookRepository;
    }

    //Get /api/books -> list all books
    @GetMapping
    public List<Book> getAllBooks(){
        return bookRepository.findAll();
    }

    // GET /api/books/{id} -> get a single book
    @GetMapping("/{id}")
    public Optional<Book> getBookById(@PathVariable Long id){
        return bookRepository.findById(id);
    }

    // Create New Book
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Book creatBook(@RequestBody Book book){
        if(book.getTitle() == null || book.getTitle().isBlank()){
            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "Title is required"
            );
        }

        // author can be empty if someone doesnt remember it
        return bookRepository.save(book);
    }
}
