package com.example.backend.controller;

import com.example.backend.model.UserCouchDoc;
import com.example.backend.model.Role;
import com.example.backend.model.UserStatus;
import com.example.backend.service.UserService;
import org.lightcouch.CouchDbClient;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminController {

    private final UserService userService;
    private final CouchDbClient couch;

    public AdminController(UserService userService, CouchDbClient couch) {
        this.userService = userService;
        this.couch = couch;
    }

    // ✅ 1. Get all pending users (FOR ADMIN DASHBOARD)
    @GetMapping("/users/pending")
    @PreAuthorize("hasRole('ADMIN')")
    public List<UserCouchDoc> getPendingUsers() {
        return couch.view("users/byStatus")
                .key(UserStatus.PENDING.name())
                .includeDocs(true)
                .query(UserCouchDoc.class);
    }

    // ✅ 2. Approve user and assign role
    @PutMapping("/users/{id}/approve")
    @PreAuthorize("hasRole('ADMIN')")
    public void approveUser(
            @PathVariable String id,
            @RequestParam Role role) {

        userService.approve(id, role);
    }

    // (Optional but good)
    @PutMapping("/users/{id}/reject")
    @PreAuthorize("hasRole('ADMIN')")
    public void rejectUser(@PathVariable String id) {
        userService.reject(id);
    }
}
