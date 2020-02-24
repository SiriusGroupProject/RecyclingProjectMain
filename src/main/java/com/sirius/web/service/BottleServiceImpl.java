package com.sirius.web.service;

import com.sirius.web.model.Bottle;
import com.sirius.web.repository.BottleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BottleServiceImpl implements BottleService {

    private final BottleRepository bottleRepository;

    @Autowired
    public BottleServiceImpl(BottleRepository bottleRepository) {
        this.bottleRepository = bottleRepository;
    }
    
    @Override
    public List<Bottle> getAllBottles() {
        return null;
    }

    @Override
    public Bottle createBottle(Bottle bottle) {
        return null;
    }

    @Override
    public Bottle findBottleById(String id) {
        return null;
    }

    @Override
    public Bottle updateBottle(Bottle bottle) {
        return null;
    }

    @Override
    public boolean deleteBottle(String id) {
        return false;
    }

    @Override
    public boolean deleteAll() {
        return false;
    }

    @Override
    public boolean exists(String id) {
        return true;
    }
}
