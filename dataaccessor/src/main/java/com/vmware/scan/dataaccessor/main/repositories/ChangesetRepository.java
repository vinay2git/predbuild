package com.vmware.scan.dataaccessor.main.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.vmware.scan.dataaccessor.main.beans.Changeset;

@Repository
public interface ChangesetRepository extends MongoRepository<Changeset, String> {

}
