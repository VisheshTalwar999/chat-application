package com.example.backend.config;

import com.example.backend.model.Role;
import com.example.backend.model.UserCouchDoc;
import com.example.backend.model.UserStatus;
import org.lightcouch.CouchDbClient;
import org.lightcouch.NoDocumentException;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DataInitializer implements CommandLineRunner {

    private final CouchDbClient couch;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(CouchDbClient couch, PasswordEncoder passwordEncoder) {
        this.couch = couch;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {

        try {
            List<UserCouchDoc> admins = couch
                    .view("users/byEmail")
                    .key("admin@system.com")
                    .includeDocs(true)
                    .query(UserCouchDoc.class);

            if (admins.isEmpty()) {
                createAdmin();
            }

        } catch (NoDocumentException e) {
            // View or DB not present yet
            createAdmin();
        } catch (Exception e) {
            System.err.println("⚠️ Admin initialization skipped: " + e.getMessage());
        }
    }

    private void createAdmin() {
        UserCouchDoc admin = new UserCouchDoc();
        admin.setFirstName("System");
        admin.setLastName("Admin");
        admin.setEmail("admin@system.com");
        admin.setPassword(passwordEncoder.encode("admin123"));
        admin.setRole(Role.ADMIN);

        // ✅ FIX: use status instead of approved
        admin.setStatus(UserStatus.APPROVED);

        couch.save(admin);
        System.out.println("✅ Default Admin Created in CouchDB");
    }

}
