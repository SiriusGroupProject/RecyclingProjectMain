package com.sirius.web.service;

import com.sirius.web.model.Bottle;

import java.util.List;

public interface BottleService {

    List<Bottle> getAllBottles();

    Bottle createBottle(Bottle bottle);

    Bottle findBottleById(String id);

    Bottle updateBottle(Bottle bottle);

    boolean deleteBottle(String id);

    boolean deleteAll();

    boolean exists(String id);

}
