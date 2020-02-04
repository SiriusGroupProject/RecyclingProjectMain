package com.sirius.web.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;

@Document(collection = "user")
@Getter
@Setter
public class User implements Serializable {
    @Id
    private String id;
    private String password;
    private String name; //firstName
    private String surname; //lastName
    @Indexed(unique = true)
    private String email;
    private float balance;
    private boolean type; // 0:admin 1:user

}
