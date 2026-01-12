package com.example.backend.service;

import com.example.backend.model.Role;
import com.example.backend.model.UserCouchDoc;
import com.example.backend.model.UserStatus;
import org.lightcouch.CouchDbClient;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final CouchDbClient couch;

    public UserService(CouchDbClient couch) {
        this.couch = couch;
    }

    // ✅ Admin approves user and assigns role
    public void approve(String id, Role role) {
        UserCouchDoc user = couch.find(UserCouchDoc.class, id);
        user.setStatus(UserStatus.APPROVED);   // ✅ FIXED
        user.setRole(role);
        couch.update(user);
    }

    // (Optional) Reject user
    public void reject(String id) {
        UserCouchDoc user = couch.find(UserCouchDoc.class, id);
        user.setStatus(UserStatus.REJECTED);
        couch.update(user);
    }
}
