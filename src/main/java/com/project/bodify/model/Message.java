package com.project.bodify.model;



import java.util.Date;
import javax.persistence.*;

import lombok.Getter;
import lombok.Setter;

@Entity // Represents a table in MySQL
@Table(name = "messages") // Table name in MySQL
@Getter
@Setter
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto-generate ID for MySQL
    private Long id;

    @Column(name = "toid", nullable = false)
    private String toid;

    @Column(name = "fromid", nullable = false)
    private String fromid;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "date", nullable = false)
    private Date date;

    @Column(name = "view", nullable = false)
    private int view;

    @Column(name = "img")
    private String img;

    @Column(name = "msg", nullable = false)
    private String msg;

    public Message(String toid, String fromid, Date date, String img, String msg) {
        super();
        this.toid = toid;
        this.fromid = fromid;
        this.date = date;
        this.img = img;
        this.msg = msg;
        this.view = 0;
    }

    public Message() {
        // Default constructor for JPA
    }

    // Getters and setters are handled by Lombok (@Getter, @Setter)
}
