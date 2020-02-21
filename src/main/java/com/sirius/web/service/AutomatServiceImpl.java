package com.sirius.web.service;

import com.sirius.web.model.Automat;
import com.sirius.web.repository.AutomatRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AutomatServiceImpl implements AutomatService {

    private final AutomatRepository automatRepository;

    @Autowired
    public AutomatServiceImpl(AutomatRepository automatRepository) {
        this.automatRepository = automatRepository;
    }


    @Override
    public List<Automat> getAllAutomats() {
        return automatRepository.findAll();
    }

    @Override
    public Automat createAutomat(Automat automat) {
        return automatRepository.save(automat);
    }

    @Override
    public Automat findAutomatById(String id) {
        return automatRepository.findById(id).orElse(null);
    }

    @Override
    public Automat updateAutomat(Automat automat) {
        return automatRepository.save(automat);
    }

    @Override
    public boolean deleteAutomat(String id) {
        final Automat dbautomat = findAutomatById(id);
        if(dbautomat == null){
            throw new RuntimeException("Automat not found");
        }
        automatRepository.deleteById(id);
        return true;
    }

    @Override
    public boolean deleteAll() {
        automatRepository.deleteAll();
        return true;
    }

    @Override
    public boolean exists(String id) {
        return automatRepository.existsById(id);
    }

}
