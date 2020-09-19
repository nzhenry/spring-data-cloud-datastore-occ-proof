package com.myapplication;

import org.springframework.cloud.gcp.data.datastore.repository.DatastoreRepository;

public interface MyEntityRepo extends DatastoreRepository<MyEntity, String> {
}