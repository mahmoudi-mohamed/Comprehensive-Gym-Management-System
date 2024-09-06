package com.project.bodify.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.bodify.model.Payment;
import com.project.bodify.service.impl.PaymentService;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

	@Autowired
    private PaymentService paymentService;

    @PostMapping
    public ResponseEntity<Payment> processPayment(@RequestBody Payment payment) {
        return new ResponseEntity<>(paymentService.processPayment(payment), HttpStatus.CREATED);
    }
}
