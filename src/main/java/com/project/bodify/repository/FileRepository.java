package com.project.bodify.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.bodify.model.Files;

public interface FileRepository extends JpaRepository<Files, Long> {

	Files findByName(String name);
}
