package com.vmware.scan.analyzer.main.controllers;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.vmware.scan.analyzer.main.dbpojos.Changeset;
import com.vmware.scan.analyzer.main.dbpojos.FileWeight;
import com.vmware.scan.analyzer.main.dbpojos.ReviewerWeight;
import com.vmware.scan.analyzer.main.dbpojos.UserWeight;
import com.vmware.scan.analyzer.main.services.ComputationService;

@RestController
public class AnalyzerController {
	
	@Autowired
	private ComputationService computationService;
	
	private RestTemplate restTemplate = new RestTemplate();
	
	@Autowired
	private Environment env;

	@RequestMapping("/hello")
	public String sayHi(){
		return "Hi!";
	}
	
	@RequestMapping("/analyzer/compute")
	public void compute(){
		StringBuilder urlBuilder = new StringBuilder();
		urlBuilder.append("http://").append(this.env.getProperty("database.url")).append("/changes");
		String response = restTemplate.getForObject(urlBuilder.toString(), String.class);
		
		Gson gson = new Gson();
		
		JsonParser parser = new JsonParser();
		JsonElement responseInJson = parser.parse(response);
		JsonArray changeArray = responseInJson.getAsJsonArray();
		
		Set<Changeset> changes = new HashSet<Changeset>();
		
		for (JsonElement each : changeArray){
			changes.add(gson.fromJson(each, Changeset.class));
		}
		
		computationService.setChanges(changes);
		computationService.computeFileWeight();
		computationService.computeOwnerWeight();
		computationService.computeReviewerWeight();
	}
	
	@RequestMapping("/analyzer/predict")
	public String predict(@PathVariable String changeId){
		// TODO: Getting change list details from gerrit
		Changeset cl = new Changeset();
		StringBuilder urlBuilder = new StringBuilder();
		urlBuilder.append("http://").append(env.getProperty("database.url")).append("/weights/user/").append(cl.getOwnerid());
		UserWeight uw = restTemplate.getForObject(urlBuilder.toString(), UserWeight.class);

		urlBuilder.append("http://").append(env.getProperty("database.url")).append("/weights/file/").append(cl.getFiles().get(0).getName());
		FileWeight fw = restTemplate.getForObject(urlBuilder.toString(), FileWeight.class);
		
		urlBuilder.append("http://").append(env.getProperty("database.url")).append("/weights/reviewer/").append(cl.getReviews().get(0).getName());
		ReviewerWeight rw = restTemplate.getForObject(urlBuilder.toString(), ReviewerWeight.class);
		
		double prediction = fw.getWeight() * rw.getReviewerWeight() * uw.getUserWeight();
		// TODO: Threshold comparison logic
		
		return Double.toString(prediction);
	}
}
