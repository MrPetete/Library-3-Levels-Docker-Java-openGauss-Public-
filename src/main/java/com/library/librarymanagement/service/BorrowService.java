package com.library.librarymanagement.service;

import com.library.librarymanagement.model.BorrowRecord;
import com.library.librarymanagement.repository.BorrowRecordRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class BorrowService {

    private final BorrowRecordRepository repository;

    public BorrowService(BorrowRecordRepository repository) {
        this.repository = repository;
    }

    public List<BorrowRecord> getAllRecords() {
        return repository.findAll();
    }

    @Transactional
    public void borrowBook(Long userId, Long bookId, int days) {
        int updated = repository.decreaseAvailableCopies(bookId);
        if (updated == 0) {
            throw new RuntimeException("No available copies for this book.");
        }
        LocalDate now = LocalDate.now();
        LocalDate due = now.plusDays(days);
        repository.insertBorrow(userId, bookId, now, due);
    }

    @Transactional
    public void returnBook(Long recordId) {
        BorrowRecord record = repository.findById(recordId)
                .orElseThrow(() -> new RuntimeException("Borrow record not found"));

        if ("RETURNED".equalsIgnoreCase(record.getStatus())) {
            return;
        }

        repository.markReturned(recordId, LocalDate.now());
        repository.increaseAvailableCopies(record.getBookId());
    }
}