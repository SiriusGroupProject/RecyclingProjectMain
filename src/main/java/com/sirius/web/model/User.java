package com.sirius.web.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.HashMap;

@Document
@Getter
@Setter
public class User {
    @Id
    private String id;
    private String name;
    private String surname;
    private String email;
    private float balance;
    private boolean type;
    private String password;

    private HashMap properties;


}
