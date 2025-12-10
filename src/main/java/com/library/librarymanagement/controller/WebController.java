package com.library.librarymanagement.controller;

import com.library.librarymanagement.model.Book;
import com.library.librarymanagement.model.BorrowRecord;
import com.library.librarymanagement.service.BookService;
import com.library.librarymanagement.service.BorrowService;
import com.library.librarymanagement.service.AppUserService;
import com.library.librarymanagement.model.AppUser;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
public class WebController {

    private final BookService bookService;
    private final BorrowService borrowService;
    private final AppUserService userService;

    public WebController(BookService bookService,
                         BorrowService borrowService,
                         AppUserService userService) {
        this.bookService = bookService;
        this.borrowService = borrowService;
        this.userService = userService;
    }

    @GetMapping("/")
    public String home() {
        return "redirect:/books";
    }

    // List + search books
    @GetMapping("/books")
    public String listBooks(@RequestParam(value = "q", required = false) String query,
                            Model model) {

        List<Book> books = bookService.getAllBooks();

        if (query != null && !query.isBlank()) {
            String lower = query.toLowerCase();
            books = books.stream()
                    .filter(b -> (b.getTitle() != null && b.getTitle().toLowerCase().contains(lower)) ||
                            (b.getAuthor() != null && b.getAuthor().toLowerCase().contains(lower)))
                    .collect(Collectors.toList());
        }

        model.addAttribute("books", books);
        model.addAttribute("query", query);
        return "books";
    }

    // Show borrow form
    @GetMapping("/borrow")
    public String showBorrowForm(Model model) {
        model.addAttribute("users", userService.getAllUsers());
        model.addAttribute("books", bookService.getAllBooks());
        return "borrow";
    }

    // Handle borrow submit
    @PostMapping("/borrow")
    public String borrow(@RequestParam Long userId,
                         @RequestParam Long bookId,
                         @RequestParam(defaultValue = "14") Integer days,
                         Model model) {
        try {
            borrowService.borrowBook(userId, bookId, days);
            return "redirect:/borrows";
        } catch (RuntimeException e) {
            model.addAttribute("errorMessage", e.getMessage());
            model.addAttribute("users", userService.getAllUsers());
            model.addAttribute("books", bookService.getAllBooks());
            return "borrow";
        }
    }

    // List borrows
    @GetMapping("/borrows")
    public String listBorrows(Model model) {
        // Load raw records
        List<BorrowRecord> records = borrowService.getAllRecords();  // or your method name
        List<AppUser> users = userService.getAllUsers();
        List<Book> books = bookService.getAllBooks();

        // Build id -> entity maps for quick lookup in Thymeleaf
        Map<Long, AppUser> userMap = users.stream()
                .collect(Collectors.toMap(AppUser::getId, u -> u));

        Map<Long, Book> bookMap = books.stream()
                .collect(Collectors.toMap(Book::getId, b -> b));

        model.addAttribute("borrows", records);
        model.addAttribute("userMap", userMap);
        model.addAttribute("bookMap", bookMap);

        return "borrows";
    }



    // Handle return
    @PostMapping("/borrows/{id}/return")
    public String returnBook(@PathVariable Long id) {
        borrowService.returnBook(id);
        return "redirect:/borrows";
    }
}
