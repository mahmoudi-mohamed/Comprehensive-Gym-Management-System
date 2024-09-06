package com.project.bodify.repository;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.project.bodify.model.Media;
@Repository
public interface MediaRepository extends JpaRepository<Media, Long> {
	List<Media>  findByAuthorId(Long authorId);
	List<Media> findByAuthorIdAndStatus(Long authorId, String status);
}
