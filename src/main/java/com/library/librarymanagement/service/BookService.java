package com.library.librarymanagement.service;

import com.library.librarymanagement.model.Book;
import com.library.librarymanagement.repository.BookRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import com.library.librarymanagement.model.PageResult;

@Service
public class BookService {

    private final BookRepository bookRepository;

    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    public PageResult<Book> getBooksPage(int page, int size) {
        int safePage = Math.max(page, 1);
        int safeSize = Math.max(size, 1);
        long total = bookRepository.count();
        int offset = (safePage - 1) * safeSize;
        List<Book> items = bookRepository.findPage(safeSize, offset);
        int totalPages = (int) Math.ceil((double) total / safeSize);
        return new PageResult<>(items, safePage, safeSize, total, Math.max(totalPages, 1));
    }

    public Optional<Book> getBookById(Long id) {
        return bookRepository.findById(id);
    }

    public void createBook(Book book) {
        bookRepository.save(book);
    }

    public void updateBook(Long id, Book book) {
        book.setId(id);
        bookRepository.update(book);
    }

    public void deleteBook(Long id) {
        bookRepository.delete(id);
    }
}