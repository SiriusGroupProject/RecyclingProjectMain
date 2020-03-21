package com.sirius.web.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;

@Document(collection = "automat")
@Data
public class Automat implements Serializable {
    @Id
    private String id;
    private float overallVolume;
    private float capacity;
    private boolean active;
    private int numberOfBottles ;
    private Location  location;

}
