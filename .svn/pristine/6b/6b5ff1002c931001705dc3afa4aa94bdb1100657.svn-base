package com.vmware.scan.dataaccessor.main.beans;

import org.springframework.data.mongodb.core.mapping.Document;

@Document (collection = "config")
public class SCANConfiguration {

	private int id;
	private String name;
	private Object value;
	
	public SCANConfiguration(){
		
	}
	
	public SCANConfiguration(int id, String name, Object value) {
		super();
		this.id = id;
		this.name = name;
		this.value = value;
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Object getValue() {
		return value;
	}
	public void setValue(Object value) {
		this.value = value;
	}
	
}
