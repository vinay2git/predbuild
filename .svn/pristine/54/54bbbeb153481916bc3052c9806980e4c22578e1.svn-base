package com.vmware.scan.dataaccessor.main.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.vmware.scan.dataaccessor.main.beans.Changeset;
import com.vmware.scan.dataaccessor.main.repositories.ChangesetRepository;

@RestController
public class ChangesetController {

	@Autowired
	private ChangesetRepository changesetRepo;
	
	@RequestMapping("/changes")
	public List<Changeset> getChanges(){
		return changesetRepo.findAll();
	}
	
	@RequestMapping("/changes/{id}")
	public Changeset getChange(@PathVariable String id){
		return changesetRepo.findOne(id);
	}
	
	@RequestMapping(value="/changes", method=RequestMethod.POST)
	public void addChange(@RequestBody Changeset change){
		changesetRepo.save(change);
	}
	
}