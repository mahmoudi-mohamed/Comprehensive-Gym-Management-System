package com.project.bodify.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.project.bodify.model.Media;
@Repository
public interface ProduitRepository extends JpaRepository<Media, Long> {
    
}
