package com.sirius.web.repository;

import com.sirius.web.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<User,String> {

    User findByUsernameAndPassword(final String id, final String password);

}
