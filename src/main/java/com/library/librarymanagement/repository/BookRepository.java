package com.library.librarymanagement.repository;

import com.library.librarymanagement.model.Book;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class BookRepository {

    private final JdbcTemplate jdbcTemplate;

    private final RowMapper<Book> bookRowMapper = (rs, rowNum) -> {
        Book b = new Book();
        b.setId(rs.getLong("id"));
        b.setTitle(rs.getString("title"));
        b.setAuthor(rs.getString("author"));
        b.setCategoryId(rs.getLong("category_id"));
        b.setIsbn(rs.getString("isbn"));
        b.setTotalCopies(rs.getInt("total_copies"));
        b.setAvailableCopies(rs.getInt("available_copies"));
        return b;
    };

    public BookRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Book> findAll() {
        String sql = "SELECT id, title, author, category_id, isbn, total_copies, available_copies FROM book";
        return jdbcTemplate.query(sql, bookRowMapper);
    }

    public int save(Book book) {
        String sql = "INSERT INTO book (title, author, category_id, isbn, total_copies, available_copies) " +
                "VALUES (?, ?, ?, ?, ?, ?)";
        return jdbcTemplate.update(sql,
                book.getTitle(),
                book.getAuthor(),
                book.getCategoryId(),
                book.getIsbn(),
                book.getTotalCopies(),
                book.getAvailableCopies());
    }
}