package com.library.librarymanagement.repository;

import com.library.librarymanagement.model.BorrowRecord;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public class BorrowRecordRepository {

    private final JdbcTemplate jdbcTemplate;

    private final RowMapper<BorrowRecord> rowMapper = (rs, rowNum) -> {
        BorrowRecord r = new BorrowRecord();
        r.setId(rs.getLong("id"));
        r.setUserId(rs.getLong("user_id"));
        r.setBookId(rs.getLong("book_id"));
        r.setBorrowDate(rs.getDate("borrow_date").toLocalDate());
        r.setDueDate(rs.getDate("due_date").toLocalDate());
        if (rs.getDate("return_date") != null) {
            r.setReturnDate(rs.getDate("return_date").toLocalDate());
        }
        r.setStatus(rs.getString("status"));
        return r;
    };

    public BorrowRecordRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<BorrowRecord> findAll() {
        String sql = "SELECT * FROM borrow_record ORDER BY borrow_date DESC";
        return jdbcTemplate.query(sql, rowMapper);
    }

    public Optional<BorrowRecord> findById(Long id) {
        String sql = "SELECT * FROM borrow_record WHERE id = ?";
        List<BorrowRecord> list = jdbcTemplate.query(sql, rowMapper, id);
        if (list.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(list.get(0));
    }

    public int insertBorrow(Long userId, Long bookId, LocalDate borrowDate, LocalDate dueDate) {
        String sql = "INSERT INTO borrow_record (user_id, book_id, borrow_date, due_date, status) " +
                "VALUES (?, ?, ?, ?, 'BORROWED')";
        return jdbcTemplate.update(sql, userId, bookId, borrowDate, dueDate);
    }

    public int markReturned(Long id, LocalDate returnDate) {
        String sql = "UPDATE borrow_record SET return_date = ?, status = 'RETURNED' WHERE id = ?";
        return jdbcTemplate.update(sql, returnDate, id);
    }

    public int decreaseAvailableCopies(Long bookId) {
        String sql = "UPDATE book SET available_copies = available_copies - 1 " +
                "WHERE id = ? AND available_copies > 0";
        return jdbcTemplate.update(sql, bookId);
    }

    public int increaseAvailableCopies(Long bookId) {
        String sql = "UPDATE book SET available_copies = available_copies + 1 WHERE id = ?";
        return jdbcTemplate.update(sql, bookId);
    }
}