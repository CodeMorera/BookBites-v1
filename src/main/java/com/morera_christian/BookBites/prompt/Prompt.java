package com.morera_christian.BookBites.prompt;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name= "prompts")
public class Prompt {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long bookId;

    private String category;

    @Column(nullable = false, length = 1000)
    private String text;

    public Long getId() {
        return id;
    }
    // public void setId(Long id) {
    //     this.id = id;
    // }
    public Long getBookId() {
        return bookId;
    }
    public void setBookId(Long bookId) {
        this.bookId = bookId;
    }
    public String getCategory() {
        return category;
    }
    public void setCategory(String category) {
        this.category = category;
    }
    public String getText() {
        return text;
    }
    public void setText(String text) {
        this.text = text;
    }
    public Prompt(){

    }
    public Prompt(Long id, 
                  Long bookId, 
                  String category, 
                  String text){
        this.bookId = bookId;
        this.category = category;
        this.text = text;
    }

}
