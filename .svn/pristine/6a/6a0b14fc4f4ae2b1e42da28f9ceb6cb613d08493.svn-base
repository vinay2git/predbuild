package com.vmware.scan.analyzer.main.pojos;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.apache.log4j.Logger;

import com.vmware.scan.analyzer.main.phases.BATPhase;
import com.vmware.scan.analyzer.main.phases.GerritPhase;
import com.vmware.scan.analyzer.main.phases.RegressionPhase;

public class ChangesetObject {
	
	Logger logger = Logger.getLogger(ChangesetObject.class);
	
	private String changeId;
	private int ownerAccountId;
	private String ownerName;
	private List<CLFile> files;
	private Map<Integer, UserInfo> reviwers;
	private GerritPhase gerritPhase;
	private BATPhase batPhase;
	private RegressionPhase regressionPhase;
	private int priority;
	
	public ChangesetObject(String[] parts) {
		this.changeId = parts[0].trim();
		this.ownerAccountId = Integer.parseInt(parts[1].trim());
		this.ownerName = parts[2].trim();
		this.reviwers = createReviewersMap(parts[3].trim());
		this.files = createFileList(parts[4]);
		this.gerritPhase = new GerritPhase(parts[5]);
		this.batPhase = new BATPhase(parts[6]);
		this.regressionPhase = new RegressionPhase(parts[7]);
		int aRandomNumber = (int) ((Math.random() * 10) % 5);
		this.priority = ((aRandomNumber == 0) ? 1 : aRandomNumber);
	}

	private Map<Integer, UserInfo> createReviewersMap(String line) {
		Map<Integer, UserInfo> reviewers = new HashMap<Integer, UserInfo>();
		String[] parts = line.split(SplittingSymbols.SEMICOLON.getValue());
		
		for (String eachPart : parts){
			// Each reviewer has to be checked for +1 or +2
			UserInfo info = new UserInfo();
			String[] reviewerData = eachPart.split(SplittingSymbols.ARROW.getValue());
			info.setRole(ReviewerRole.getReviewerRole(reviewerData[0].trim()));
			info.setUserId(Integer.parseInt(reviewerData[1].trim().split(SplittingSymbols.COLON.getValue())[0]));
			info.setUserName(reviewerData[1].trim().split(SplittingSymbols.COLON.getValue())[1]);
			reviewers.put(info.getUserId(), info);
		}
		
		return reviewers;
	}

	private List<CLFile> createFileList(String line) {
		List<CLFile> files = new ArrayList<>();
		String[] parts = line.split(SplittingSymbols.COMMA.getValue());
		
		for (String each : parts){
			CLFile file = new CLFile();
			String[] fileParts = each.split(SplittingSymbols.ARROW.getValue());
			file.setFileName(fileParts[0].trim());
			String[] qocParts = fileParts[1].split(SplittingSymbols.COLON.getValue());
			file.setInsertions(Integer.parseInt(qocParts[0].trim()));
			file.setDeletions(Integer.parseInt(qocParts[1].trim()));
			
			files.add(file);
		}
		
		return files;
	}

	public String getChangeId() {
		return changeId;
	}

	public int getOwnerAccountId() {
		return ownerAccountId;
	}

	public String getOwnerName() {
		return ownerName;
	}

	public List<CLFile> getFiles() {
		return files;
	}

	public GerritPhase getGerritPhase() {
		return gerritPhase;
	}

	public BATPhase getBatPhase() {
		return batPhase;
	}

	public RegressionPhase getRegressionPhase() {
		return regressionPhase;
	}
	
	public double getWeight(){
		return (this.gerritPhase.getWeight() + this.batPhase.getWeight() + this.regressionPhase.getWeight());
	}

	public int getPriority() {
		return priority;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == null || !(obj instanceof ChangesetObject))
			return false;
		ChangesetObject cl = (ChangesetObject) obj;
		return Objects.equals(this.changeId, cl.changeId);
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(this.changeId);
	}

	public Map<Integer, UserInfo> getReviewers() {
		return reviwers;
	}
}
