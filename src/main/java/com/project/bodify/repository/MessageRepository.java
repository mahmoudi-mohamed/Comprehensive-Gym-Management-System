package com.project.bodify.repository;



import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.project.bodify.model.Message;





@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {

    // Find messages by recipient's id (toid)
    List<Message> findByToid(String toid);

    // Check if messages exist for a specific recipient's id
    Boolean existsByToid(String toid);

    // Custom query using JPQL to find messages between two users
    @Query("SELECT m FROM Message m WHERE (m.fromid = ?1 AND m.toid = ?2) OR (m.toid = ?1 AND m.fromid = ?2)")
    List<Message> findByFromidAndToidOrToidAndFromid(String fromid, String toid);

    // Find messages by sender's or recipient's id
    List<Message> findByFromidOrToid(String fromid, String toid);

    // Check if messages exist for a specific sender's id
    Boolean existsByFromid(String fromid);
}
