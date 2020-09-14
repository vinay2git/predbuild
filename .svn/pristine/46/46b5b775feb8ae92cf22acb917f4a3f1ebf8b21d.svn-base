package com.vmware.scan.analyzer.main.phases;

import com.vmware.scan.analyzer.main.pojos.SplittingSymbols;
import com.vmware.scan.analyzer.main.pojos.TaskStates;

public class BATPhase {

	private double weight;
	private static final int TOTAL_TASKS = 12;
	private static final int PHASE_WEIGHT = 3;
	
	public BATPhase(String line) {
		String[] taskParts = line.split(SplittingSymbols.ARROW.getValue())[1].split(SplittingSymbols.COMMA.getValue());
		int sum = 0;
		
		for (int i = 0; i < taskParts.length;i++){
			if (!(taskParts[i].trim().contains(SplittingSymbols.COLON.getValue()))) {
				sum += TaskStates.getStateValue(taskParts[i].split(SplittingSymbols.EQUALSTO.getValue())[1].trim());
			}
		}
		
		this.weight = (double) sum / (double) TOTAL_TASKS;
	}
	
	public double getWeight() {
		return (this.weight * PHASE_WEIGHT);
	}

}
