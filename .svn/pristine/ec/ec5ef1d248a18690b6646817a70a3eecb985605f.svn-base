package com.vmware.scan.dataaccessor.main.beans;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document (collection = "reviewerweigt")
public class ReviewerWeight {
	
	@Id
	private int reviewerId;
	private double reviewerWeight;
	private int count;
	
	public ReviewerWeight(){
		
	}
	
	public ReviewerWeight(int reviewerId, double reviewerWeight, int count) {
		super();
		this.reviewerId = reviewerId;
		this.reviewerWeight = reviewerWeight;
		this.count = count;
	}
	public int getReviewerId() {
		return reviewerId;
	}
	public void setReviewerId(int reviewerId) {
		this.reviewerId = reviewerId;
	}
	public double getReviewerWeight() {
		return reviewerWeight;
	}
	public void setReviewerWeight(double reviewerWeight) {
		this.reviewerWeight = reviewerWeight;
	}
	public void setCount(int count){
		this.count = count;
	}
	public int getCount(){
		return this.count;
	}
}
