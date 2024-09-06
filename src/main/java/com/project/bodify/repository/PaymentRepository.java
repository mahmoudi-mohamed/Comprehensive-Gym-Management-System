package com.project.bodify.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.project.bodify.model.Payment;
@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
	
}
