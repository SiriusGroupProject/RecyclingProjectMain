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
    public Bottle findBottleByBarcode(String bottle) {
        return bottleRepository.findById(bottle).orElse(null);
    }

    @Override
    public Bottle updateBottle(Bottle bottle) {
        return bottleRepository.save(bottle);
    }

    @Override
    public boolean deleteBottle(String bottle) {
        final Bottle dbBottle = findBottleByBarcode(bottle);
        if(dbBottle == null){
            throw new RuntimeException("Bottle not found");
        }
        bottleRepository.deleteById(bottle);
        return true;
    }

    @Override
    public boolean deleteAll() {
        bottleRepository.deleteAll();
        return true;
    }

    @Override
    public boolean exists(String bottle) {
        return bottleRepository.existsById(bottle);
    }
}
