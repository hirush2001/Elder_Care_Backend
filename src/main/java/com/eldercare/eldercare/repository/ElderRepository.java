package com.eldercare.eldercare.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import com.eldercare.eldercare.model.Elder;

@Repository
public interface ElderRepository extends MongoRepository<Elder, String> {

}
