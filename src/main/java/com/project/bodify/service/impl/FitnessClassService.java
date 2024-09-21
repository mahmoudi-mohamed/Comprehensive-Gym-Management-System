package com.project.bodify.service.impl;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.bodify.model.FitnessClass;
import com.project.bodify.repository.FitnessClassRepository;

@Service
public  class FitnessClassService {
	
	@Autowired
    private FitnessClassRepository fitnessClassRepository;

    public FitnessClass createClass(FitnessClass fitnessClass) {
        return fitnessClassRepository.save(fitnessClass);
    }

    public List<FitnessClass> getAllClasses() {
        return fitnessClassRepository.findAll();
    }
	
	
	
 public long gettime() {
    	
    	LocalDateTime localDateTime = LocalDateTime.now(); 
    	ZoneId tunisiaZone = ZoneId.of("Africa/Tunis");
    	ZonedDateTime zdtTunisia = ZonedDateTime.of(localDateTime, tunisiaZone);
    	long millis = zdtTunisia.toInstant().toEpochMilli();
		return millis;
		
	}
    
    

}
