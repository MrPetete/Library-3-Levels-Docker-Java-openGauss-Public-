package com.library.librarymanagement.repository;

import com.library.librarymanagement.model.Book;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class BookRepository {

    private final JdbcTemplate jdbcTemplate;

    private final RowMapper<Book> bookRowMapper = new RowMapper<Book>() {
        @Override
        @NonNull
        public Book mapRow(@NonNull java.sql.ResultSet rs, int rowNum) throws java.sql.SQLException {
            Book b = new Book();
            b.setId(rs.getLong("id"));
            b.setTitle(rs.getString("title"));
            b.setAuthor(rs.getString("author"));
            b.setCategoryId(rs.getLong("category_id"));
            b.setIsbn(rs.getString("isbn"));
            b.setTotalCopies(rs.getInt("total_copies"));
            b.setAvailableCopies(rs.getInt("available_copies"));
            return b;
        }
    };

    public BookRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @SuppressWarnings("null")
    public List<Book> findAll() {
        String sql = "SELECT id, title, author, category_id, isbn, total_copies, available_copies FROM book";
        return jdbcTemplate.query(sql, bookRowMapper);
    }

    @SuppressWarnings("null")
    public Optional<Book> findById(Long id) {
        String sql = "SELECT id, title, author, category_id, isbn, total_copies, available_copies FROM book WHERE id = ?";
        List<Book> list = jdbcTemplate.query(sql, bookRowMapper, id);
        if (list.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(list.get(0));
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

    public int update(Book book) {
        String sql = "UPDATE book SET title = ?, author = ?, category_id = ?, isbn = ?, " +
                "total_copies = ?, available_copies = ? WHERE id = ?";
        return jdbcTemplate.update(sql,
                book.getTitle(),
                book.getAuthor(),
                book.getCategoryId(),
                book.getIsbn(),
                book.getTotalCopies(),
                book.getAvailableCopies(),
                book.getId());
    }

    public int delete(Long id) {
        String sql = "DELETE FROM book WHERE id = ?";
        return jdbcTemplate.update(sql, id);
    }
}