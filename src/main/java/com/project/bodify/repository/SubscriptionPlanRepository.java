package com.project.bodify.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.project.bodify.model.SubscriptionPlan;
@Repository
public interface SubscriptionPlanRepository extends JpaRepository<SubscriptionPlan, Long> {
	
}
