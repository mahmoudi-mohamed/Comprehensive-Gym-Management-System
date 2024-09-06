package com.project.bodify.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;




@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AddInterventionDto {
   
   
    
	private Long posteid;
	private Long realisesId;
	private String machineid;
	private String localisation;
	private String priorite;
	private String descprobleme;
	
   
    
    
}
