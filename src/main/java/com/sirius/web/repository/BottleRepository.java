package com.sirius.web.repository;

import com.sirius.web.model.Bottle;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface BottleRepository extends MongoRepository<Bottle,String> {

}
