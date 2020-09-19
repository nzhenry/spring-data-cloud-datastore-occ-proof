package com.myapplication.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gcp.data.datastore.repository.DatastoreRepository;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class UserService {

    private final UserRepo userRepo;

    public UserService(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    public User getUser(String id) {
        return getUser(id, userRepo);
    }

    private User getUser(String id, DatastoreRepository<User, String> repo) {
        return repo.findById(id).orElseGet(() -> {
            User user = User.create(id);
            return repo.save(user);
        });
    }

    public User getUserWithTransaction(String id) {
        return userRepo.performTransaction(repo -> getUser(id,repo));
    }
}
