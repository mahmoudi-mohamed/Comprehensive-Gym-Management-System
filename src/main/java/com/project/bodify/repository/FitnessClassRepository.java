package com.project.bodify.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.project.bodify.model.FitnessClass;
@Repository
public interface FitnessClassRepository extends JpaRepository<FitnessClass, Long> {
	
}
