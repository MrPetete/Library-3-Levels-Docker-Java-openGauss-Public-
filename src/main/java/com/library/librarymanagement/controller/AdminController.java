package com.library.librarymanagement.controller;

import com.library.librarymanagement.model.AppUser;
import com.library.librarymanagement.model.Book;
import com.library.librarymanagement.service.AppUserService;
import com.library.librarymanagement.service.BookService;
import com.library.librarymanagement.service.BorrowService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
        model.addAttribute("bookCount", bookService.getBooksPage(1, 1).getTotal());
        model.addAttribute("userCount", userService.getUsersPage(1, 1).getTotal());
        model.addAttribute("borrowCount", borrowService.getRecordsPage(1, 1).getTotal());
        return "admin/admin-home";
    }

    // Book management endpoints
    @GetMapping("/books")
    public String adminBooks(@RequestParam(defaultValue = "1") int page,
                             @RequestParam(defaultValue = "10") int size,
                             Model model) {
        var pageData = bookService.getBooksPage(page, size);
        model.addAttribute("booksPage", pageData);
        return "admin/admin-books";
    }

    @GetMapping("/books/new")
    public String showAddBookForm(Model model) {
        model.addAttribute("book", new Book());
        return "admin/book-form";
    }

    @PostMapping("/books")
    public String addBook(@ModelAttribute Book book, RedirectAttributes redirectAttributes) {
        bookService.createBook(book);
        redirectAttributes.addFlashAttribute("message", "Book added successfully!");
        return "redirect:/admin/books";
    }

    @GetMapping("/books/{id}/edit")
    public String showEditBookForm(@PathVariable Long id, Model model) {
        return bookService.getBookById(id)
                .map(book -> {
                    model.addAttribute("book", book);
                    return "admin/book-form";
                })
                .orElse("redirect:/admin/books");
    }

    @PostMapping("/books/{id}")
    public String updateBook(@PathVariable Long id, @ModelAttribute Book book, RedirectAttributes redirectAttributes) {
        bookService.updateBook(id, book);
        redirectAttributes.addFlashAttribute("message", "Book updated successfully!");
        return "redirect:/admin/books";
    }

    @PostMapping("/books/{id}/delete")
    public String deleteBook(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        bookService.deleteBook(id);
        redirectAttributes.addFlashAttribute("message", "Book deleted successfully!");
        return "redirect:/admin/books";
    }

    // User management endpoints
    @GetMapping("/users")
    public String adminUsers(@RequestParam(defaultValue = "1") int page,
                             @RequestParam(defaultValue = "10") int size,
                             Model model) {
        var pageData = userService.getUsersPage(page, size);
        model.addAttribute("usersPage", pageData);
        return "admin/admin-users";
    }

    @GetMapping("/users/new")
    public String showAddUserForm(Model model) {
        model.addAttribute("user", new AppUser());
        return "admin/user-form";
    }

    @PostMapping("/users")
    public String addUser(@ModelAttribute AppUser user, RedirectAttributes redirectAttributes) {
        userService.createUser(user);
        redirectAttributes.addFlashAttribute("message", "User added successfully!");
        return "redirect:/admin/users";
    }

    @GetMapping("/users/{id}/edit")
    public String showEditUserForm(@PathVariable Long id, Model model) {
        return userService.getAllUsers().stream()
                .filter(u -> u.getId().equals(id))
                .findFirst()
                .map(u -> {
                    model.addAttribute("user", u);
                    return "admin/user-form";
                })
                .orElse("redirect:/admin/users");
    }

    @PostMapping("/users/{id}")
    public String updateUser(@PathVariable Long id,
                             @ModelAttribute AppUser user,
                             RedirectAttributes redirectAttributes) {
        userService.updateUser(id, user);
        redirectAttributes.addFlashAttribute("message", "User updated successfully!");
        return "redirect:/admin/users";
    }

    @PostMapping("/users/{id}/delete")
    public String deleteUser(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        userService.deleteUser(id);
        redirectAttributes.addFlashAttribute("message", "User deleted successfully!");
        return "redirect:/admin/users";
    }

    // Borrow history endpoint
    @GetMapping("/borrows")
    public String adminBorrows(@RequestParam(defaultValue = "1") int page,
                               @RequestParam(defaultValue = "10") int size,
                               Model model) {
        var pageData = borrowService.getRecordsPage(page, size);
        List<AppUser> users = userService.getAllUsers();
        List<Book> books = bookService.getAllBooks();

        // Build id -> entity maps for quick lookup in Thymeleaf
        Map<Long, AppUser> userMap = users.stream()
                .collect(Collectors.toMap(AppUser::getId, u -> u));

        Map<Long, Book> bookMap = books.stream()
                .collect(Collectors.toMap(Book::getId, b -> b));

        model.addAttribute("borrowsPage", pageData);
        model.addAttribute("userMap", userMap);
        model.addAttribute("bookMap", bookMap);
        return "admin/admin-borrows";
    }
}
