package com.sirius.web.service;

import com.sirius.web.model.Automat;
import com.sirius.web.repository.AutomatRepository;
import org.springframework.beans.BeanUtils;
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
    public Automat updateAutomat(String id, Automat automat) {
        Automat existingAutomat = automatRepository.findById(id).orElse(null);
        BeanUtils.copyProperties(automat, existingAutomat);
        return automatRepository.save(existingAutomat);
    }

    @Override
    public Automat deleteAutomat(String id) {

        Automat existingAutomat = automatRepository.findById(id).orElse(null);

        if(existingAutomat == null){
            throw new RuntimeException("Wrong User");
        }
        automatRepository.deleteById(id);
        return existingAutomat;
    }


}
