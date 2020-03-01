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
        return bottleRepository.findAll();
    }

    @Override
    public Bottle createBottle(Bottle bottle) {
        return bottleRepository.save(bottle);
    }

    @Override
    public Bottle findBottleById(String id) {
        return bottleRepository.findById(id).orElse(null);
    }

    @Override
    public Bottle updateBottle(Bottle bottle) {
        return bottleRepository.save(bottle);
    }

    @Override
    public boolean deleteBottle(String id) {
        final Bottle dbBottle = findBottleById(id);
        if(dbBottle == null){
            throw new RuntimeException("Bottle not found");
        }
        bottleRepository.deleteById(id);
        return true;
    }

    @Override
    public boolean deleteAll() {
        bottleRepository.deleteAll();
        return true;
    }

    @Override
    public boolean exists(String id) {
        return bottleRepository.existsById(id);
    }
}
