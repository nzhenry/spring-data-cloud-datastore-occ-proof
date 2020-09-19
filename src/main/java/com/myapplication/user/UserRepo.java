package com.myapplication.user;

import org.springframework.cloud.gcp.data.datastore.repository.DatastoreRepository;

public interface UserRepo extends DatastoreRepository<User, String> {
}