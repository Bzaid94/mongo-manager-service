package com.blautech.mongodb.repository;

import com.blautech.mongodb.model.History;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HistoryRepository extends MongoRepository<History, String > {
}
