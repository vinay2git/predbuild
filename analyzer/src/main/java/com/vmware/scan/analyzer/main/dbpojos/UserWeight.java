package com.vmware.scan.analyzer.main.dbpojos;

public class UserWeight {

	private int userId;
	private double userWeight;
	private int count;
	
	public UserWeight(){
		
	} 
	
	public UserWeight(int userId, double userWeight, int count) {
		super();
		this.userId = userId;
		this.userWeight = userWeight;
		this.count = count;
	}
	
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public double getUserWeight() {
		return userWeight;
	}
	public void setUserWeight(double userWeight) {
		this.userWeight = userWeight;
	}
	public int getCount() {
		return this.count;
	}
	public void setCount(int count){
		this.count = count;
	}
	public void computeWeight(double impactScore, double passRate){
		this.count++;
		this.userWeight = impactScore * passRate;
	}
}
