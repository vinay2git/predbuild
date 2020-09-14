package com.vmware.scan.analyzer.main.dbpojos;

public class ReviewerWeight {
	
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
	public int getCount(){
		return this.count;
	}
	public void setCount(int count){
		this.count = count;
	}
	public void computeWeight(double impactScore, double passRate) {
		this.count++;
		this.reviewerWeight = impactScore * passRate;
	}
}
