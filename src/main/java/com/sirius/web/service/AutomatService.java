package com.sirius.web.service;

import com.sirius.web.model.Automat;

import java.util.List;

public interface AutomatService {

    List<Automat> getAllAutomats();

    Automat createAutomat(Automat automat);

    Automat findAutomatById(String id);

    Automat updateAutomat(String id, Automat automat);

    Automat deleteAutomat(String id);

}
