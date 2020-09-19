package com.myapplication;

import lombok.Data;
import org.springframework.cloud.gcp.data.datastore.core.mapping.Entity;
import org.springframework.data.annotation.Id;

@Data
@Entity
public class MyEntity {

    @Id
    private final String id;

    private int saveCount;

    public MyEntity(String id){
        this.id = id;
    }
}
