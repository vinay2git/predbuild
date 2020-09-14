package com.vmware.scan.dataaccessor.main.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.vmware.scan.dataaccessor.main.beans.ReviewerWeight;

@Repository
public interface ReviewerWeightRepository extends MongoRepository<ReviewerWeight, Integer>{

}
