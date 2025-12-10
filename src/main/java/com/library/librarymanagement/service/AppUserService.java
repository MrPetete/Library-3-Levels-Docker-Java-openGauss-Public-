package com.library.librarymanagement.service;

import com.library.librarymanagement.model.AppUser;
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