package com.sirius.web.service;

import com.sirius.web.model.Bottle;

import java.util.List;

public interface BottleService {

    List<Bottle> getAllBottles();

    Bottle createBottle(Bottle bottle);

    Bottle findBottleByBarcode(String barcode);

    Bottle updateBottle(Bottle bottle);

    boolean deleteBottle(String barcode);

    boolean deleteAll();

    boolean exists(String barcode);

}
