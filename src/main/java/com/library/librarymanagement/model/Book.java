package com.library.librarymanagement.model;

import lombok.Data;

@Data
public class Book {
    private Long id;
    private String title;
    private String author;
    private Long categoryId;
    private String isbn;
    private Integer totalCopies;
    private Integer availableCopies;
}