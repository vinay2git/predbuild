package com.vmware.scan.collector.main.pojos;

import java.util.List;

public class TaskDetails {
	private String taskName;
	private String taskStatus;
	private List<SuiteDetails> suites;
	
	public String getTaskName() {
		return taskName;
	}
	
	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}

	public String getTaskStatus() {
		return taskStatus;
	}

	public void setTaskStatus(String taskStatus) {
		this.taskStatus = taskStatus;
	}

	public List<SuiteDetails> getSuites() {
		return suites;
	}

	public void setSuites(List<SuiteDetails> suites) {
		this.suites = suites;
	}
}
