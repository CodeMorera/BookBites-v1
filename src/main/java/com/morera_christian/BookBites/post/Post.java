package com.morera_christian.BookBites.post;

import com.morera_christian.BookBites.book.Book;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

import java.time.LocalDateTime;

@Entity
@Table(name="posts")
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(nullable = false)
    private String authorName;

    @NotBlank
    @Column(nullable = false)
    private String headline;

    @NotBlank
    @Column(nullable = false, length = 4000)
    private String content;

    @Min(1)
    @Max(5)
    private Integer rating;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @PrePersist
    public void onCreate(){
        if(this.createdAt == null){
            this.createdAt = LocalDateTime.now();
        }
    }

    @ManyToOne(optional = false)
    @JoinColumn(name = "book-id", nullable = false)
    private Book book;

    public Post(){};

    public Post(String authorName,
                String headline,
                String content,
                Integer rating,
                Book book){
        this.authorName = authorName;
        this.headline = headline;
        this.content = content;
        this.rating = rating;
        this.book = book;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Long getId() {
        return id;
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

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

}
