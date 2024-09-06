package com.project.bodify.service.impl;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.bodify.model.Payment;
import com.project.bodify.model.SubscriptionPlan;
import com.project.bodify.repository.PaymentRepository;
import com.project.bodify.repository.SubscriptionPlanRepository;
import com.project.bodify.service.ProduitService;

@Service
public  class PaymentService implements ProduitService {
	
	 @Autowired
	    private PaymentRepository paymentRepository;

	    public Payment processPayment(Payment payment) {
	        return paymentRepository.save(payment);
	    }

	
	
	
 public long gettime() {
    	
    	LocalDateTime localDateTime = LocalDateTime.now(); 
    	ZoneId tunisiaZone = ZoneId.of("Africa/Tunis");
    	ZonedDateTime zdtTunisia = ZonedDateTime.of(localDateTime, tunisiaZone);
    	long millis = zdtTunisia.toInstant().toEpochMilli();
		return millis;
		
	}
    
    

}
