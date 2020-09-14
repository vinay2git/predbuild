package com.vmware.scan.analyzer.main.dbpojos;

public class FileWeight {

	// From db
	private String name;
	private double weight;
	private int count;
	
	// For computation
	private double qoc;
	private double meanPassRate;
	private double meanImpactRate;
	
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

	public double getQoc() {
		return qoc;
	}

	public void setQoc(double qoc) {
		this.qoc = qoc;
	}

	public double getMeanPassRate() {
		return meanPassRate;
	}

	public void setMeanPassRate(double meanPassRate) {
		this.meanPassRate = meanPassRate;
	}

	public double getMeanImpactRate() {
		return meanImpactRate;
	}

	public void setMeanImpactRate(double meanImpactRate) {
		this.meanImpactRate = meanImpactRate;
	}
	
	public void computeWeightedMean(int totalSize) {
		this.weight = (((double) this.count / (double) totalSize) * this.meanImpactRate);
	}
	
	public void updateInfo(double weight, double qoc) {
		this.count++;
		this.meanPassRate = ((weight + ((this.count-1) * this.meanPassRate)) / (this.count));
		this.meanImpactRate = ((qoc + ((this.count-1) * this.meanImpactRate)) / (this.count));
	}
	@Override
	public String toString() {
		return this.name + "-" + this.count + "-" + this.meanImpactRate + "-" + this.meanPassRate;
	}
	
}
