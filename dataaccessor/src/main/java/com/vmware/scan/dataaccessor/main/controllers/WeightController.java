package com.vmware.scan.dataaccessor.main.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.vmware.scan.dataaccessor.main.beans.FileWeight;
import com.vmware.scan.dataaccessor.main.beans.ReviewerWeight;
import com.vmware.scan.dataaccessor.main.beans.UserWeight;
import com.vmware.scan.dataaccessor.main.repositories.FileWeightRepository;
import com.vmware.scan.dataaccessor.main.repositories.ReviewerWeightRepository;
import com.vmware.scan.dataaccessor.main.repositories.UserWeightRepository;

@RestController
public class WeightController {

	@Autowired
	private UserWeightRepository userWeightRepo;
	
	@Autowired
	private ReviewerWeightRepository reviewerWeightRepo;
	
	@Autowired
	private FileWeightRepository fileWeightRepo;

	
	@RequestMapping("/weights/user/{id}")
	public UserWeight getUserWeight(@PathVariable int id){
		return userWeightRepo.findOne(id);
	}
	
	@RequestMapping("/weights/file/{name}")
	public FileWeight getFileWeight(@PathVariable String name){
		name = name.replaceAll("/", "").replaceAll("\\.", "");
		return fileWeightRepo.findOne(name);
	}
	
	@RequestMapping("/weights/reviewer/{id}")
	public ReviewerWeight getReviewerWeight(@PathVariable int id){
		return reviewerWeightRepo.findOne(id);
	}
	
	@RequestMapping(value = "/weights/file", method=RequestMethod.POST)
	public void addFileWeight(@RequestBody FileWeight body){
		String name = body.getName().replaceAll("/", "").replaceAll("\\.", "");
		body.setFilename(name);
		fileWeightRepo.save(body);
	}
	
	@RequestMapping(value = "/weights/user", method=RequestMethod.POST)
	public void addUserWeight(@RequestBody UserWeight body){
		userWeightRepo.save(body);
	}
	
	@RequestMapping(value = "/weights/reviewer", method=RequestMethod.POST)
	public void addReviewerWeight(@RequestBody ReviewerWeight body){
		reviewerWeightRepo.save(body);
	}
	
}
