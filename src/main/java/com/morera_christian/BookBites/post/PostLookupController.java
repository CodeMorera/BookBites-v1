package com.morera_christian.BookBites.post;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/posts")
public class PostLookupController {
    private final PostRepository postRepository;

    public PostLookupController(PostRepository postRepository){
        this.postRepository = postRepository;
    }
    @GetMapping("/{id}")
    public Post getPostByid(@PathVariable Long id){
        return postRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "post not found"));
    }
}
