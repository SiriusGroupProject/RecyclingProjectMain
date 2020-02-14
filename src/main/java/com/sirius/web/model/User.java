package com.sirius.web.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;

@Document(collection = "user")
@Data
public class User implements Serializable {
    @Id
    private String email;
    private String password;
    private String name;
    private String surname;
    private float balance;
    private boolean admin;

}
