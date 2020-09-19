package com.myapplication.user;

import lombok.Data;
import org.springframework.cloud.gcp.data.datastore.core.mapping.Entity;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;

import java.util.Date;

@Data
@Entity
public class User {

    @Id
    private final String id;

    private final Date createdAt;

    @Transient
    private boolean isNew;

    public static User create(String id) {
        User user = new User(id, new Date());
        user.isNew = true;
        return user;
    }
}
