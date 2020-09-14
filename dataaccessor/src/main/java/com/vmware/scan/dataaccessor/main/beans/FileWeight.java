package com.vmware.scan.dataaccessor.main.beans;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document (collection = "fileweight")
public class FileWeight {

	@Id
	private String name;
	private double weight;
	private int count;
	
	public FileWeight(){}
	
	public FileWeight(String name, double weight) {
		super();
		this.name = name;
		this.weight = weight;
	}
	
	public String getName() {
		return name;
	}
	public void setFilename(String name) {
		this.name = name;
	}
	public double getWeight() {
		return weight;
	}
	public void setFileWeight(double weight) {
		this.weight = weight;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}
	
}
