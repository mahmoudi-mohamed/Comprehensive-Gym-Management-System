package com.project.bodify.model;




import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Transient;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public  class Intervention implements Serializable   {
	


private static final long serialVersionUID = 1L;



@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
private Long id;



private long posteid;
@NonNull
private String localisation;
public Intervention(@NonNull Long posteid, @NonNull String localisation, Long demender, String priorite,
		LocalDateTime datedeclare, String descprobleme,@NonNull String machine,@NonNull Long realisesId,@NonNull Long fixedBy) {
	super();
	this.posteid = posteid;
	this.localisation = localisation;
	this.demenderId = demender;
	this.priorite = priorite;
	this.datedeclare = datedeclare;
	this.descprobleme = descprobleme;
	this.machine = machine;
	this.realisesId = realisesId;
	this.fixedBy = fixedBy;
}


private String machine;

private Long demenderId;
private Long realisesId;
private Long fixedBy;


private String  priorite;


private LocalDateTime datedeclare;
private LocalDateTime daterepare;

private LocalDateTime datepresent;
private LocalDateTime daterealises;

private String descprobleme;

private String desctravaux;

private String etat="attente";

private String piecesutlise;

@Transient 
private String requesterName;

@Transient
private String executorName;

@Transient
private String fixedName;
@Transient
private long duretoresolved;
private long durecumile=0;

private int verif=0;
private String arret="oui";

}
