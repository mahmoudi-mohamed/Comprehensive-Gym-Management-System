package com.project.bodify.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.bodify.model.FitnessClass;
import com.project.bodify.service.impl.FitnessClassService;

@RestController
@RequestMapping("/api/classes")
public class FitnessClassController {

    @Autowired
    private FitnessClassService fitnessClassService;

    @PostMapping
    public ResponseEntity<FitnessClass> createClass(@RequestBody FitnessClass fitnessClass) {
        return new ResponseEntity<>(fitnessClassService.createClass(fitnessClass), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<FitnessClass>> getAllClasses() {
        return ResponseEntity.ok(fitnessClassService.getAllClasses());
    }
}