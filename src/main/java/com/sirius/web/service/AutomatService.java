package com.sirius.web.service;

import com.sirius.web.model.Automat;

import java.util.List;

public interface AutomatService {

    List<Automat> getAllAutomats();

    Automat createAutomat(Automat automat);

    Automat findAutomatById(String id);

    Automat updateAutomat(Automat automat);

    boolean deleteAutomat(String id);

    boolean deleteAll();

    boolean exists(String id);

}
