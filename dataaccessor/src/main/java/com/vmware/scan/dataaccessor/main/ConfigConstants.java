package com.vmware.scan.dataaccessor.main;

public enum ConfigConstants {

	ANALYZER_MACHINE("analyzer.host"),
	COLLECTOR_MACHINE("collector.host"),
	DATAACCESSOR_MACHINE("dataaccessor.host");
	
	private String value;
	
	private ConfigConstants(String value){
		this.value = value;
	}
	
	public String getValue(){
		return this.value;
	}
	
}
