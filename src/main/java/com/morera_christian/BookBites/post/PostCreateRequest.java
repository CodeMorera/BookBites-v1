package com.morera_christian.BookBites.post;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public class PostCreateRequest {

    @NotBlank
    private String authorName;

    @NotBlank
    private String headline;

    @NotBlank
    private String content;

    @Min(1)
    @Max(5)
    private Integer rating;

    private Long bookId;

    public Long getBookId() {
        return bookId;
    }
    public void setBookId(Long bookId) {
        this.bookId = bookId;
    }

    private Long promptId;

    public Long getPromptId() {
        return promptId;
    }

    public void setPromptId(Long promptId){
        this.promptId = promptId;
    }

    public PostCreateRequest(){

    }
    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public String getHeadline() {
        return headline;
    }

    public void setHeadline(String headline) {
        this.headline = headline;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }
}
