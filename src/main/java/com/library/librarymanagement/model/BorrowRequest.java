package com.library.librarymanagement.model;

import lombok.Data;

@Data
public class BorrowRequest {
    private Long userId;
    private Long bookId;
    private Integer days; // how many days to borrow
}