package com.vmware.scan.analyzer.main.pojos;

public class CLFile {
	private String fileName;
	private int insertions = 0;
	private int deletions = 0;
	
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public int getInsertions() {
		return insertions;
	}
	public void setInsertions(int insertions) {
		this.insertions = insertions;
	}
	public int getDeletions() {
		return deletions;
	}
	public void setDeletions(int deletions) {
		this.deletions = deletions;
	}
	@Override
	public String toString() {
		return this.fileName +"-"+ this.insertions +"-"+ this.deletions;
	}
	public int getTotalQOC() {
		return this.deletions + this.insertions;
	}
}
