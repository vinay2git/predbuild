package com.vmware.scan.dataaccessor.main.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.vmware.scan.dataaccessor.main.beans.SCANConfiguration;

public interface ConfigurationRepository extends MongoRepository<SCANConfiguration, String>{


}
