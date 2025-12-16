package com.morera_christian.BookBites;

import com.morera_christian.BookBites.book.Book;
import com.morera_christian.BookBites.book.BookRepository;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;


import java.io.InputStream;
import java.util.List;

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
           
                ObjectMapper mapper = new ObjectMapper();
                try(InputStream is = getClass().getResourceAsStream("/static/Books.json")) {
                     if (is == null) {
                    throw new IllegalStateException("Could not find /static/Books.json on the classpath");
                    }

                    List<Book> books = mapper.readValue(is, new TypeReference<List<Book>>() {});
                    int inserted = 0;

                    for (Book b: books){
                        if (b.getTitle() == null || b.getAuthor() == null) continue;

                            String title = b.getTitle().trim();
                            String author = b.getAuthor().trim();
                        boolean exists = repo.existsByTitleIgnoreCaseAndAuthorIgnoreCase(
                            title, author);

                        if (!exists) {
                            Book toSave = new Book();
                            toSave.setTitle(title);
                            toSave.setAuthor(author);
                            repo.save(toSave);
                            inserted++;
                            
                        }
                    }
                    System.out.println("Seed complete. Inserted " + inserted + " new books; skipped " + (books.size() - inserted) + ".");

                }
            
        };
    }

}
