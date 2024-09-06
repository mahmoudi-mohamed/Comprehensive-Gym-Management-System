package com.project.bodify.service.impl;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.bodify.model.SubscriptionPlan;
import com.project.bodify.repository.SubscriptionPlanRepository;
import com.project.bodify.service.ProduitService;

@Service
public  class SubscriptionPlanService implements ProduitService {
	
	@Autowired
    private SubscriptionPlanRepository subscriptionPlanRepository;
    
    public SubscriptionPlan createPlan(SubscriptionPlan plan) {
        return subscriptionPlanRepository.save(plan);
    }

    public SubscriptionPlan updatePlan(Long id, SubscriptionPlan planDetails) {
        SubscriptionPlan plan = subscriptionPlanRepository.findById(id).orElseThrow();
        plan.setPlanName(planDetails.getPlanName());
        plan.setPrice(planDetails.getPrice());
        plan.setDuration(planDetails.getDuration());
        return subscriptionPlanRepository.save(plan);
    }

    public void deletePlan(Long id) {
        subscriptionPlanRepository.deleteById(id);
    }

    public List<SubscriptionPlan> getAllPlans() {
        return subscriptionPlanRepository.findAll();
    }

	
	
	
 public long gettime() {
    	
    	LocalDateTime localDateTime = LocalDateTime.now(); 
    	ZoneId tunisiaZone = ZoneId.of("Africa/Tunis");
    	ZonedDateTime zdtTunisia = ZonedDateTime.of(localDateTime, tunisiaZone);
    	long millis = zdtTunisia.toInstant().toEpochMilli();
		return millis;
		
	}
    
    

}
