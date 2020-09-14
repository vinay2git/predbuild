package com.vmware.scan.collector.main.pojos;

public enum Priority {

	P0(4),P1(3),P2(2),P3(1);
	
	private int priorityValue;
	
	private Priority(int value) {
		this.priorityValue = value;
	}
	
	public static Priority getPriority(int value) {
		for (Priority eachVal : values()) {
			if (value == eachVal.getValue()) {
				return eachVal;
			}
		}
		
		return null;
	}
	
	public int getValue() {
		return this.priorityValue;
	}
	
}
