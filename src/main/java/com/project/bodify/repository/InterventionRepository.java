package com.project.bodify.repository;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.project.bodify.model.Intervention;

@Repository
public interface InterventionRepository extends JpaRepository<Intervention, Long> {
    List<Intervention> findByDemenderId(Long id);
    List<Intervention> findByRealisesId(Long id);
    List<Intervention> findByRealisesIdAndEtat(Long realisesId, String etat);
    List<Intervention> findByEtat(String etat);
    List<Intervention> findByEtatAndPosteid(String etat, Long posteid);
    List<Intervention> findByEtatOrEtat(String etat1, String etat2);
    @Query("SELECT i FROM Intervention i WHERE DATE(i.daterepare) = :dateWithoutTime")
    List<Intervention> getInterventionsByDateRepare(@Param("dateWithoutTime") Date dateWithoutTime);


    // Method to count interventions by datedeclare
    default Map<LocalDateTime, Long> countByDatedeclare() {
        List<Intervention> interventions = findAll();
        Map<LocalDateTime, Long> countByDatedeclare = new HashMap<>();

        for (Intervention intervention : interventions) {
            LocalDateTime datedeclare = intervention.getDatedeclare();
            countByDatedeclare.put(datedeclare, countByDatedeclare.getOrDefault(datedeclare, 0L) + 1);
        }

        return countByDatedeclare;
    }
}
