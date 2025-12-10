package com.library.librarymanagement.model;

import lombok.Data;

import java.time.LocalDate;

@Data
public class BorrowRecord {
    private Long id;
    private Long userId;
    private Long bookId;
    private LocalDate borrowDate;
    private LocalDate dueDate;
    private LocalDate returnDate;
    private String status; // BORROWED, RETURNED
}