package com.morera_christian.BookBites;

import com.morera_christian.BookBites.book.Book;
import com.morera_christian.BookBites.book.BookRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class BookBitesApplication {

	public static void main(String[] args) {
        SpringApplication.run(BookBitesApplication.class, args);
    }

    @Bean
    public CommandLineRunner seedData(BookRepository repo) {
        return args -> {
            if (repo.count() == 0) {
                repo.save(new Book("Dune", "Frank Herbert"));
                System.out.println("Saved test book!");
            }
        };
    }

}
