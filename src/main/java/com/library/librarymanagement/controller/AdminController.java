package com.library.librarymanagement.controller;

import com.library.librarymanagement.service.AppUserService;
import com.library.librarymanagement.service.BookService;
import com.library.librarymanagement.service.BorrowService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final BookService bookService;
    private final AppUserService userService;
    private final BorrowService borrowService;

    public AdminController(BookService bookService,
                           AppUserService userService,
                           BorrowService borrowService) {
        this.bookService = bookService;
        this.userService = userService;
        this.borrowService = borrowService;
    }

    @GetMapping
    public String adminHome(Model model) {
        model.addAttribute("bookCount", bookService.getAllBooks().size());
        model.addAttribute("userCount", userService.getAllUsers().size());
        model.addAttribute("borrowCount", borrowService.getAllRecords().size());
        return "admin/admin-home";
    }

    @GetMapping("/books")
    public String adminBooks(Model model) {
        model.addAttribute("books", bookService.getAllBooks());
        return "admin/admin-books";
    }

    @GetMapping("/users")
    public String adminUsers(Model model) {
        model.addAttribute("users", userService.getAllUsers());
        return "admin/admin-users";
    }

    @GetMapping("/borrows")
    public String adminBorrows(Model model) {
        model.addAttribute("borrows", borrowService.getAllRecords());
        return "admin/admin-borrows";
    }
}
