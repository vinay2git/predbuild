package com.vmware.scan.dataaccessor.main.beans;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document (collection = "userweight")
public class UserWeight {

	@Id
	private int userId;
	private double userWeight;
	private int count;
	
	public UserWeight(){}
	
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
	public int getCount(){
		return this.count;
	}
	public void setCount(int count){
		this.count = count;
	}
}
