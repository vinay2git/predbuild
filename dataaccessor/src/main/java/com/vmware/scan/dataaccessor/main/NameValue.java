package com.vmware.scan.dataaccessor.main;

public class NameValue {

	private String name;
	private Object value;
	
	public NameValue(){
		
	}
	
	public NameValue(String name, Object value) {
		super();
		this.name = name;
		this.value = value;
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
