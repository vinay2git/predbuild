package com.vmware.scan.analyzer.main.pojos;

import java.io.Serializable;
import java.util.List;

public class AlgorithmInputObject implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String id;
	private String ownerAccountId;
	private List<CLFile> files;
	private List<String> reviewers;
	private boolean actualResult;
	private boolean predictedResult;
	private boolean isProcessed;
	private int totalqoc;
	private double weight;
	private Priority priority;
	
	public AlgorithmInputObject(String id, String ownerAccountId, List<CLFile> files,
			List<String> reviewers, boolean actualResult, int totalqoc, double weight, int priority) {
		super();
		this.id = id;
		this.ownerAccountId = ownerAccountId;
		this.files = files;
		this.reviewers = reviewers;
		this.actualResult = actualResult;
		this.isProcessed = false;
		this.totalqoc = totalqoc;
		this.weight = weight;
		if (priority == 0)
			priority = 1;
		this.priority = Priority.getPriority(priority); 
	}

	public String getId() {
		return id;
	}

	public String getOwnerAccountId() {
		return ownerAccountId;
	}

	public List<CLFile> getFiles() {
		return files;
	}

	public List<String> getReviewers() {
		return reviewers;
	}
	
	public void setReviewers(List<String> reviewers) {
		this.reviewers = reviewers;
	}

	public boolean isActualResult() {
		return actualResult;
	}

	public void setActualResult(boolean result) {
		this.actualResult = result;
	}

	public boolean isPredictedResult() {
		return predictedResult;
	}

	public void setPredictedResult(boolean predictedResult) {
		this.predictedResult = predictedResult;
	}

	public boolean isProcessed() {
		return isProcessed;
	}

	public void setProcessed(boolean isProcessed) {
		this.isProcessed = isProcessed;
	}

	@Override
	public String toString() {
		//return this.id + ","+ this.ownerAccountId+ ","+ this.files+ "," + this.reviewers + ","+ this.actualResult+ ","+ this.predictedResult+ "<END>";
		//return this.id + ","+ this.actualResult+ ","+ this.predictedResult+ "<END>";
		return this.id +"-"+ this.ownerAccountId +"-"+ this.files +"-"+ this.totalqoc +"-"+ this.reviewers +"\n";
	}

	public int getTotalqoc() {
		return totalqoc;
	}

	public void setTotalqoc(int totalqoc) {
		this.totalqoc = totalqoc;
	}

	public double getWeight() {
		return weight;
	}

	public void setWeight(double weight) {
		this.weight = weight;
	}

	public Priority getPriority() {
		return priority;
	}

	public void setPriority(Priority priority) {
		this.priority = priority;
	}
}
