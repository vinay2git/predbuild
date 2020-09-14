package com.vmware.scan.analyzer.main.pojos;

public enum ReviewerRole {

	PLUSONE(1), PLUSTWO(2), ZERO(0);
	
	private int roleValue;
	
	private ReviewerRole(int roleValue){
		this.roleValue = roleValue;
	}
	
	public int getRoleValue(){
		return this.roleValue;
	}
	
	public static ReviewerRole getReviewerRole(String roleString){
		if (roleString.equalsIgnoreCase("one")){
			return PLUSONE;
		} else if (roleString.equalsIgnoreCase("two")){
			return PLUSTWO;
		} else {
			return ZERO;
		}
	}
	
}
