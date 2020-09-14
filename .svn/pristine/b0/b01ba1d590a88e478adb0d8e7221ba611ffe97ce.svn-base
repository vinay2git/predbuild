package com.vmware.scan.analyzer.main.services;

import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.web.client.RestTemplate;

import com.vmware.scan.analyzer.main.dbpojos.Changeset;
import com.vmware.scan.analyzer.main.dbpojos.FileWeight;
import com.vmware.scan.analyzer.main.dbpojos.NameValue;
import com.vmware.scan.analyzer.main.dbpojos.ReviewerWeight;
import com.vmware.scan.analyzer.main.dbpojos.UserWeight;
import com.vmware.scan.analyzer.main.pojos.Priority;
import com.vmware.scan.analyzer.main.pojos.ReviewerRole;

@Service
public class ComputationService {

	private static Set<Changeset> changes;
	private Logger logger = Logger.getLogger(ComputationService.class);
	private static RestTemplate restTemplate = new RestTemplate();
	private static final int MAX_WEIGHT = 6;

	@Autowired
	private Environment env;

	public void setChanges(Set<Changeset> changeBatch) {
		changes = changeBatch;
	}

	public void computeFileWeight(){
		for (Changeset each : changes) {
			double totalqoc = getTotalQOCOfChange(each);
			logger.debug("CL ("+each.getId()+" has files: "+each.getFiles()+" Total QOC : "+totalqoc);
			for (NameValue eachF : each.getFiles()) {
				double qoc = 0.0;
				StringBuilder urlBuilder = new StringBuilder();
				urlBuilder.append("http://").append(env.getProperty("database.url")).append("/weights/file/").append(eachF.getName().replaceAll("/", "").replaceAll("\\.", ""));
				FileWeight fw = restTemplate.getForObject(urlBuilder.toString(), FileWeight.class);

				if ((double)eachF.getValue() != 0.0)
					qoc = (((double)eachF.getValue() / totalqoc));

				if (fw == null) {
					fw = new FileWeight();
					fw.setCount(1);
					fw.setFilename(eachF.getName());
					fw.setQoc(qoc);
				}

				fw.updateInfo(each.getWeight(), qoc);
				fw.computeWeightedMean(changes.size());				

				String url = "http://"+env.getProperty("database.url")+"/weights/file/";
				ResponseEntity<FileWeight> response = restTemplate.postForEntity(url, fw, FileWeight.class);
				Assert.isTrue(response.getStatusCode() == HttpStatus.OK, "Updating fileweight failed!");
			}
		}
	}

	private double getTotalQOCOfChange(Changeset each) {
		double totalQOC = 0.0;
		for (NameValue eachF : each.getFiles()) {
			totalQOC += (double) eachF.getValue();
		}

		return totalQOC;
	}

	public void computeOwnerWeight() {
		for (Changeset each : changes) {
			StringBuilder urlBuilder = new StringBuilder();
			urlBuilder.append("http://").append(env.getProperty("database.url")).append("/weights/user/")
			.append(each.getOwnerid());
			UserWeight uw = restTemplate.getForObject(urlBuilder.toString(), UserWeight.class);
			int count = 0;
			if (uw == null) {
				uw = new UserWeight();
				uw.setUserId(each.getOwnerid());
				count = 1;
			} else {
				count = uw.getCount();
			}
			
			double totalPriority = Priority.P0.getValue() * count;
			double totalPassRate = Priority.P0.getValue() * each.getWeight();
			double impactScore = totalPriority / (Priority.P0.getValue() * count);
			double passRate = totalPassRate / (totalPriority * MAX_WEIGHT);
			uw.computeWeight(impactScore, passRate);
			String url = "http://" + env.getProperty("database.url") + "/weights/user/";
			ResponseEntity<UserWeight> response = restTemplate.postForEntity(url, uw, UserWeight.class);
			Assert.isTrue(response.getStatusCode() == HttpStatus.OK, "Updating fileweight failed!");
		}
	}

	public void computeReviewerWeight() {
		for (Changeset each : changes) {
			for (NameValue eachNV : each.getReviews()) {
				StringBuilder urlBuilder = new StringBuilder();
				urlBuilder.append("http://").append(env.getProperty("database.url")).append("/weights/reviewer/")
				.append(eachNV.getName());
				ReviewerWeight rw = restTemplate.getForObject(urlBuilder.toString(), ReviewerWeight.class);
				int count = 0;
				if (rw == null) {
					rw = new ReviewerWeight();
					rw.setReviewerId((int)eachNV.getValue());
					count = 1;
				} else {
					count = rw.getCount();
				}
				
				int totalRoleValue = ReviewerRole.getReviewerRole(eachNV.getName()).getRoleValue() * count;
				int maxRoleValue = ReviewerRole.getReviewerRole(eachNV.getName()).getRoleValue() * count;
				double totalPassRate = Priority.P0.getValue() * each.getWeight();

				double impactScore = totalRoleValue / maxRoleValue;
				double passRate = totalPassRate / (totalRoleValue * MAX_WEIGHT);
				rw.computeWeight(impactScore, passRate);
				String url = "http://" + env.getProperty("database.url") + "/weights/reviewer/";
				ResponseEntity<ReviewerWeight> response = restTemplate.postForEntity(url, rw, ReviewerWeight.class);
				Assert.isTrue(response.getStatusCode() == HttpStatus.OK, "Updating fileweight failed!");
			}
		}
	}
}
