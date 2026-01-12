package com.example.backend.repository;

import com.example.backend.model.UserCouchDoc;
import org.lightcouch.CouchDbClient;
import org.springframework.stereotype.Repository;

@Repository
public class CouchUserRepository {

    private final CouchDbClient couch;

    public CouchUserRepository(CouchDbClient couch) {
        this.couch = couch;
    }

    public void save(UserCouchDoc user) {
        couch.save(user);
    }

    public UserCouchDoc find(String id) {
        return couch.find(UserCouchDoc.class, id);
    }
}
