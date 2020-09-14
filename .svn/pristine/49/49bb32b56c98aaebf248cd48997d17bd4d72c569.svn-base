package com.vmware.scan.analyzer.main.pojos;

public class UserInfo {

	private int userId;
	private double impactScore;
	private double passRate;
	private double userScore;
	private String userName;
	private ReviewerRole role;
	
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public double getImpactScore() {
		return impactScore;
	}
	public void setImpactScore(double impactScore) {
		this.impactScore = impactScore;
	}
	public double getPassRate() {
		return passRate;
	}
	public void setPassRate(double passRate) {
		this.passRate = passRate;
	}
	public double getUserScore() {
		return userScore;
	}
	public void done() {
		this.userScore  = (this.passRate * this.impactScore);
	}
	@Override
	public String toString() {
		return this.userId + "-" + this.userName + "-" + this.impactScore + "-" + this.passRate + "-" + this.userScore; 
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public ReviewerRole getRole() {
		return role;
	}
	public void setRole(ReviewerRole role) {
		this.role = role;
	}
}
