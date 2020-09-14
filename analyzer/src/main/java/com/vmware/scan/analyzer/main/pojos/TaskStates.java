package com.vmware.scan.analyzer.main.pojos;

public enum TaskStates {

	COMPLETED(1), FAILED_CONTINUE(0), FAILED(0), SKIPPED(0), UNKNOWN_STATE(-1);
	
	private int state;
	
	private TaskStates(int state){
		this.state = state;
	}
	
	public int getValue(){
		return this.state;
	}
	
	public static int getStateValue(String state){
		for (TaskStates each : values()){
			if (each.toString().equalsIgnoreCase(state)){
				return each.getValue();
			}
		}
		
		return UNKNOWN_STATE.state;
	}
}
