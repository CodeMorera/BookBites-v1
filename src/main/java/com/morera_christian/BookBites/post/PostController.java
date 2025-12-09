package com.morera_christian.BookBites.post;

import com.morera_christian.BookBites.book.Book;
import com.morera_christian.BookBites.book.BookRepository;
import com.morera_christian.BookBites.prompt.Prompt;
import com.morera_christian.BookBites.prompt.PromptRepository;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/books/{bookId}/posts")
public class PostController {

        private final PostRepository postRepository;
        private final BookRepository bookRepository;
        private final PromptRepository promptRepository; 

        public PostController(PostRepository postRepository,
                              BookRepository bookRepository,
                              PromptRepository promptRepository){
            this.postRepository = postRepository;
            this.bookRepository = bookRepository;
            this.promptRepository = promptRepository;
        }
        // Getting all posts for a specific book
        @GetMapping
        public List<Post> getPostsForBook(@PathVariable Long bookId){
            // verify book exists
            if(!bookRepository.existsById(bookId)){
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Book not found");
            }
            return postRepository.findByBookId(bookId);
        }

        @PostMapping
        @ResponseStatus(HttpStatus.CREATED)
        public Post createPostForBook(@PathVariable Long bookId,
                                      @Valid @RequestBody PostCreateRequest request){
            //Look up the book this post is about
            Book book = bookRepository.findById(bookId)
                    .orElseThrow(() -> new ResponseStatusException(
                            HttpStatus.NOT_FOUND, "Book not found"));
            Prompt prompt = null;
            if(request.getPromptId() != null){
                prompt = promptRepository.findById(request.getPromptId())
                        .orElseThrow(()-> new ResponseStatusException(
                            HttpStatus.BAD_REQUEST, "Prompt not found"));
            }
            

            //Build a new Post from the request + book
            Post post = new Post(
                    request.getAuthorName(),
                    request.getHeadline(),
                    request.getContent(),
                    request.getRating(),
                    book
            );

            post.setPrompt(prompt);
            //Save it to the database
            return postRepository.save(post);
        }
    }

