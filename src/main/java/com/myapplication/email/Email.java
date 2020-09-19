package com.myapplication.email;

import lombok.Data;
import org.springframework.cloud.gcp.data.datastore.core.mapping.Entity;
import org.springframework.data.annotation.Id;

@Data
@Entity
public class Email {

    @Id
    private Long id;

    private final String to;
}
