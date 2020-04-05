package com.sirius.web.model;

import lombok.Data;

@Data
public class Location {
    String province;
    String district;
    String 	neighborhood;
    float latitude;
    float longitude;
}
