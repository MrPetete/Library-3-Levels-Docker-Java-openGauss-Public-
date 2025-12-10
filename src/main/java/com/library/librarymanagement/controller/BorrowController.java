package com.library.librarymanagement.controller;

import com.library.librarymanagement.model.BorrowRecord;
import com.library.librarymanagement.model.BorrowRequest;
import com.library.librarymanagement.service.BorrowService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/borrows")
public class BorrowController {

    private final BorrowService borrowService;

    public BorrowController(BorrowService borrowService) {
        this.borrowService = borrowService;
    }

    @GetMapping
    public List<BorrowRecord> listBorrows() {
        return borrowService.getAllRecords();
    }

    @PostMapping("/borrow")
    public ResponseEntity<String> borrow(@RequestBody BorrowRequest request) {
        int days = request.getDays() != null ? request.getDays() : 14;
        borrowService.borrowBook(request.getUserId(), request.getBookId(), days);
        return ResponseEntity.status(HttpStatus.CREATED).body("Borrow created");
    }

    @PostMapping("/{id}/return")
    public ResponseEntity<String> returnBook(@PathVariable Long id) {
        borrowService.returnBook(id);
        return ResponseEntity.ok("Book returned");
    }
}