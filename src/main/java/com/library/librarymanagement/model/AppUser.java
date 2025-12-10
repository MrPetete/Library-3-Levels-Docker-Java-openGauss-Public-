package com.library.librarymanagement.model;

import lombok.Data;

@Data
public class AppUser {
    private Long id;
    private String name;
    private String email;
    private String studentNo;
}