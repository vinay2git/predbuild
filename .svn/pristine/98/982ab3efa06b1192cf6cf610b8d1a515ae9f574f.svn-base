package com.vmware.scan.dataaccessor.main.beans;

import java.util.List;

import org.springframework.data.mongodb.core.mapping.Document;

import com.vmware.scan.dataaccessor.main.NameValue;

@Document(collection = "changes")
public class Changeset {

	private String id;
	private int ownerid;
	private String ownername;
	private List<NameValue> reviews;
	private List<NameValue> files;
	private List<NameValue> gerrit;
	private List<NameValue> bat;
	private List<NameValue> regression;
	
	public Changeset(){
		
	}
		
	public Changeset(String id, int ownerid, String ownername, List<NameValue> reviews, List<NameValue> files,
			List<NameValue> gerrit, List<NameValue> bat, List<NameValue> regression) {
		super();
		this.id = id;
		this.ownerid = ownerid;
		this.ownername = ownername;
		this.reviews = reviews;
		this.files = files;
		this.gerrit = gerrit;
		this.bat = bat;
		this.regression = regression;
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public int getOwnerid() {
		return ownerid;
	}
	public void setOwnerid(int ownerid) {
		this.ownerid = ownerid;
	}
	public String getOwnername() {
		return ownername;
	}
	public void setOwnername(String ownername) {
		this.ownername = ownername;
	}
	public List<NameValue> getReviews() {
		return reviews;
	}
	public void setReviews(List<NameValue> reviews) {
		this.reviews = reviews;
	}
	public List<NameValue> getFiles() {
		return files;
	}
	public void setFiles(List<NameValue> files) {
		this.files = files;
	}
	public List<NameValue> getGerrit() {
		return gerrit;
	}
	public void setGerrit(List<NameValue> gerrit) {
		this.gerrit = gerrit;
	}
	public List<NameValue> getBat() {
		return bat;
	}
	public void setBat(List<NameValue> bat) {
		this.bat = bat;
	}
	public List<NameValue> getRegression() {
		return regression;
	}
	public void setRegression(List<NameValue> regression) {
		this.regression = regression;
	}
}
