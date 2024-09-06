package com.project.bodify.dto;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;



@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class JourProductionDto implements Serializable {
	private static final long serialVersionUID = 1L;
	private Long travallerId;
	private String travallerName;
	private Long posteId;
	private String posteOperation;
	private int Objectif;
	private int production;
    private float productivite;
    private float retouche;
    private float tauxretouche=0;
    
}
