package com.myapplication;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gcp.data.datastore.repository.DatastoreRepository;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class MyService {

    private final MyEntityRepo myEntityRepo;

    public MyService(MyEntityRepo myEntityRepo) {
        this.myEntityRepo = myEntityRepo;
    }

    public MyEntity putEntity(String id) {
        return putEntity(id, this.myEntityRepo);
    }

    public MyEntity putWithTransaction(String id) {
        // We're using an instance of DatastoreRepository to perform the transaction here,
        // but if we needed to modify multiple entities of different types, we could use
        // DatastoreOperations instead.
        return myEntityRepo.performTransaction(ops -> putEntity(id, ops));
    }

    private MyEntity putEntity(String id, DatastoreRepository<MyEntity, String> ops) {
        MyEntity instance = ops.findById(id).orElse(null);
        if(instance == null) {
            instance = new MyEntity(id);
        }
        instance.setSaveCount(instance.getSaveCount() + 1);
        return ops.save(instance);
    }
}
