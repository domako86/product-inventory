package com.example.productinventory.security.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class RoleController {
    @GetMapping("/user")
    public String userAccess() {
        return "Content for users with role USER";
    }

    @GetMapping("/admin")
    public String adminAccess() {
        return "Content for users with role ADMIN";
    }
}
