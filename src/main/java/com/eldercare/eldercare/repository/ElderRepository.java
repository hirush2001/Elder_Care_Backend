package com.eldercare.eldercare.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import com.eldercare.eldercare.model.Elder;
import java.util.Optional;

@Repository
public interface ElderRepository extends MongoRepository<Elder, String> {
    Optional<Elder> findByEmail(String email);

    Optional<Elder> findById(String elderId);
}
