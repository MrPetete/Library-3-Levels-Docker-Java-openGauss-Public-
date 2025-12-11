package com.library.librarymanagement.service;

import com.library.librarymanagement.model.AppUser;
import com.library.librarymanagement.model.PageResult;
import com.library.librarymanagement.repository.AppUserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AppUserService {

    private final AppUserRepository repository;

    public AppUserService(AppUserRepository repository) {
        this.repository = repository;
    }

    public List<AppUser> getAllUsers() {
        return repository.findAll();
    }

    public PageResult<AppUser> getUsersPage(int page, int size) {
        int safePage = Math.max(page, 1);
        int safeSize = Math.max(size, 1);
        long total = repository.countActive();
        int offset = (safePage - 1) * safeSize;
        List<AppUser> items = repository.findPage(safeSize, offset);
        int totalPages = (int) Math.ceil((double) total / safeSize);
        return new PageResult<>(items, safePage, safeSize, total, Math.max(totalPages, 1));
    }

    public void createUser(AppUser user) {
        repository.save(user);
    }

    public void updateUser(Long id, AppUser user) {
        user.setId(id);
        repository.update(user);
    }

    public void deleteUser(Long id) {
        repository.delete(id);
    }
}