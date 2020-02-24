package com.sirius.web.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;

@Document(collection = "bottle")
@Data
public class Bottle implements Serializable {
    @Id
    private String id;
    private String name;
    private String barcode;
    private String type;
    private float price;
    private float volume;

}
