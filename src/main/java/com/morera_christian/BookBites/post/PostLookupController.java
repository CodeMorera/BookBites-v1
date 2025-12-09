package com.morera_christian.BookBites.post;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import com.morera_christian.BookBites.prompt.Prompt;
import com.morera_christian.BookBites.prompt.PromptRepository;

@RestController// returns json not html
@RequestMapping("/api/posts")// uses that address as base
public class PostLookupController {

    private final PromptRepository promptRepository;
    private final PostRepository postRepository;

    public PostLookupController(PostRepository postRepository, 
                                PromptRepository promptRepository){
        this.postRepository = postRepository;
        this.promptRepository = promptRepository;
    }
    //Get /api/posts/{id} -> to fetch a single post
    @GetMapping("/{id}")
    public Post getPostByid(@PathVariable Long id){
        return postRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "post not found"));
    }

    // Put /api/posts/{id} -> update an existing post
    @PutMapping("/{id}")
    public Post updatePost(@PathVariable Long id,
                           @Valid @RequestBody PostCreateRequest request){
        Post existing = postRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Post not found"));
        Prompt prompt = null;
        if(request.getPromptId() != null){
            prompt = promptRepository.findById(request.getPromptId())
                     .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.BAD_REQUEST, "Prompt not found"));
        }
        existing.setPrompt(prompt);

        existing.setAuthorName(request.getAuthorName());
        existing.setHeadline(request.getHeadline());
        existing.setContent(request.getContent());
        existing.setRating(request.getRating());

        return postRepository.save(existing);
    }

    // Delete a post
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletePost(@PathVariable Long id){
        if (!postRepository.existsById(id)){
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Post not found");
        }
        postRepository.deleteById(id);
    }
}
