package com.vmware.scan.dataaccessor.main.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.vmware.scan.dataaccessor.main.ConfigConstants;
import com.vmware.scan.dataaccessor.main.repositories.ConfigurationRepository;

@RestController
public class ConfigurationController {

	@Autowired
	ConfigurationRepository configRepo;
	
	@RequestMapping("/config/analyzer/host")
	public String getAnalyzerHost(){
		return (String) configRepo.findOne(ConfigConstants.ANALYZER_MACHINE.getValue()).getValue();
	}
	
	@RequestMapping("/config/collector/host")
	public String getCollectorHost(){
		return (String) configRepo.findOne(ConfigConstants.COLLECTOR_MACHINE.getValue()).getValue();
	}
	
	@RequestMapping("/config/dataaccessor/host")
	public String getDataaccessorHost(){
		return (String) configRepo.findOne(ConfigConstants.DATAACCESSOR_MACHINE.getValue()).getValue();
	}
}