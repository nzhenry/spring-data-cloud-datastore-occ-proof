package com.myapplication.email;

import org.springframework.cloud.gcp.data.datastore.repository.DatastoreRepository;

public interface EmailRepo extends DatastoreRepository<Email, Integer> {
}
